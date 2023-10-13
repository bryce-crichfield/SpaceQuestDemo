package game.state;

import game.Game;
import game.util.Util;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

import java.awt.*;
import java.time.Duration;

public class Sandbox extends GameState {
    static final VectorSpecies<Float> species = FloatVector.SPECIES_PREFERRED;

    final float[] posX;
    final float[] posY;
    final float[] velX;
    final float[] velY;

    final int particleCount = 1000;

    FloatVector posXVec;
    FloatVector posYVec;
    FloatVector velXVec;
    FloatVector velYVec;

    public Sandbox(Game game) {
        super(game);

        posX = new float[particleCount];
        posY = new float[particleCount];
        velX = new float[particleCount];
        velY = new float[particleCount];

        for (int i = 0; i < particleCount; i++) {
            posX[i] = (float) (Math.random() * Game.SCREEN_WIDTH);
            posY[i] = (float) (Math.random() * Game.SCREEN_HEIGHT);
            velX[i] = (float) (Math.random() * 100 - 50);
            velY[i] = (float) (Math.random() * 100 - 50);
        }

        posXVec = FloatVector.fromArray(species, posX, 0);
        posYVec = FloatVector.fromArray(species, posY, 0);
        velXVec = FloatVector.fromArray(species, velX, 0);
        velYVec = FloatVector.fromArray(species, velY, 0);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onUpdate(Duration delta) {
        float dt = Util.perSecond(delta);
        posXVec = posXVec.add(velXVec.mul(dt));
        posYVec = posYVec.add(velYVec.mul(dt));
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < particleCount; i++) {
            graphics.fillRect((int) posX[i], (int) posY[i], 1, 1);
        }
    }
}
