package game.form.element;

import game.Game;
import game.event.Event;
import game.form.properties.*;
import game.util.Util;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class FormElement {
    public final Event<Void> onPrimary = new Event<>();
    public final Event<Void> onSecondary = new Event<>();
    private final List<FormElement> children;
    private FormBounds bounds = new FormBounds(0, 0, 0, 0);
    private FormBounds margin = new FormBounds(0, 0, 0, 0);
    private FormAlignment elementAlignment = FormAlignment.CENTER;
    private FormLayout layout = new FormVerticalLayout();
    private Optional<FormFill> fill = Optional.empty();
    private Optional<FormBorder> border = Optional.empty();
    private Boolean visible = true;
    private Optional<FormElement> parent;
    private FormBounds absoluteBounds = new FormBounds(0, 0, 0, 0);
    public FormElement() {
        this(new FormBounds(0, 0, 0, 0));
    }
    public FormElement(FormBounds percentBounds) {
        this.bounds = percentBounds;
        this.parent = Optional.empty();
        this.children = new ArrayList<>();
    }

    public FormElement(int width, int height) {
        this(new FormBounds(0, 0, width / 100f, height / 100f));
    }

    public FormElement(int x, int y, int width, int height) {
        this(new FormBounds(x / 100f, y / 100f, width / 100f, height / 100f));
    }

    public void setFill(FormFill fill) {
        this.fill = Optional.of(fill);
    }

    public void setBorder(FormBorder border) {
        this.border = Optional.of(border);
    }

    public final void setBounds(FormBounds bounds) {
        this.bounds = bounds;
        onBoundsChanged();
    }

    private void onBoundsChanged() {
        bounds.setX(Util.clamp(bounds.getX(), 0, 1));
        bounds.setY(Util.clamp(bounds.getY(), 0, 1));
        bounds.setWidth(Util.clamp(bounds.getWidth(), 0, 1));
        bounds.setHeight(Util.clamp(bounds.getHeight(), 0, 1));

        if (parent.isEmpty()) {
            FormBounds screenBounds = new FormBounds(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            absoluteBounds = new FormBounds((int) (bounds.getX() * screenBounds.getWidth()),
                                            (int) (bounds.getY() * screenBounds.getHeight()),
                                            (int) (bounds.getWidth() * screenBounds.getWidth()),
                                            (int) (bounds.getHeight() * screenBounds.getHeight())
            );
        } else {
            FormBounds parentBounds = parent.get().getAbsoluteBounds();
            absoluteBounds = new FormBounds((int) (parentBounds.getX() + parentBounds.getWidth() * bounds.getX()),
                                            (int) (parentBounds.getY() + parentBounds.getHeight() * bounds.getY()),
                                            (int) (parentBounds.getWidth() * bounds.getWidth()),
                                            (int) (parentBounds.getHeight() * bounds.getHeight())
            );
        }

        layout.execute(this, children, margin);
    }

    public FormBounds getAbsoluteBounds() {
        return absoluteBounds;
    }

    public final void setMargin(FormBounds margin) {
        this.margin = margin;
        onBoundsChanged();
    }

    public final void setElementAlignment(FormAlignment elementAlignment) {
        this.elementAlignment = elementAlignment;
        onBoundsChanged();
    }

    public final void setLayout(FormLayout layout) {
        this.layout = layout;
        onBoundsChanged();
    }

    public final void addChild(FormElement child) {
        children.add(child);
        child.parent = Optional.of(this);
        onBoundsChanged();
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
    }
}
