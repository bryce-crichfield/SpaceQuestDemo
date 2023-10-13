package game.state.battle.hud;

import game.event.Event;
import game.state.battle.model.Actor;

import java.awt.*;

public class Hud {
    HudStats primary;
    HudStats secondary;
    HudActions actions;

    public Hud(Event<Actor> onActorChanged) {
        primary = new HudStats(0, 0, 25, 25, onActorChanged);
        primary.setVisible(false);

        secondary = new HudStats(25, 25, 25, 25, onActorChanged);
        secondary.setVisible(false);

        actions = new HudActions(55, 55, 25, 25, onActorChanged);
        actions.setVisible(false);
    }

    public HudStats getPrimary() {
        return primary;
    }

    public HudStats getSecondary() {
        return secondary;
    }

    public HudActions getActions() {
        return actions;
    }

    public void onRender(Graphics2D graphics) {
        primary.onRender(graphics);
        secondary.onRender(graphics);
        actions.onRender(graphics);
    }
}
