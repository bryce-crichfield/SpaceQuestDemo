package game.event;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionManager {
    public class SubscriptionBuilderRunClause<T> {
        Event <T> event;

        public SubscriptionBuilderRunClause(Event<T> event) {
            this.event = event;
        }

        public void run(EventListener<T> listener) {
            event.listenWith(listener);
            subscriptions.add(() -> event.remove(listener));
        }
    }


    private final List<Runnable> subscriptions;

    public SubscriptionManager() {
        this.subscriptions = new ArrayList<>();
    }

    public <T> SubscriptionBuilderRunClause<T> on(Event<T> event) {
        return new SubscriptionBuilderRunClause<>(event);
    }

    public void unsubscribeAll() {
        for (Runnable subscription : subscriptions) {
            subscription.run();
        }
        subscriptions.clear();
    }
}
