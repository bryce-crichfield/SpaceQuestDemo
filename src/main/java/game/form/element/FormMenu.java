package game.form.element;

import game.event.Event;
import game.form.properties.*;
import game.form.properties.layout.FormVerticalLayout;
import game.io.Keyboard;
import game.util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FormMenu extends FormElement {
    private final List<FormElement> caretChildren = new ArrayList<>();
    private Integer index = 0;

    public FormMenu(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setLayout(new FormVerticalLayout());
        this.setBorder(new FormBorder());
        this.setFillPaint(Color.BLACK);
    }

    public void onKeyPressed(Integer keycode) {
        if (getChildren().isEmpty()) {
            return;
        }

        Optional<FormElement> current = Util.optionalFromThrowable(() -> caretChildren.get(index));

        boolean changed = false;
        if (keycode == Keyboard.UP) {
            index--;
            changed = true;
        } else if (keycode == Keyboard.DOWN) {
            index++;
            changed = true;
        }

        index = Util.wrap(index, 0, caretChildren.size());

        Optional<FormElement> highlighted = Util.optionalFromThrowable(() -> caretChildren.get(index));

        if (changed && current.isPresent()) {
            current.get().getOnUnhover().fire(null);
        }

        if (changed && highlighted.isPresent()) {
            highlighted.get().getOnHover().fire(null);
        }

        if (keycode == Keyboard.PRIMARY) {
            Optional<FormElement> child = Optional.ofNullable(caretChildren.get(index));
            child.ifPresent(c -> c.getOnPrimary().fire(null));
        } else if (keycode == Keyboard.SECONDARY) {
            Optional<FormElement> child = Optional.ofNullable(caretChildren.get(index));
            child.ifPresent(c -> c.getOnSecondary().fire(null));
        } else {
            Optional<FormElement> child = Optional.ofNullable(caretChildren.get(index));
            child.ifPresent(c -> c.getOnKeyPressed().fire(keycode));
        }
    }

    public void addCaretChild(FormElement child) {
        caretChildren.add(child);
        addChild(child);
    }

    @Override
    public void onRender(Graphics2D graphics) {
        if (!getVisible())
            return;

        super.onRender(graphics);

        Optional<FormElement> child = Util.optionalFromThrowable(() -> caretChildren.get(index));
        if (child.isEmpty()) {
            return;
        }

        FormBounds bounds = child.get().getBounds();
        graphics.setColor(Color.WHITE);

        int caretSize = 10;
        int centerY = (int) (bounds.getY() + bounds.getHeight() / 2) - caretSize / 2;
        int centerX = (int) (bounds.getX() + caretSize);

        FormBounds caretBounds = new FormBounds(centerX, centerY, 10, 10);
        FormText text = new FormText();
        text.setValue(">");
        text.onRender(graphics, caretBounds);
    }
}
