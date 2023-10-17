package game.form.element;

import game.form.properties.FormBorder;
import game.form.properties.layout.FormGridLayout;
import game.io.Keyboard;
import game.util.Util;

import java.text.Normalizer;

public class FormGrid extends FormElement {
    int cursorX;
    int cursorY;
    int rows;
    int cols;

    public FormGrid(int x, int y, int width,int height, int rows, int cols) {
        super(x, y, width, height);
        this.setLayout(new FormGridLayout(rows, cols));
        this.rows = rows;
        this.cols = cols;
    }

    public void onKeyPressed(int keyCode) {
        boolean hasCurrentElement = cursorX + cursorY * cols < this.getChildren().size();
        if (hasCurrentElement) {
            FormElement currentElement = this.getChildren().get(cursorX + cursorY * cols);
            currentElement.setBorder(null);
        }

        switch (keyCode) {
            case Keyboard.UP -> cursorY--;
            case Keyboard.DOWN -> cursorY++;
            case Keyboard.LEFT -> cursorX--;
            case Keyboard.RIGHT -> cursorX++;
            default -> {}
        }

        if (cursorX >= cols) {
            cursorY++;
            cursorX = 0;
        }

        if (cursorX < 0) {
            cursorY--;
            cursorX = cols - 1;
        }

        if (cursorY >= rows) {
            cursorY = 0;
        }

        if (cursorY < 0) {
            cursorY = rows - 1;
        }

        hasCurrentElement = cursorX + cursorY * cols < this.getChildren().size();
        if (hasCurrentElement) {
            FormElement currentElement = this.getChildren().get(cursorX + cursorY * cols);
            currentElement.setBorder(new FormBorder());

            if (keyCode == Keyboard.PRIMARY) {
                currentElement.getOnPrimary().fire(null);
            }
        }
    }


}
