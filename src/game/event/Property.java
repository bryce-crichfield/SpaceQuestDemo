package game.event;

public class Property<T> implements EventSource {
    private final EventEmitter emitter = new EventEmitter();
    private T value;

    public Property(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        T oldValue = this.value;
        this.value = value;
        fireEvent(new PropertyChangedEvent<>(oldValue, value));
    }

    @Override
    public EventEmitter getEmitter() {
        return emitter;
    }
}
