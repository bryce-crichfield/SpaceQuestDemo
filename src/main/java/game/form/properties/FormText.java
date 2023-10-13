package game.form.properties;

import game.util.UserInterface;
import lombok.*;

import java.awt.*;
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
        UserInterface ui = new UserInterface(graphics);

        int textX = (int) bounds.getX();
        int textY = (int) bounds.getY();
        Shape shape = ui.textToShape(value, textX, textY, size);

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
                    new BasicStroke(formBorder.getThicknessInlay(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.setPaint(formBorder.getInlayColor());
            graphics.draw(shape);
        });

        graphics.setPaint(restorePaint);
        graphics.setStroke(restoreStroke);
    }
}
