package game;

import java.time.Duration;
import java.time.Instant;

public class DebugTimer {
    Instant start;

    public DebugTimer() {
        start = Instant.now();
    }

    public void start() {
        start = Instant.now();
    }

    public void clock(String source) {
        Instant now = Instant.now();
        Duration elapsed = Duration.between(start, now);
        float millis = elapsed.toNanos() / 1e6f;
        System.out.println(source + " took " + millis + "ms");
        start = now;
    }
}
