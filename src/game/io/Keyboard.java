package game.io;

import game.event.Event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    public static final int UP = KeyEvent.VK_W;
    public static final int DOWN = KeyEvent.VK_S;
    public static final int LEFT = KeyEvent.VK_A;
    public static final int RIGHT = KeyEvent.VK_D;
    public static final int PRIMARY = KeyEvent.VK_SPACE;
    public static final int SECONDARY = KeyEvent.VK_SHIFT;
    public static final Event<Integer> onKeyPressed = new Event<>();
    public static final Event<Integer> onKeyReleased = new Event<>();
    public static final Event<Integer> onKeyTyped = new Event<>();
    private final boolean[] oldKeys;
    private final boolean[] newKeys;

    public Keyboard() {
        oldKeys = new boolean[256];
        newKeys = new boolean[256];

        for (int i = 0; i < 256; i++) {
            oldKeys[i] = false;
            newKeys[i] = false;
        }
    }

    public boolean anyPressed(int... keyCodes) {
        for (int keyCode : keyCodes) {
            if (pressed(keyCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean pressed(int keyCode) {
        return newKeys[keyCode] && !oldKeys[keyCode];
    }

    public boolean held(int keyCode) {
        return newKeys[keyCode] && oldKeys[keyCode];
    }

    public boolean down(int keyCode) {
        return newKeys[keyCode] || oldKeys[keyCode];
    }

    public boolean released(int keyCode) {
        return !newKeys[keyCode] && oldKeys[keyCode];
    }

    public void onUpdate() {
        System.arraycopy(newKeys, 0, oldKeys, 0, 256);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        onKeyTyped.fire(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean currentKey = newKeys[e.getKeyCode()];
        newKeys[e.getKeyCode()] = true;
        oldKeys[e.getKeyCode()] = currentKey;
        onKeyPressed.fire(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        boolean currentKey = newKeys[e.getKeyCode()];
        newKeys[e.getKeyCode()] = false;
        oldKeys[e.getKeyCode()] = currentKey;
        onKeyReleased.fire(e.getKeyCode());
    }
}
