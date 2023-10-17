package game.form.properties.layout;

import game.form.element.FormElement;
import game.form.properties.FormAlignment;
import game.form.properties.FormBounds;

import java.util.List;

public class FormVerticalLayout implements FormLayout {
    FormAlignment layoutAlignment;

    public FormVerticalLayout() {
        this.layoutAlignment = FormAlignment.START;
    }

    public FormVerticalLayout(FormAlignment layoutAlignment) {
        this.layoutAlignment = layoutAlignment;
    }

    @Override
    public void onLayout(FormElement parent, List<FormElement> children) {
        int totalChildrenHeight = 0;
        for (FormElement child : children) {
            totalChildrenHeight += child.getBounds().getHeight();
            totalChildrenHeight += child.getPadding().getTop();
            totalChildrenHeight += child.getPadding().getBottom();
        }

        int parentX = (int) parent.getBounds().getX();
        int parentY = (int) parent.getBounds().getY();
        int parentWidth = (int) parent.getBounds().getWidth();
        int parentHeight = (int) parent.getBounds().getHeight();

        int parentMarginLeft = (int) parent.getMargin().getLeft();
        int parentMarginRight = (int) parent.getMargin().getRight();
        int parentMarginTop = (int) parent.getMargin().getTop();
        int parentMarginBottom = (int) parent.getMargin().getBottom();

        int offsetY = 0;
        switch (layoutAlignment) {
            case START -> {
                offsetY = parentY + parentMarginTop;
            }
            case CENTER -> {
                offsetY = parentY + parentHeight / 2 - totalChildrenHeight / 2;
            }
            case END -> {
                offsetY = parentY + parentHeight - parentMarginBottom - totalChildrenHeight;
            }
        }

        for (FormElement child : children) {
            int childX = (int) child.getBounds().getX();
            int childY = (int) child.getBounds().getY();
            int childWidth = (int) child.getBounds().getWidth();
            int childHeight = (int) child.getBounds().getHeight();

            int childPaddingTop = (int) child.getPadding().getTop();
            int childPaddingBottom = (int) child.getPadding().getBottom();
            int childPaddingLeft = (int) child.getPadding().getLeft();
            int childPaddingRight = (int) child.getPadding().getRight();

            int childTotalWidth = childWidth + childPaddingLeft + childPaddingRight;
            int childTotalHeight = childHeight + childPaddingTop + childPaddingBottom;

            int offsetX = 0;
            offsetY += childPaddingTop;

            switch (parent.getElementAlignment()) {
                case START -> {
                    offsetX = parentX + parentMarginLeft + childPaddingLeft;
                }
                case CENTER -> {
                    int parentCenterX = parentX + parentWidth / 2;
                    int childCenterX = childX + childWidth / 2;
                    offsetX = parentCenterX - childCenterX;
                }
                case END -> {
                    offsetX = parentX + parentWidth - parentMarginRight - childTotalWidth;
                }
            }

            FormBounds bounds = new FormBounds(offsetX, offsetY, childWidth, childHeight);
            child.setBounds(bounds);
            offsetY += childTotalHeight + childPaddingBottom;
        }
    }
}
