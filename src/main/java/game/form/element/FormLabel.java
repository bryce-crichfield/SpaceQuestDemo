package game.form.element;

import game.form.properties.FormAlignment;
import game.form.properties.FormBounds;
import game.form.properties.FormText;
import game.widget.UserInterface;
import lombok.Data;

import java.awt.*;

@Data
public class FormLabel extends FormElement {
    private FormText text;
    private FormAlignment horizontalTextAlignment;
    private FormAlignment verticalTextAlignment;

    public FormLabel() {
        this(0, 0, 100, 100);
    }

    public FormLabel(int width, int height) {
        this(0, 0, width, height);
    }

    public FormLabel(int x, int y, int width, int height) {
        super(x, y, width, height);

        this.text = new FormText();
        this.horizontalTextAlignment = FormAlignment.CENTER;
        this.verticalTextAlignment = FormAlignment.CENTER;
    }

    @Override
    public void onRender(Graphics2D graphics) {
        if (!getVisible()) {
            return;
        }

        super.onRender(graphics);

        UserInterface ui = new UserInterface(graphics);
        Shape shape = ui.textToShape(text.getValue(), 0, 0, text.getSize());
        Rectangle bounds = shape.getBounds();
        int textWidth = bounds.width;
        int textHeight = bounds.height;
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
