package entity;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;

    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
    }

    public void update() {
        if (keyH.pressedKeys.isEmpty()) {
            return;
        }
        if (keyH.pressedKeys.get(68) != null && keyH.pressedKeys.get(68)) {
            x += speed;
        }
        if (keyH.pressedKeys.get(65) != null && keyH.pressedKeys.get(65)) {
            x -= speed;
        }
        if (keyH.pressedKeys.get(83) != null && keyH.pressedKeys.get(83)) {
            y += speed;
        }
        if (keyH.pressedKeys.get(87) != null && keyH.pressedKeys.get(87)) {
            y -= speed;
        }
    }
}
