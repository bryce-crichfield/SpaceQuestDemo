package game.form.element;

import game.event.Event;
import game.form.properties.*;
import game.form.properties.layout.FormLayout;
import game.form.properties.layout.FormVerticalLayout;
import game.util.UserInterface;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class FormElement {
    private final Event<Void> onPrimary = new Event<>();
    private final Event<Void> onSecondary = new Event<>();
    private final List<FormElement> children;
    private FormBounds absoluteBounds = new FormBounds(0, 0, 0, 0);
    private FormText text = new FormText();
    private FormAlignment horizontalTextAlignment = FormAlignment.CENTER;
    private FormAlignment verticalTextAlignment = FormAlignment.CENTER;
    private FormBounds bounds = new FormBounds(0, 0, 0, 0);
    private FormMargin margin = new FormMargin(0, 0, 0, 0);
    private FormAlignment elementAlignment = FormAlignment.CENTER;
    private FormLayout layout = new FormVerticalLayout();
    private Boolean debug = false;
    private Boolean visible = true;
    private Optional<FormElement> parent;
    private Optional<FormFill> fill = Optional.empty();
    private Optional<FormBorder> border = Optional.empty();

    public FormElement(int width, int height) {
        this(new FormBounds(0, 0, width / 100f, height / 100f));
    }

    public FormElement(FormBounds percentBounds) {
        this.bounds = percentBounds;
        this.parent = Optional.empty();
        this.children = new ArrayList<>();
    }

    public FormElement(int x, int y, int width, int height) {
        this(new FormBounds(x / 100f, y / 100f, width / 100f, height / 100f));
    }

    public FormElement(String value) {
        this();
        FormText formText = new FormText();
        formText.setValue(value);
        formText.setFill(Color.WHITE);
        formText.setSize(16);

        this.setText(formText);
    }

    public FormElement() {
        this(new FormBounds(0, 0, 0, 0));
    }

    public void setFill(FormFill fill) {
        this.fill = Optional.of(fill);
    }

    public void setBorder(FormBorder border) {
        this.border = Optional.of(border);
    }

    public final void setBounds(FormBounds bounds) {
        this.bounds = bounds;
//        layout.execute(this);
    }

    public final void setMargin(FormMargin margin) {
        this.margin = margin;
//        layout.execute(this);
    }

    public final void setElementAlignment(FormAlignment elementAlignment) {
        this.elementAlignment = elementAlignment;
//        layout.execute(this);
    }

    public final void setLayout(FormLayout layout) {
        this.layout = layout;
//        layout.execute(this);
    }

    public final void addChild(FormElement child) {
        children.add(child);
        child.parent = Optional.of(this);
//        layout.execute(this);
    }

    public List<FormElement> getChildren() {
        return children;
    }

    public void onRender(Graphics2D graphics) {
        if (!visible)
            return;

        fill.ifPresent(fill -> fill.onRender(graphics, absoluteBounds));
        border.ifPresent(border -> border.onRender(graphics, absoluteBounds));
        children.forEach(child -> child.onRender(graphics));

        if (getDebug()) {
            graphics.setColor(Color.GREEN);
            int x = (int) absoluteBounds.getX();
            int y = (int) absoluteBounds.getY();
            int width = (int) absoluteBounds.getWidth();
            int height = (int) absoluteBounds.getHeight();
            Stroke oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            graphics.drawRect(x, y, width, height);
            graphics.setStroke(oldStroke);
        }

        UserInterface ui = new UserInterface(graphics);
        Shape shape = ui.textToShape(text.getValue(), 0, 0, text.getSize());
        Rectangle textBounds = shape.getBounds();
        int textWidth = textBounds.width;
        int textHeight = textBounds.height;
        int textX = (int) getAbsoluteBounds().getX();
        int textY = (int) getAbsoluteBounds().getY();

        switch (horizontalTextAlignment) {
            case CENTER -> textX += (int) (getAbsoluteBounds().getWidth() / 2) - (textWidth / 2);
            case END -> textX += (int) getAbsoluteBounds().getWidth() - textWidth;
            default -> {
            }
        }

        switch (verticalTextAlignment) {
            case CENTER -> textY += (int) (getAbsoluteBounds().getHeight() / 2) - (textHeight / 2) - textHeight / 4;
            case END -> textY += (int) getAbsoluteBounds().getHeight() - textHeight - textHeight / 2;
            default -> {
            }
        }

        text.onRender(graphics, new FormBounds(textX, textY, textWidth, textHeight));
    }
}
