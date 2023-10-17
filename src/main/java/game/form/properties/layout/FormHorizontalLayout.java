package game.form.properties.layout;

import game.form.element.FormElement;
import game.form.properties.FormAlignment;
import game.form.properties.FormBounds;

import java.util.List;

public class FormHorizontalLayout implements FormLayout {
    FormAlignment layoutAlignment;

    public FormHorizontalLayout() {
        this.layoutAlignment = FormAlignment.START;
    }

    public FormHorizontalLayout(FormAlignment layoutAlignment) {
        this.layoutAlignment = layoutAlignment;
    }

    @Override
    public void onLayout(FormElement parent, List<FormElement> children) {
        int totalChildrenWidth = 0;
        for (FormElement child : children) {
            int childWidth = (int) child.getBounds().getWidth();
            int childPaddingLeft = (int) child.getPadding().getLeft();
            int childPaddingRight = (int) child.getPadding().getRight();
            totalChildrenWidth += childWidth + childPaddingLeft + childPaddingRight;
        }

        int parentX = (int) parent.getBounds().getX();
        int parentY = (int) parent.getBounds().getY();
        int parentWidth = (int) parent.getBounds().getWidth();
        int parentHeight = (int) parent.getBounds().getHeight();

        int parentMarginLeft = (int) parent.getMargin().getLeft();
        int parentMarginRight = (int) parent.getMargin().getRight();
        int parentMarginTop = (int) parent.getMargin().getTop();
        int parentMarginBottom = (int) parent.getMargin().getBottom();

        int offsetX = switch (layoutAlignment) {
            case START -> parentX + parentMarginLeft;
            case CENTER -> parentX + parentWidth / 2 - totalChildrenWidth / 2;
            case END -> parentX + parentWidth - parentMarginRight - totalChildrenWidth;
        };

        for (FormElement child : children) {
            int childWidth = (int) child.getBounds().getWidth();
            int childHeight = (int) child.getBounds().getHeight();

            int childPaddingTop = (int) child.getPadding().getTop();
            int childPaddingBottom = (int) child.getPadding().getBottom();
            int childPaddingLeft = (int) child.getPadding().getLeft();
            int childPaddingRight = (int) child.getPadding().getRight();

            int childTotalWidth = childWidth + childPaddingLeft + childPaddingRight;
            int childTotalHeight = childHeight + childPaddingTop + childPaddingBottom;

            int offsetY = switch (parent.getElementAlignment()) {
                case START -> childPaddingLeft + (parentY + parentMarginTop + childPaddingTop);
                case CENTER -> childPaddingLeft + (parentY + parentHeight / 2 - childTotalHeight / 2);
                case END -> childPaddingLeft + (parentY + parentHeight - parentMarginBottom - childTotalHeight);
            };

            FormBounds bounds = new FormBounds(offsetX, offsetY, childWidth, childHeight);
            child.setBounds(bounds);

            offsetX += childTotalWidth + childPaddingRight;
        }

    }
}
