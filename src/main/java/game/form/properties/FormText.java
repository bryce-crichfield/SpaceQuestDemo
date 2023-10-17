package game.form.properties;

import lombok.*;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public class FormText {
    @NonNull
    private String value;
    @NonNull
    private Integer size;
    @NonNull
    private Paint fill;
    @NonNull
    private Optional<FormBorder> border;

    public FormText() {
        this("", 12, Color.WHITE, Optional.empty());
    }

    public void onRender(Graphics2D graphics, FormBounds bounds) {

        int textX = (int) bounds.getX();
        int textY = (int) bounds.getY();
        Shape shape = this.toShape(graphics, textX, textY);

        Paint restorePaint = graphics.getPaint();
        Stroke restoreStroke = graphics.getStroke();

        graphics.setPaint(fill);
        graphics.fill(shape);

        border.ifPresent(formBorder -> {
            graphics.setStroke(
                    new BasicStroke(formBorder.getThicknessOutline(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.setPaint(formBorder.getOutlineColor());
            graphics.draw(shape);

            graphics.setStroke(
                    new BasicStroke(formBorder.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.setPaint(formBorder.getColor());
            graphics.draw(shape);
        });

        graphics.setPaint(restorePaint);
        graphics.setStroke(restoreStroke);
    }

    public Shape toShape(Graphics2D graphics, int x, int y) {
        Font font = new Font("White Rabbit", Font.PLAIN, size);
        GlyphVector glyphVector = font.createGlyphVector(graphics.getFontRenderContext(), value);
        return glyphVector.getOutline(x, y + size);
    }
}
