package game.event;

public abstract class Event {
    private boolean handled;

    public Event() {
        handled = false;
    }

    public void setHandled() {
        handled = true;
    }

    public boolean isHandled() {
        return handled;
    }
}
