package game.event;

import java.util.Queue;
import java.util.function.Consumer;

public class LazyEvent<T> extends Event<T> {
    private final Queue<T> queue;

    public LazyEvent() {
        super();
        this.queue = new java.util.concurrent.ConcurrentLinkedQueue<>();
    }

    @Override
    public void fire(T event) {
        queue.add(event);
    }

    public void flush() {
        while (!queue.isEmpty()) {
            super.fire(queue.remove());
        }
    }

    public void flush(Consumer<T> consumer) {
        while (!queue.isEmpty()) {
            consumer.accept(queue.remove());
        }
    }
}
