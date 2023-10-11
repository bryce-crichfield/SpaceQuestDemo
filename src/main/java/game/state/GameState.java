package game.state;

import game.Game;
import game.event.Event;
import game.event.LazyEvent;
import game.event.SubscriptionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.time.Duration;

@Getter
@RequiredArgsConstructor
public abstract class GameState {
    private final Game game;
    private final Event<Graphics2D> onWorldRender = new Event<>();
    private final Event<Graphics2D> onGuiRender = new Event<>();
    private final SubscriptionManager subscriptions = new SubscriptionManager();

    public abstract void onEnter();
    public abstract void onUpdate(Duration delta);
    public abstract void onRender(Graphics2D graphics);

    public void onExit() {
        getOnGuiRender().clear();
        getOnWorldRender().clear();
        getSubscriptions().unsubscribeAll();
    }
}
