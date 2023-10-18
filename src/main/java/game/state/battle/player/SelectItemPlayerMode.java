package game.state.battle.player;

import game.state.battle.hud.HudItems;
import game.state.battle.model.Actor;
import game.state.battle.model.World;

import java.awt.*;
import java.time.Duration;

public class SelectItemPlayerMode extends PlayerMode
{
    HudItems hudItems;

    public SelectItemPlayerMode(World world, Cursor cursor, Actor actor) {
        super(world, cursor, actor);

        hudItems = new HudItems();
        hudItems.setVisible(true);
    }

    public SelectItemPlayerMode(PlayerMode controller) {
        this(controller.world, controller.cursor, controller.actor.get());
    }

    @Override
    public void onKeyPressed(int keyCode) {
        hudItems.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(int keyCode) {

    }

    @Override
    public void onEnter() {
    }

    @Override
    public void onUpdate(Duration delta) {

    }

    @Override
    public void onGuiRender(Graphics2D graphics) {
        hudItems.onRender(graphics);
    }

    @Override
    public void onWorldRender(Graphics2D graphics) {

    }

    @Override
    public void onExit() {
        unsubscribeAll();
    }
}
