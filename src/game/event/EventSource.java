package game.event;

public interface EventSource {
    EventEmitter getEmitter();

    default void addListener(EventListener listener) {
        getEmitter().addListener(listener);
    }

    default void removeListener(EventListener listener) {
        getEmitter().removeListener(listener);
    }

    default void fireEvent(Event event) {
        getEmitter().fireEvent(event);
    }
}
