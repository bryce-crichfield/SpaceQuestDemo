package game.form.properties;

import game.form.element.FormElement;
import lombok.NoArgsConstructor;

import java.util.List;

public class FormVerticalLayout implements FormLayout {
    @Override
    public void execute(FormElement parent, List<FormElement> children, FormBounds margin) {
        float y = 0;
        for (FormElement child : children) {
            float childX = 0;
            int percentWidth = (int) (child.getBounds().getWidth() * 100);

            switch (child.getElementAlignment()) {
                case START -> childX = 0;
                case CENTER -> {
                    int childPercent = (int) (50 - (0.5 * percentWidth));
                    childX = childPercent / 100f;
                }
                case END -> {
                    int childPercent = 100 - percentWidth;
                    childX = childPercent / 100f;
                }
            }

            int whatPercentOfParentWidthIsMargin = (int) (margin.getX() / parent.getBounds().getWidth());
            float marginX = whatPercentOfParentWidthIsMargin / 100f;
            childX += marginX;

            FormBounds newBounds = child.getBounds().copy();
            newBounds.setX(childX);
            newBounds.setY(y);
            child.setBounds(newBounds);
            y += child.getBounds().getHeight();
        }
    }
}
