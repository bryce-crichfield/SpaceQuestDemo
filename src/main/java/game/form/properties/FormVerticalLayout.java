package game.form.properties;

import game.form.element.FormElement;

import java.util.List;

public class FormVerticalLayout implements FormLayout {
    @Override
    public void execute(FormElement parent, List<FormElement> children, FormBounds margin) {
        float y = 0;
        for (FormElement child : children) {
            float childX = 0;
            int percentWidth = (int) (child.bounds.get().getWidth() * 100);

            switch (child.elementAlignment.get()) {
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

            int whatPercentOfParentWidthIsMargin = (int) (margin.getX() / parent.bounds.get().getWidth());
            float marginX = whatPercentOfParentWidthIsMargin / 100f;
            childX += marginX;


            child.bounds.set(child.bounds.get().withX(childX));
            child.bounds.set(child.bounds.get().withY(y));
            y += child.bounds.get().getHeight();
        }
    }
}
