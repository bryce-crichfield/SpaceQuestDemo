package game.form.properties.layout;

import game.form.element.FormElement;
import game.form.properties.FormBounds;

import java.awt.*;
import java.util.List;

public class FormGridLayout implements FormLayout {
    int colsCount;
    int rowsCount;

    float horizontalSpacing = 0.1f;
    float verticalSpacing = 0.1f;

    public FormGridLayout(int colsCount, int rowsCount) {
        this.colsCount = colsCount;
        this.rowsCount = rowsCount;
    }

    @Override
    public void onLayout(FormElement parent, List<FormElement> children) {
        int childIndex = 0;
        float childWidth  = 1 / (colsCount + (colsCount * horizontalSpacing));
        float childHeight = 1 / (rowsCount + (rowsCount * verticalSpacing));

        for (int x = 0; x < colsCount; x++) {
            for (int y = 0; y < rowsCount; y++) {
                if (childIndex >= children.size()) {
                    return;
                }

                FormElement child = children.get(childIndex);
                float childX = x * childWidth;
                childX += childWidth * horizontalSpacing;
                float childY = y * childHeight;
                childY += childHeight * verticalSpacing;

                FormBounds bounds = new FormBounds(childX, childY, childWidth, childHeight);

                childIndex++;
            }
        }

        return;
    }
}
