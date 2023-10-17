package game.form.properties;


import lombok.*;

import java.awt.*;

@Data
@RequiredArgsConstructor
public class FormBorder {
    @NonNull
    private Color color;
    @NonNull
    private Color outlineColor;
    @NonNull
    private Integer rounding;
    @NonNull
    private Integer thickness;
    @NonNull
    private Integer thicknessOutline;
    @NonNull
    private FormLine lineStyle;

    public FormBorder() {
        this(Color.WHITE, Color.BLACK, 25, 3, 6, FormLine.SOLID);
    }

    public void onRender(Graphics2D graphics, FormBounds bounds) {
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        Stroke stroke = graphics.getStroke();
        Paint paint = graphics.getPaint();

        graphics.setStroke(new BasicStroke(thicknessOutline, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setPaint(outlineColor);
        graphics.drawRoundRect(x, y, width, height, rounding, rounding);

        graphics.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setPaint(color);
        graphics.drawRoundRect(x, y, width, height, rounding, rounding);

        graphics.setStroke(stroke);
        graphics.setPaint(paint);
    }


}
