package game.event;

public interface EventListener<T> {
    void onEvent(T event);
    default void listenTo(Event<T> event) {
        event.listenWith(this);
    }
}
