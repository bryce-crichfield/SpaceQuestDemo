package game.event;

public interface HasEvent<T> {
    Event<T> getEvent();
}
