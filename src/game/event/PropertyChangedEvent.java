package game.event;

public class PropertyChangedEvent<T> extends Event {
    private final T oldValue;
    private final T newValue;

    public PropertyChangedEvent(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
