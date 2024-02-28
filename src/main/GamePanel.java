package main;

import world.World;
import world.Chunk;
import entity.Player;
import world.elementTypes.Air;
import world.elementTypes.MovableSolid;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

import static world.Chunk.*;
import static world.World.createNewChunk;
import static world.World.getChunkCoord;


public class GamePanel extends JPanel implements Runnable {

    static final int STANDARDX = 16;
    static final int STANDARDY = 9;
    static final int RENDERSCALE = 15;

    int screenWidth = STANDARDX * RENDERSCALE;
    int screenHeight = STANDARDY * RENDERSCALE;
    int realWidth = 0;
    int realHeight = 0;
    double scale = 0;

    BufferedImage tempScreen;
    Graphics2D g2;


    static final int FPS = 60;
    int currentFPS = 0;

    static final boolean TRUEFPS = false;

    KeyHandler keyH = new KeyHandler();
    MouseHandler mouse = new MouseHandler();
    Thread gameThread;

    World world;

    Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(1280, 720));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.addMouseListener(mouse);
        this.setFocusable(true);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                screenWidth = c.getWidth();
                screenHeight = c.getHeight();
                calculateRealSize();
            }
        });
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
                drawToTempScreen();
                drawToScreen();
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
        player.update();
    }

    public void initGame() {
        world = new World();
        world.initializeWorld();

        calculateRealSize();
        tempScreen = new BufferedImage(STANDARDX * RENDERSCALE, STANDARDY * RENDERSCALE, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
        player.setDefaultValues();
    }

    public void calculateRealSize() {
        if (screenWidth * STANDARDY > screenHeight * STANDARDX) {
            realWidth = screenHeight * STANDARDX / STANDARDY;
            realHeight = screenHeight;
            scale = (double) realWidth / (RENDERSCALE * STANDARDX);
        } else {
            realWidth = screenWidth;
            realHeight = screenWidth * STANDARDY / STANDARDX;
            scale = (double) realHeight / (RENDERSCALE * STANDARDY);
        }
    }

    public void drawToTempScreen() {
        drawChunks(g2);
        drawCursor(g2);

        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.setColor(Color.BLACK);

        g2.drawString("FPS: " + currentFPS, 20, 20);
    }

    public void drawToScreen() {
        Graphics g = getGraphics();

        ;
        g.drawImage(tempScreen, (screenWidth - realWidth) / 2, (screenHeight - realHeight) / 2, realWidth, realHeight, null);
        g.dispose();
    }

    public void drawChunks(Graphics2D g2d) {
        int maxX = STANDARDX * RENDERSCALE / CHUNKSIZE + 2;
        int maxY = STANDARDY * RENDERSCALE / CHUNKSIZE + 2;

        for (int x = -1; x < maxX; x++) {
            for (int y = -1; y < maxY; y++) {
                int chunkX = x + player.x / CHUNKSIZE;
                int chunkY = y + player.y / CHUNKSIZE;
                Chunk chunk = world.chunks.get(chunkX + "," + chunkY);

                if (chunk != null) {
                    if (chunk.hasUpdatedSinceImageBufferChange) {
                        chunk.updateImageBuffer();
                    }
                    g2d.drawImage(chunk.imageBuffer, -(player.x % CHUNKSIZE) + x * CHUNKSIZE, -(player.y % CHUNKSIZE) + y * CHUNKSIZE, CHUNKSIZE, CHUNKSIZE, null);
                } else {
                    createNewChunk(world, chunkX, chunkY);
                }
            }
        }
    }

    public void drawCursor(Graphics2D g2d) {
        Point mousePos = MouseInfo.getPointerInfo().getLocation();

        mousePos.x -= (screenWidth - realWidth) / 2;
        mousePos.y -= (screenHeight - realHeight) / 2;

        mousePos.x = (int) clamp(mousePos.x, 0, (float) realWidth);
        mousePos.y = (int) clamp(mousePos.y, 0, (float) realHeight);

        int frameMouseX = (int) (mousePos.x / scale);
        int frameMouseY = (int) (mousePos.y / scale);

        g2d.drawRect(frameMouseX - 5, frameMouseY - 5, 10, 10);

        if (mouse.down) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    int realX = frameMouseX + x + player.x;
                    int realY = frameMouseY + y + player.y;

                    int chunkX = getChunkCoord(realX);
                    int chunkY = getChunkCoord(realY);

                    int elementX = relativeElementCoordinate(realX);
                    int elementY = relativeElementCoordinate(realY);

                    Chunk chunk = world.chunks.get(chunkX + "," + chunkY);

                    if (chunk != null) {

                        if (chunk.elements[elementCoordinate(elementX, elementY)] instanceof Air) {
                            chunk.elements[elementCoordinate(elementX, elementY)] = new MovableSolid(realX, realY, new int[]{255, 120, 0, 0}, chunk);
                            chunk.hasUpdatedSinceImageBufferChange = true;
                        }
                        ;
                    }
                }
            }
        }

    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}