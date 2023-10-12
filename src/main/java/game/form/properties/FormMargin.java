package game.form.properties;

import lombok.Data;

@Data
public class FormMargin {
    private float top;
    private float right;
    private float bottom;
    private float left;

    public FormMargin(int top, int right, int bottom, int left) {
        this.top = top / 100f;
        this.right = right / 100f;
        this.bottom = bottom / 100f;
        this.left = left / 100f;
    }
}
