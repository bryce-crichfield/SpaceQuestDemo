package game.form.element;

import game.event.Event;
import game.form.properties.*;
import game.form.properties.layout.FormLayout;
import game.form.properties.layout.FormVerticalLayout;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class FormElement {
    private final Event<Void> onPrimary = new Event<>();
    private final Event<Void> onSecondary = new Event<>();
    private final Event<Integer> onKeyPressed = new Event<>();
    private final Event<Void> onHover = new Event<>();
    private final Event<Void> onUnhover = new Event<>();
    private final List<FormElement> children = new ArrayList<>();
    private FormText text = new FormText();
    private FormAlignment horizontalTextAlignment = FormAlignment.CENTER;
    private FormAlignment verticalTextAlignment = FormAlignment.CENTER;
    private FormBounds bounds = new FormBounds(0, 0, 0, 0);
    private FormMargin margin = new FormMargin(0, 0, 0, 0);
    private FormMargin padding = new FormMargin(0, 0, 0, 0);
    private FormAlignment elementAlignment = FormAlignment.CENTER;
    private FormLayout layout = new FormVerticalLayout();
    private Boolean debug = false;
    private Boolean visible = true;
    private int rounding = 0;
    private Optional<FormElement> parent = Optional.empty();

    private FormBounds fillArea = new FormBounds(0, 0, 1, 1);
    private Paint fillPaint = new Color(0, 0, 0, 0);
    private Optional<FormBorder> border = Optional.empty();
    private Optional<BufferedImage> texture = Optional.empty();

    public FormElement() {
    }
    public FormElement(int width, int height) {
        this(new FormBounds(0, 0, width, height));
    }

    public FormElement(FormBounds bounds) {
        this.bounds = bounds;
    }

    public FormElement(int x, int y, int width, int height) {
        this(new FormBounds(x, y, width, height));
    }

    public FormElement(String value) {
        this(0, 0, 0, 0);
        FormText formText = new FormText();
        formText.setValue(value);
        formText.setFill(Color.WHITE);
        formText.setSize(16);

        this.setText(formText);
    }
    public void setBorder(FormBorder border) {
        if (border == null)
            this.border = Optional.empty();
        else
            this.border = Optional.of(border);
    }

    public final void setBounds(FormBounds bounds) {
        this.bounds = bounds;
        onLayout();
    }

    public final void setX(int x) {
        this.bounds.setX(x);
        onLayout();
    }

    public final void setY(int y) {
        this.bounds.setY(y);
        onLayout();
    }

    public final void setWidth(int width) {
        this.bounds.setWidth(width);
        onLayout();
    }

    public final void setHeight(int height) {
        this.bounds.setHeight(height);
        onLayout();
    }

    public final void setSize(int width, int height) {
        this.bounds.setWidth(width);
        this.bounds.setHeight(height);
        onLayout();
    }


    public final void setMargin(FormMargin margin) {
        this.margin = margin;
        onLayout();
    }
    public final void setPadding(FormMargin padding) {
        this.padding = padding;
        onLayout();
    }

    public final void setElementAlignment(FormAlignment elementAlignment) {
        this.elementAlignment = elementAlignment;
        onLayout();
    }
    public final void setTexture(BufferedImage texture) {
        this.texture = Optional.of(texture);
    }
    public final void setLayout(FormLayout layout) {
        this.layout = layout;
        onLayout();
    }

    public final void addChild(FormElement child) {
        children.add(child);
        child.parent = Optional.of(this);
        onLayout();
    }

    public List<FormElement> getChildren() {
        return children;
    }

    public void onLayout() {
        layout.onLayout(this, children);
        for (FormElement child : children) {
            child.onLayout();
        }
    }


    public void onRender(Graphics2D graphics) {
        if (!visible)
            return;

        graphics.setPaint(fillPaint);
        int fillX = (int) (bounds.getX() + bounds.getWidth() * fillArea.getX());
        int fillY = (int) (bounds.getY() + bounds.getHeight() * fillArea.getY());
        int fillWidth = (int) (bounds.getWidth() * fillArea.getWidth());
        int fillHeight = (int) (bounds.getHeight() * fillArea.getHeight());
        graphics.fillRoundRect(fillX, fillY, fillWidth, fillHeight, rounding, rounding);

        border.ifPresent(border -> border.onRender(graphics, bounds, rounding));
        texture.ifPresent(image -> {
            int x = (int) bounds.getX();
            int y = (int) bounds.getY();
            int width = (int) bounds.getWidth();
            int height = (int) bounds.getHeight();

            graphics.drawImage(image, x, y, width, height, null);
        });
        children.forEach(child -> child.onRender(graphics));

        if (getDebug()) {
            graphics.setColor(Color.GREEN);
            int x = (int) bounds.getX();
            int y = (int) bounds.getY();
            int width = (int) bounds.getWidth();
            int height = (int) bounds.getHeight();
            Stroke oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            graphics.drawRect(x, y, width, height);
            graphics.setStroke(oldStroke);
        }

        Shape shape = text.toShape(graphics, 0, 0);
        Rectangle textBounds = shape.getBounds();
        int textWidth = textBounds.width;
        int textHeight = textBounds.height;
        int textX = (int) bounds.getX();
        int textY = (int) bounds.getY();

        switch (horizontalTextAlignment) {
            case CENTER -> textX += (int) (bounds.getWidth() / 2) - (textWidth / 2);
            case END -> textX += (int) bounds.getWidth() - textWidth;
            default -> {
            }
        }

        switch (verticalTextAlignment) {
            case CENTER -> textY += (int) (bounds.getHeight() / 2) - (textHeight / 2) - textHeight / 4;
            case END -> textY += (int) bounds.getHeight() - textHeight - textHeight / 2;
            default -> {
            }
        }

        text.onRender(graphics, new FormBounds(textX, textY, textWidth, textHeight));
    }
}
