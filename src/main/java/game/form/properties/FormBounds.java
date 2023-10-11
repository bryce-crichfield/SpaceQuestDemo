package game.form.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@With
@Builder
@AllArgsConstructor
public class FormBounds {
    @Getter
    private float x = 0;
    @Getter
    private float y = 0;
    @Getter
    private float width = 100;
    @Getter
    private float height = 100;

    public FormBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
