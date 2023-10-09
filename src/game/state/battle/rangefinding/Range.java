package game.state.battle.rangefinding;

import game.state.battle.world.World;
import game.util.Camera;

public interface Range {
    void onUpdate(World world);

    void onRender(Camera camera);
}
