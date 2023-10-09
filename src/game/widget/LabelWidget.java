package game.widget;

import game.Game;

import java.awt.*;

public class LabelWidget extends Widget {
    private String label = "";

    public LabelWidget(String text, Game game) {
        super(text, game);
    }

    public LabelWidget(String text, String label, Game game) {
        super(text, game);
        this.label = label;
    }

    @Override
    public void onRender(UserInterface ui, int menuX, int y, int menuWidth, boolean hovered) {
        super.onRender(ui, menuX, y, menuWidth, hovered);

        int sliderX = (int) (menuX + (menuWidth / 2f));
        int sliderY = y + 16;
        int sliderWidth = (int) ((menuWidth / 2f) - game.TILE_SIZE);
        int sliderHeight = ui.tileSize / 2;
        Color border = hovered ? UserInterface.highlight : Color.WHITE;
        ui.drawTextRightJustified(label, sliderX, sliderY, menuWidth, sliderHeight, 32);
    }
}
