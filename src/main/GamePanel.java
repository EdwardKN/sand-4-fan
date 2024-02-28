package main;

import element.World;
import element.Chunk;

import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    Thread gameThread;
    static final int FPS = 60;
    int currentFPS = 0;

    static final boolean TRUEFPS = false;

    static final int STANDARDX = 16;
    static final int STANDARDY = 9;
    static final int RENDERSCALE = 15;

    static final int PIXELSIZE = 4;

    World world;

    public GamePanel() {
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (TRUEFPS || delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer > 1000000000) {
                currentFPS = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {

    }

    public void initGame(JFrame frame) {
        world = new World();
        world.initializeWorld();

        frame.setSize(RENDERSCALE * STANDARDX * PIXELSIZE, RENDERSCALE * STANDARDY * PIXELSIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawChunks(g2d);

        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.setColor(Color.BLACK);

        g2d.drawString("FPS: " + currentFPS, 50, 30);

        g2d.dispose();
    }

    public void drawChunks(Graphics2D g2d) {

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                Chunk chunk = world.chunks.get(x + "," + y);

                if (chunk != null) {
                    if (chunk.hasUpdatedSinceImageBufferChange) {
                        chunk.updateImageBuffer();
                    }
                    g2d.drawImage(chunk.imageBuffer, x * 32 * PIXELSIZE, y * 32 * PIXELSIZE, 32 * PIXELSIZE, 32 * PIXELSIZE, null);
                }
            }
        }
    }
}