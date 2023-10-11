package game.form.properties;

import lombok.*;

import java.awt.*;

@Data
@RequiredArgsConstructor
public class FormFill {
    @NonNull
    private Paint paint;

    @NonNull
    private Integer roundness;

    public FormFill() {
        this(Color.WHITE, 0);
    }

    public void onRender(Graphics2D graphics, FormBounds bounds) {
        graphics.setPaint(paint);
        graphics.fillRoundRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(),
                               (int) bounds.getHeight(), roundness, roundness
        );
    }
}
