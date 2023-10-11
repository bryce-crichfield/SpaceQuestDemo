package game.form.element;

import game.event.Event;
import game.form.properties.*;
import game.io.Keyboard;
import game.util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FormMenu extends FormElement {
    public final Event<FormElement> onCaretHighlight = new Event<>();
    private Integer index = 0;
    private final List<FormElement> caretChildren = new ArrayList<>();

    public FormMenu(int width, int height) {
        this(0, 0, width, height);
    }

    public FormMenu(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setLayout(new FormVerticalLayout());
        this.setBorder(new FormBorder());
        this.setFill(new FormFill());
    }

    public void onKeyPressed(Integer keycode) {
        if (getChildren().isEmpty()) {
            return;
        }

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
        if (changed && highlighted.isPresent()) {
            onCaretHighlight.fire(highlighted.get());
        }

        if (keycode == Keyboard.PRIMARY) {
            // TODO: fire event
            Optional<FormElement> child = Optional.ofNullable(caretChildren.get(index));
            child.ifPresent(c -> c.onPrimary.fire(null));
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

        FormBounds bounds = child.get().getAbsoluteBounds();
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
