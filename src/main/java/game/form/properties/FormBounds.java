package game.form.properties;

import lombok.*;


@Data
@AllArgsConstructor
public class FormBounds {
    private float x = 0;
    private float y = 0;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    private float width = 100;
    private float height = 100;

    public FormBounds add(FormBounds bounds) {
        return new FormBounds(x + bounds.x, y + bounds.y, width + bounds.width, height + bounds.height);
    }

    public static FormBounds fractional(float x, float y, float width, float height) {
        return new FormBounds(x / 100f, y / 100f, width / 100f, height / 100f);
    }

    public static FormBounds percent(int x, int y, int width, int height) {
        return new FormBounds(x, y, width, height);
    }

    public FormBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public FormBounds copy() {
        return new FormBounds(x, y, width, height);
    }
}
