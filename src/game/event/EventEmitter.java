package game.event;

import java.util.ArrayList;
import java.util.List;

public class EventEmitter<T extends Event> {
    private final List<EventListener<T>> listeners;

    public EventEmitter() {
        listeners = new ArrayList<>();
    }

    public void addListener(EventListener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(EventListener<T> listener) {
        listeners.remove(listener);
    }

    public void fireEvent(T event) {
        for (EventListener<T> listener : listeners) {
            listener.onEvent(event);
            if (event.isHandled())
                break;
        }
    }
}
