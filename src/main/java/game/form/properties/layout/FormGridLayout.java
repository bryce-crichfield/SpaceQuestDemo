package game.form.properties.layout;

import game.form.element.FormElement;
import game.form.properties.FormBounds;

import java.awt.*;
import java.util.List;

public class FormGridLayout implements FormLayout {
    int colsCount;
    int rowsCount;

    public FormGridLayout(int colsCount, int rowsCount) {
        this.colsCount = colsCount;
        this.rowsCount = rowsCount;
    }

    @Override
    public void onLayout(FormElement parent, List<FormElement> children) {
        int parentX = (int) parent.getBounds().getX();
        int parentY = (int) parent.getBounds().getY();
        int parentWidth = (int) parent.getBounds().getWidth();
        int parentHeight = (int) parent.getBounds().getHeight();

        int parentMarginLeft = (int) parent.getMargin().getLeft();
        int parentMarginRight = (int) parent.getMargin().getRight();
        int parentMarginTop = (int) parent.getMargin().getTop();
        int parentMarginBottom = (int) parent.getMargin().getBottom();

        int parentPaddingLeft = (int) parent.getPadding().getLeft();
        int parentPaddingRight = (int) parent.getPadding().getRight();
        int parentPaddingTop = (int) parent.getPadding().getTop();
        int parentPaddingBottom = (int) parent.getPadding().getBottom();
        int paddingHorizontal = colsCount * parentPaddingLeft + colsCount * parentPaddingRight;
        int paddingVertical = rowsCount * parentPaddingTop + rowsCount * parentPaddingBottom;

        int gridWidth = parentWidth - parentMarginLeft - parentMarginRight - paddingHorizontal;
        int gridHeight = parentHeight - parentMarginTop - parentMarginBottom - paddingVertical;

        int childWidth = gridWidth / colsCount;
        int childHeight = gridHeight / rowsCount;

        int offsetX = parentX + parentMarginLeft + parentPaddingLeft;
        int offsetY = parentY + parentMarginTop + parentPaddingTop;

        int index = 0;
        for (int x = 0; x < colsCount; x++) {
            for (int y = 0; y < rowsCount; y++) {
                index = x + y * colsCount;

                boolean hasChild = index < children.size();
                if (!hasChild) {
                    break;
                }

                FormElement child = children.get(index);
                child.setBounds(new FormBounds(offsetX, offsetY, childWidth, childHeight));

                offsetY += childHeight + parentPaddingTop + parentPaddingBottom;
                if (y == rowsCount - 1) {
                    offsetY = parentY + parentMarginTop + parentPaddingTop;
                    offsetX += childWidth + parentPaddingLeft + parentPaddingRight;
                }
            }
        }
    }
}
