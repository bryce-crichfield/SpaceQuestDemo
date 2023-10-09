package game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window extends JFrame {
    private final Game game;
    private final BufferedImage buffer;
    private final Canvas canvas = new Canvas();
    private final BufferStrategy strategy;

    public Window(int width, int height, Game game) {
        this.game = game;
        this.buffer = new BufferedImage(game.SCREEN_WIDTH, game.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        canvas.setSize(width, height);
        canvas.setFocusable(true);
        canvas.requestFocus();
        canvas.addKeyListener(game.getKeyboard());

        this.add(canvas);
        this.pack();

        canvas.createBufferStrategy(4);
        strategy = canvas.getBufferStrategy();
    }

    public void onRender(float updateTime, float renderTime) {
        this.repaint();

        Graphics2D g = (Graphics2D) buffer.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        game.onRender(g);

        Graphics2D g2 = (Graphics2D) strategy.getDrawGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        float canvasAspectRatio = (float) canvas.getWidth() / (float) canvas.getHeight();
        float bufferAspectRatio = (float) buffer.getWidth() / (float) buffer.getHeight();

        int displayWidth = 0;
        int displayHeight = 0;

        if (bufferAspectRatio > canvasAspectRatio) {
            displayWidth = canvas.getWidth();
            displayHeight = (int) (canvas.getWidth() / bufferAspectRatio);
        } else {
            displayHeight = canvas.getHeight();
            displayWidth = (int) (canvas.getHeight() * bufferAspectRatio);
        }

        int centerX = (canvas.getWidth() - displayWidth) / 2;
        int centerY = (canvas.getHeight() - displayHeight) / 2;
        g2.drawImage(buffer, centerX, centerY, displayWidth, displayHeight, null);
        drawDebugStats(g2, updateTime, renderTime);
        g2.dispose();

        strategy.show();
    }

    private void drawDebugStats(Graphics2D graphics, float updateTime, float renderTime) {
        graphics.setColor(Color.WHITE);

        String upsString = String.format("%.1f ups", 1f / updateTime);
        String fpsString = String.format("%.1f fps", 1f / renderTime);

        String updateTimeString = String.format("%.1f ms", updateTime * 1000);
        String renderTimeString = String.format("%.1f ms", renderTime * 1000);

        final int textSize = 12;
        int y = 20;

        Font font = graphics.getFont();
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font(font.getName(), Font.PLAIN, textSize));

        graphics.drawString("UPS: " + upsString, 10, y);
        y += textSize;

        graphics.drawString("FPS: " + fpsString, 10, y);
        y += textSize;

        graphics.drawString("Update Time: " + updateTimeString, 10, y);
        y += textSize;

        graphics.drawString("Render Time: " + renderTimeString, 10, y);
        y += textSize;

        graphics.setFont(font);
    }
}
