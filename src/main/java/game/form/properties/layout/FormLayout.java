package game.form.properties.layout;

import game.Game;
import game.form.element.FormElement;
import game.form.properties.FormBounds;
import game.util.Util;

import java.util.List;

public interface FormLayout {
    default void execute(FormElement element) {
        FormBounds bounds = element.getBounds();

        // Percentages should be between 0 and 1.
        clampBoundsToZeroThroughOne(bounds);

        // The child's bounds are described in terms of percentages of the parent/screen's bounds.
        // Calculate the absolute bounds, or parent/screen coordinates, of the child.
        attemptResizeInRelationToScreen(element, bounds);
        attemptResizeInRelationToParent(element, bounds);

        // Layout the children.
        onLayout(element, element.getChildren());

        // Layout the children's children.
        for (FormElement child : element.getChildren()) {
                child.getLayout().execute(child);
        }
    }

    private static void clampBoundsToZeroThroughOne(FormBounds bounds) {
        bounds.setX(Util.clamp(bounds.getX(), 0, 1));
        bounds.setY(Util.clamp(bounds.getY(), 0, 1));
        bounds.setWidth(Util.clamp(bounds.getWidth(), 0, 1));
        bounds.setHeight(Util.clamp(bounds.getHeight(), 0, 1));
    }

    private static void attemptResizeInRelationToParent(FormElement element, FormBounds bounds) {
        if (element.getParent().isEmpty()) {
            return;
        }

        FormBounds parentBounds = element.getParent().get().getAbsoluteBounds();
        FormBounds newAbsBounds = new FormBounds(0, 0, 0, 0);
        newAbsBounds.setX(parentBounds.getX() + parentBounds.getWidth() * bounds.getX());
        newAbsBounds.setY(parentBounds.getY() + parentBounds.getHeight() * bounds.getY());
        newAbsBounds.setWidth(parentBounds.getWidth() * bounds.getWidth());
        newAbsBounds.setHeight(parentBounds.getHeight() * bounds.getHeight());
        element.setAbsoluteBounds(newAbsBounds);
    }

    private static void attemptResizeInRelationToScreen(FormElement element, FormBounds bounds) {
        if (element.getParent().isPresent()) {
            return;
        }

        FormBounds screenBounds = new FormBounds(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        FormBounds newAbsBounds = new FormBounds(0, 0, 0, 0);
        newAbsBounds.setX(bounds.getX() * screenBounds.getWidth());
        newAbsBounds.setY(bounds.getY() * screenBounds.getHeight());
        newAbsBounds.setWidth(bounds.getWidth() * screenBounds.getWidth());
        newAbsBounds.setHeight(bounds.getHeight() * screenBounds.getHeight());
        element.setAbsoluteBounds(newAbsBounds);
    }

    void onLayout(FormElement parent, List<FormElement> children);
}
