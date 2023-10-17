package game.form.properties;

import lombok.Data;

@Data
public class FormMargin {
    private float top;
    private float right;
    private float bottom;
    private float left;

    public FormMargin(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }
}
