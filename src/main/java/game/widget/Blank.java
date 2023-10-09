package game.widget;

import game.Game;

public class Blank extends Widget {
    public Blank(Game game) {
        super("", game);
        hoverable = false;
    }
}
