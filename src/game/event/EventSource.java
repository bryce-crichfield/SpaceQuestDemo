package game.event;

public interface EventSource<T extends Event> {
    EventEmitter<T> getEmitter();

    default void addListener(EventListener<T> listener) {
        getEmitter().addListener(listener);
    }

    default void removeListener(EventListener<T> listener) {
        getEmitter().removeListener(listener);
    }

    default void fireEvent(T event) {
        getEmitter().fireEvent(event);
    }
}
