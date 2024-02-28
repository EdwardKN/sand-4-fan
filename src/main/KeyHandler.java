package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Dictionary;
import java.util.Hashtable;

public class KeyHandler implements KeyListener {

    public Dictionary<Integer, Boolean> pressedKeys = new Hashtable<>();

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.put(e.getKeyCode(), true);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.put(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
