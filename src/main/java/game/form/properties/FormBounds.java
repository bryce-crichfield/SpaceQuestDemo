package game.form.properties;

import lombok.*;


@Data
@AllArgsConstructor
public class FormBounds {
    private float x = 0;
    private float y = 0;
    private float width = 100;
    private float height = 100;

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
