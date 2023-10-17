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

        int offsetY = switch (layoutAlignment) {
            case START -> parentY + parentMarginTop;
            case CENTER -> parentY + parentHeight / 2 - totalChildrenHeight / 2;
            case END -> parentY + parentHeight - parentMarginBottom - totalChildrenHeight;
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

            int offsetX = switch (parent.getElementAlignment()) {
                case START -> parentX + parentMarginLeft + childPaddingLeft + childPaddingTop;
                case CENTER -> parentX + parentWidth / 2 - childTotalWidth / 2 + childPaddingLeft + childPaddingTop;
                case END -> parentX + parentWidth - parentMarginRight - childTotalWidth + childPaddingLeft + childPaddingTop;
            };

            FormBounds bounds = new FormBounds(offsetX, offsetY, childWidth, childHeight);
            offsetY += childTotalHeight + childPaddingBottom;

            child.setBounds(bounds);
        }
    }
}
