package game.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Event<T> {
    private final List<EventListener<T>> listeners;

    public Event() {
        listeners = new ArrayList<>();
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

    public void fire(T event) {
        for (EventListener<T> listener : listeners) {
            listener.onEvent(event);
        }
    }

    public void clear() {
        listeners.clear();
    }

    public <R> Event<R> map(Function<T, R> mapper) {
        Event<R> result = new Event<>();
        this.listenWith((event) -> result.fire(mapper.apply(event)));
        return result;
    }

    public <T> Event<T> filter(Predicate<T> filter) {
        Event<T> result = new Event<>();
        this.listenWith((event) -> {
            if (filter.test((T) event)) {
                result.fire((T) event);
            }
        });
        return result;
    }

}
