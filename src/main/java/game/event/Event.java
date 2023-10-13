package game.event;

import game.form.properties.FormBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Event<T> {
    private final List<EventListener<T>> listeners;

    public Event() {
        listeners = new ArrayList<>();
    }

    public void respondBy(EventListener<T> listener) {
        listenWith(listener);
    }

    public void listenWith(EventListener<T> listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    public void remove(EventListener<T> listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public void triggerBy(Event<T> other) {
        other.listenWith(this::fire);
    }

    public void fire(T event) {
        for (EventListener<T> listener : listeners) {
            listener.onEvent(event);
        }
    }

    public <R> Event<R> map(Function<T, R> mapper) {
        Event<R> mapped = new Event<>();
        listenWith(event -> mapped.fire(mapper.apply(event)));
        return mapped;
    }
}
