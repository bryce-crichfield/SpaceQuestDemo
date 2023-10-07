package game.event;

import java.util.ArrayList;
import java.util.List;

public class EventContext {
    private final List<EventListener> listeners;

    public EventContext() {
        listeners = new ArrayList<>();
    }

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void fireEvent(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
            if (event.isHandled())
                break;
        }
    }
}
