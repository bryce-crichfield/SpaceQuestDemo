package game.form.element;

import game.event.Event;

import java.util.function.Function;

public class FormProperty<T> {
    private final Event<T> onChange = new Event<>();
    private T value;

    public FormProperty(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void bind(FormProperty<T> other) {
        other.onChange().listenWith(this::set);
    }

    public void set(T value) {
        this.value = value;
        onChange.fire(value);
    }

    public Event<T> onChange() {
        return onChange;
    }

    public void set(Function<T, T> function) {
        this.set(function.apply(value));
    }

}
