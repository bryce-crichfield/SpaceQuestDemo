package game.form.element;

import game.Game;
import game.event.Event;
import game.form.properties.*;
import game.util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FormElement {
    public final FormProperty<FormBounds> bounds = new FormProperty<>(new FormBounds(0, 0, 0, 0));
    public final FormProperty<FormBounds> margin = new FormProperty<>(new FormBounds(0, 0, 0, 0));
    public final FormProperty<FormAlignment> elementAlignment = new FormProperty<>(FormAlignment.CENTER);
    public final FormProperty<FormLayout> layout = new FormProperty<>(new FormVerticalLayout());
    public final FormProperty<Optional<FormFill>> fill = new FormProperty<>(Optional.empty());
    public final FormProperty<Optional<FormBorder>> border = new FormProperty<>(Optional.empty());
    public final Event<Void> onPrimary = new Event<>();
    public final Event<Void> onSecondary = new Event<>();
    private final List<FormElement> children;
    private Optional<FormElement> parent;
    private FormBounds absoluteBounds = new FormBounds(0, 0, 0, 0);

    public FormElement(int width, int height) {
        this(new FormBounds(0, 0, width / 100f, height / 100f));
    }

    public FormElement(FormBounds percentBounds) {
        this.bounds.set(percentBounds);
        this.parent = Optional.empty();
        this.children = new ArrayList<>();

        this.elementAlignment.onChange().listenWith((alignment) -> onBoundsChanged());
        this.layout.onChange().listenWith((layout) -> onBoundsChanged());
        this.bounds.onChange().listenWith((bounds) -> onBoundsChanged());
    }

    private void onBoundsChanged() {
        FormBounds percentBounds = bounds.get();
        percentBounds = percentBounds.withX(Util.clamp(percentBounds.getX(), 0, 1));
        percentBounds = percentBounds.withY(Util.clamp(percentBounds.getY(), 0, 1));
        percentBounds = percentBounds.withWidth(Util.clamp(percentBounds.getWidth(), 0, 1));
        percentBounds = percentBounds.withHeight(Util.clamp(percentBounds.getHeight(), 0, 1));

        if (parent.isEmpty()) {
            FormBounds screenBounds = new FormBounds(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            absoluteBounds = new FormBounds((int) (percentBounds.getX() * screenBounds.getWidth()),
                                            (int) (percentBounds.getY() * screenBounds.getHeight()),
                                            (int) (percentBounds.getWidth() * screenBounds.getWidth()),
                                            (int) (percentBounds.getHeight() * screenBounds.getHeight())
            );
        } else {
            FormBounds parentBounds = parent.get().getAbsoluteBounds();
            absoluteBounds = new FormBounds(
                    (int) (parentBounds.getX() + parentBounds.getWidth() * percentBounds.getX()),
                    (int) (parentBounds.getY() + parentBounds.getHeight() * percentBounds.getY()),
                    (int) (parentBounds.getWidth() * percentBounds.getWidth()),
                    (int) (parentBounds.getHeight() * percentBounds.getHeight())
            );
        }

        layout.get().execute(this, children, margin.get());
    }

    public FormBounds getAbsoluteBounds() {
        return absoluteBounds;
    }

    public FormElement(int x, int y, int width, int height) {
        this(new FormBounds(x / 100f, y / 100f, width / 100f, height / 100f));
    }

    public void addChild(FormElement child) {
        children.add(child);
        child.parent = Optional.of(this);
        onBoundsChanged();
    }

    public List<FormElement> getChildren() {
        return children;
    }

    public void onRender(Graphics2D graphics) {
        fill.get().ifPresent(fill -> fill.onRender(graphics, absoluteBounds));
        border.get().ifPresent(border -> border.onRender(graphics, absoluteBounds));
        // draw debug bounds
//         graphics.setColor(Color.RED);
//            graphics.drawRect((int) absoluteBounds.getX(), (int) absoluteBounds.getY(), (int) absoluteBounds.getWidth(), (int) absoluteBounds.getHeight());
        children.forEach(child -> child.onRender(graphics));
    }
}
