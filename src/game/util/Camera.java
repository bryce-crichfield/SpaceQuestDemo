package game.util;

import game.Game;

import java.awt.geom.AffineTransform;

public class Camera {
    Game game;
    private float x;
    private float y;
    private float zoom;

    public Camera(Game game) {
        this.game = game;
        x = 0;
        y = 0;
        zoom = 1;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public AffineTransform getTransform() {
        AffineTransform transform = new AffineTransform();
        transform.scale(zoom, zoom);

        // center the camera on the screen
        float screenWidth = game.SCREEN_WIDTH / zoom;
        float screenHeight = game.SCREEN_HEIGHT / zoom;
        int cameraX = (int) (x - (screenWidth / 2));
        int cameraY = (int) (y - (screenHeight / 2));
        transform.translate(-cameraX, -cameraY);
        return transform;
    }
}
