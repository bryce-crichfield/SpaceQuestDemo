package game.form.element;

import game.form.properties.FormAlignment;
import game.form.properties.FormBounds;
import game.form.properties.FormText;
import game.widget.UserInterface;

import java.awt.*;

public class FormLabel extends FormElement {
    public final FormProperty<FormText> text;
    public final FormProperty<FormAlignment> horizontalTextAlignment;
    public final FormProperty<FormAlignment> verticalTextAlignment;

    public FormLabel(int width, int height) {
        this(0, 0, width, height);
    }

    public FormLabel(int x, int y, int width, int height) {
        super(x, y, width, height);

        this.text = new FormProperty<>(new FormText());
        this.horizontalTextAlignment = new FormProperty<>(FormAlignment.CENTER);
        this.verticalTextAlignment = new FormProperty<>(FormAlignment.CENTER);

    }

    @Override
    public void onRender(Graphics2D graphics) {
        super.onRender(graphics);

        UserInterface ui = new UserInterface(graphics);
        Shape shape = ui.textToShape(text.get().getValue(), 0, 0, text.get().getSize());
        Rectangle bounds = shape.getBounds();
        int textWidth = bounds.width;
        int textHeight = bounds.height;
        int textX = (int) getAbsoluteBounds().getX();
        int textY = (int) getAbsoluteBounds().getY();

        switch (horizontalTextAlignment.get()) {
            case CENTER -> textX += (int) (getAbsoluteBounds().getWidth() / 2) - (textWidth / 2);
            case END -> textX += (int) getAbsoluteBounds().getWidth() - textWidth;
            default -> {
            }
        }

        switch (verticalTextAlignment.get()) {
            case CENTER -> textY += (int) (getAbsoluteBounds().getHeight() / 2) - (textHeight / 2) - textHeight / 4;
            case END -> textY += (int) getAbsoluteBounds().getHeight() - textHeight - textHeight / 2;
            default -> {
            }
        }

        text.get().onRender(graphics, new FormBounds(textX, textY, textWidth, textHeight));
    }
}
