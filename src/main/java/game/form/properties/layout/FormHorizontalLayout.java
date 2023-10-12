package game.form.properties.layout;

import game.form.element.FormElement;
import game.form.properties.FormBounds;

import java.util.List;

public class FormHorizontalLayout implements FormLayout {
    @Override
    public void onLayout(FormElement parent, List<FormElement> children) {
        float[] total = new float[2];
        for (FormElement child : children) {
            total[0] += child.getBounds().getWidth();
            total[0] += child.getMargin().getLeft();
            total[0] += child.getMargin().getRight();

            total[1] += child.getBounds().getHeight();
            total[1] += child.getMargin().getTop();
            total[1] += child.getMargin().getBottom();
        }
        float totalFractionalWidth = total[0];
        float totalFractionalHeight = total[1];

        float offsetFractionalX = 0;

        for (FormElement child : children) {
            float currentFractionalWidth = child.getBounds().getWidth();
            currentFractionalWidth += child.getMargin().getLeft();
            currentFractionalWidth += child.getMargin().getRight();

            float offsetFractionalY = 0;
            float currentFractionalHeight = child.getBounds().getHeight();
            float newFractionalHeight = 0;
            switch (child.getElementAlignment()) {
                case START -> {
                    newFractionalHeight = currentFractionalHeight - child.getMargin().getBottom() - child.getMargin().getTop();
                    offsetFractionalY = child.getMargin().getTop();
                }
                case CENTER -> {
                    newFractionalHeight = currentFractionalHeight - child.getMargin().getTop() - child.getMargin().getBottom();
                    offsetFractionalY = 0.5f - (newFractionalHeight / 2f);
                }
                case END -> {
                    newFractionalHeight = currentFractionalHeight - child.getMargin().getTop() - child.getMargin().getBottom();
                    offsetFractionalY = 1 - newFractionalHeight - child.getMargin().getBottom();
                }
                default -> {
                    throw new IllegalStateException("Unexpected value: " + child.getElementAlignment());
                }
            }

            float newFractionalWidth = currentFractionalWidth / totalFractionalWidth;
            FormBounds newBounds = child.getBounds().copy();
            newBounds.setY(offsetFractionalY);
            newBounds.setX(offsetFractionalX);
            newBounds.setHeight(newFractionalHeight);
            newBounds.setWidth(newFractionalWidth);

            child.setBounds(newBounds);

            offsetFractionalX += newFractionalWidth;
        }

    }
}
