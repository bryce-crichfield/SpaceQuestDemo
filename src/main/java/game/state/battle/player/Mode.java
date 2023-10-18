package game.state.battle.player;

import game.event.SubscriptionManager;
import game.state.battle.model.World;

import java.awt.*;
import java.time.Duration;

public abstract class Mode extends SubscriptionManager {
    protected final World world;

    protected Mode(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }


    public abstract void onKeyPressed(int keyCode);
    public abstract void onKeyReleased(int keyCode);
    public abstract void onEnter();

    public abstract void onUpdate(Duration delta);

    public abstract void onGuiRender(Graphics2D graphics);

    public abstract void onWorldRender(Graphics2D graphics);

    public abstract void onExit();

    public abstract boolean isDone();
}
