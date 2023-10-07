package game;

import java.time.Duration;
import java.util.Random;

public class Util {

    public static float perSecond(Duration delta) {
        return delta.toNanos() / 1e9f;
    }

    public static int wrap(int value, int lower, int upper) {
        return Math.floorMod(value - lower, upper - lower) + lower;
    }

    public static int clamp(int value, int lower, int upper) {
        return Math.max(lower, Math.min(value, upper));
    }

    public static int random(int lower, int upper) {
        return (int) (Math.random() * (upper - lower)) + lower;
    }

    public static float random(float min, float max, float skew, float bias) {
        float range = max - min;
        float mid = min + range / 2.0f;
        float unitGaussian = (float) new Random().nextGaussian();
        float biasFactor = (float) Math.exp(bias);
        float retval = (float) (mid + (range * (biasFactor / (biasFactor + Math.exp(-unitGaussian / skew)) - 0.5)));
        return retval;
    }

    public static float map(float size, float lower1, float upper1, float lower2, float upper2) {
        return lower2 + (size - lower1) * (upper2 - lower2) / (upper1 - lower1);
    }

    public static float random(float lower, float upper) {
        return (float) (Math.random() * (upper - lower)) + lower;
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static float easeIn(float start, float end, float duration, float elapsed) {
        float t = elapsed / duration;
        return start + (end - start) * t * t;
    }
}
