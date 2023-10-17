package game.state.battle.controller;

import game.form.element.FormGrid;
import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.hud.HudItems;
import game.state.battle.model.actor.Actor;

public class SelectItemPlayerController extends PlayerController
{
    Actor selectedActor;
    HudItems hudItems;

    public SelectItemPlayerController(BattleState battleState, Actor actor) {
        super(battleState);
        this.selectedActor = actor;

        hudItems = new HudItems();
        hudItems.setVisible(true);
    }

    @Override
    public void onEnter() {
        on(Keyboard.keyPressed).run(hudItems::onKeyPressed);
        on(getBattleState().getOnGuiRender()).run(hudItems::onRender);
    }
}
