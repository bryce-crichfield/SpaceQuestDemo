package game.state.battle.model;

import game.state.battle.event.ActorAnimated;
import game.util.Util;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ActorAnimation {
    private final Actor actor;
    boolean animationEnabled = false;
    float animationPeriod = 0.35f;
    float animationX = 0;
    float animationY = 0;
    float animationTargetX = 0;
    float animationTargetY = 0;
    float animationAccumulator = 0;
    Queue<Tile> animationPath = new LinkedList<>();

    public ActorAnimation(Actor actor) {
        this.actor = actor;

        animationX = actor.getX();
        animationY = actor.getY();
    }

    public void start(List<Tile> path) {
        animationAccumulator = 0;

        animationX = actor.getX();
        animationY = actor.getY();

        animationPath = new LinkedList<>(path);

        if (animationPath.isEmpty()) {
            animationTargetX = animationX;
            animationTargetY = animationY;
            return;
        }

        Tile target = animationPath.poll();
        animationTargetX = target.getX();
        animationTargetY = target.getY();

        animationEnabled = true;
    }

    public void onUpdate(Duration delta) {
        if (!animationEnabled) {
            return;
        }


        float dt = Util.perSecond(delta);
        animationAccumulator += dt;

        if (animationAccumulator >= animationPeriod) {
            animationAccumulator = 0;

            if (!animationPath.isEmpty()) {
                Tile nextTile = animationPath.poll();
                animationTargetX = nextTile.getX();
                animationTargetY = nextTile.getY();
            }
        }

        animationX = Util.easeIn(animationTargetX, animationX, animationPeriod, animationAccumulator);
        animationY = Util.easeIn(animationTargetY, animationY, animationPeriod, animationAccumulator);

        if (Math.abs(animationTargetX - animationX) < 0.1) {
            animationX = animationTargetX;
        }

        if (Math.abs(animationTargetY - animationY) < 0.1) {
            animationY = animationTargetY;
        }

        ActorAnimated.event.fire(actor);
    }

    public float getX() {
        return animationX;
    }

    public float getY() {
        return animationY;
    }
}
