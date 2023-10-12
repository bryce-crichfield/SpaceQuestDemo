package game.form.properties.layout;

import game.form.element.FormElement;
import game.form.properties.FormBounds;

import java.util.List;

public class FormVerticalLayout implements FormLayout {
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

        float offsetFractionalY = 0;

        for (FormElement child : children) {
            float currentFractionalHeight = child.getBounds().getHeight();
            currentFractionalHeight += child.getMargin().getTop();
            currentFractionalHeight += child.getMargin().getBottom();

            float offsetFractionalX = 0;
            float currentFractionalWidth = child.getBounds().getWidth();
            float newFractionalWidth = 0;
            switch (child.getElementAlignment()) {
                case START -> {
                    newFractionalWidth = currentFractionalWidth - child.getMargin().getRight() - child.getMargin().getLeft();
                    offsetFractionalX = child.getMargin().getLeft();
                }
                case CENTER -> {
                    newFractionalWidth = currentFractionalWidth - child.getMargin().getLeft() - child.getMargin().getRight();
                    offsetFractionalX = 0.5f - (newFractionalWidth / 2f);
                }
                case END -> {
                    newFractionalWidth = currentFractionalWidth - child.getMargin().getLeft() - child.getMargin().getRight();
                    offsetFractionalX = 1 - newFractionalWidth - child.getMargin().getRight();
                }
                default -> {
                    throw new IllegalStateException("Unexpected value: " + child.getElementAlignment());
                }
            }

            float newFractionalHeight = currentFractionalHeight / totalFractionalHeight;
            FormBounds newBounds = child.getBounds().copy();
            newBounds.setY(offsetFractionalY);
            newBounds.setX(offsetFractionalX);
            newBounds.setHeight(newFractionalHeight);
            newBounds.setWidth(newFractionalWidth);

            child.setBounds(newBounds);

            offsetFractionalY += newFractionalHeight;
        }
    }
}
