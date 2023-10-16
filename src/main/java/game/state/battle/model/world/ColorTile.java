package game.state.battle.model.world;

import java.awt.*;

public class ColorTile extends Tile {
    private final Color color;
    private final int size;

    public ColorTile(int x, int y, boolean passable, Color color) {
        super(x, y, passable);
        this.color = color;
        size = 32;
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(color);
        Composite oldComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
        graphics.fillRect(getX() * size, getY() * size, size, size);
        graphics.setComposite(oldComposite);
        graphics.setColor(color.darker().darker());
        graphics.drawRect(getX() * size, getY() * size, size, size);

    }
}
