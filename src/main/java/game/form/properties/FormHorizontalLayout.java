package game.form.properties;

import game.form.element.FormElement;

import java.util.List;

public class FormHorizontalLayout implements FormLayout {
    @Override
    public void execute(FormElement parent, List<FormElement> children, FormBounds margin) {
        float x = 0;
        for (FormElement child : children) {
            float childY = 0;
            int percentHeight = (int) (child.getBounds().getHeight() * 100);

            switch (child.getElementAlignment()) {
                case START -> childY = 0;
                case CENTER -> {
                    int childPercent = (int) (50 - (0.5 * percentHeight));
                    childY = childPercent / 100f;
                }
                case END -> {
                    int childPercent = 100 - percentHeight;
                    childY = childPercent / 100f;
                }
            }

            FormBounds newBounds = child.getBounds().copy();
            newBounds.setX(x);
            newBounds.setY(childY);
            child.setBounds(newBounds);
            x += child.getBounds().getWidth();
        }
    }
}
