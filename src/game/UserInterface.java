package game;

import java.awt.*;
import java.awt.font.GlyphVector;

public class UserInterface {
    public static Color background = new Color(0, 0, 18);
    public static Color highlight = new Color(196, 149, 0);
    public final int screenWidth;
    public final int screenHeight;
    public final int tileSize;
    private final Graphics2D graphics;
    public Color textColor = Color.WHITE;
    public int textSize = 12;

    public UserInterface(Graphics2D graphics, int screenWidth, int screenHeight, int tileSize) {
        this.graphics = graphics;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.tileSize = tileSize;

    }


    public void drawTextCentered(String value, int x, int y, int width, int size) {
        Shape shape = textToShape(value, x, y, size);
        Rectangle bounds = shape.getBounds();

        int textWidth = bounds.width;
        int textHeight = bounds.height;

        int textX = (x + width / 2) - (textWidth / 2);

        drawText(value, textX, y, size);
    }

    public void drawTextRightJustified(String value, int x, int y, int width, int size, int padding) {
        Shape shape = textToShape(value, x, y, size);
        Rectangle bounds = shape.getBounds();

        int textWidth = bounds.width;
        int textHeight = bounds.height;

        int textX = x + padding;

        drawText(value, textX, y, size);
    }

    public void drawTextLeftJustified(String value, int x, int y, int width, int size, int padding) {
        Shape shape = textToShape(value, x, y, size);
        Rectangle bounds = shape.getBounds();

        int textWidth = bounds.width;
        int textHeight = bounds.height;

        int textX = x + width - textWidth - padding;

        drawText(value, textX, y, size);
    }


    public Shape textToShape(String value, int x, int y, int size) {
        Font font = new Font("White Rabbit", Font.PLAIN, textSize);
        GlyphVector glyphVector = font.createGlyphVector(graphics.getFontRenderContext(), value);
        return glyphVector.getOutline(x, y + size);
    }

    public void drawText(String value, int x, int y, int size) {
        Stroke stroke = graphics.getStroke();
        Composite composite = graphics.getComposite();

        Shape shape = textToShape(value, x, y, size);

        graphics.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.BLACK);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphics.draw(shape);
        graphics.setComposite(composite);

        graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setColor(textColor);
        shape = textToShape(value, x - 1, y - 1, size);
        graphics.fill(shape);
        graphics.fill(shape);

        graphics.setStroke(stroke);
    }

    public void drawPanel(int x, int y, int width, int height) {
        int roundness = tileSize;


        // FILLED RECTANGLE
        Composite composite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        graphics.setPaint(new GradientPaint(width / 2f, 0, Color.DARK_GRAY, width / 2f, height, Color.BLACK));
        graphics.fillRoundRect(x, y, width, height, roundness, roundness);
        graphics.setComposite(composite);

        // OUTLINE
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(5));
        graphics.drawRoundRect(x, y, width, height, roundness, roundness);

        // BORDER
        graphics.setColor(Color.WHITE);
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRoundRect(x, y, width, height, roundness, roundness);

        // SHADOW
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(1));
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphics.fillRoundRect(x - 10, y + height + 5, width + 20, 8, roundness, roundness);
        graphics.setComposite(composite);

    }

    public void drawSlider(int x, int y, int width, int height, int percent, Color border) {
        int roundness = 8;

        // FILLED RECTANGLE
        Composite composite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        graphics.setColor(Color.WHITE);
        int sliderWidth = (int) Util.map(percent, 0, 100, 0, width);
        graphics.fillRoundRect(x, y, sliderWidth, height, (int) (roundness * 2f), (int) (roundness * 2f));
        graphics.setComposite(composite);

        // OUTLINE
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(5));
        graphics.drawRoundRect(x, y, width, height, roundness, roundness);

        // BORDER
        graphics.setColor(border);
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRoundRect(x, y, width, height, roundness, roundness);
    }

    public void drawRadioButton(int x, int y, int radius, boolean selected, Color border) {

        // FILLED RECTANGLE
        Composite composite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        if (selected) {
            graphics.setColor(Color.WHITE);
            graphics.fillOval(x, y, radius, radius);
            graphics.setComposite(composite);
        }

        // OUTLINE
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(5));
        graphics.drawOval(x, y, radius, radius);

        // BORDER
        graphics.setColor(border);
        graphics.setStroke(new BasicStroke(3));
        graphics.drawOval(x, y, radius, radius);
    }

    public void setTextColor(Color color) {
        textColor = color;
    }
}
