package main;

import world.World;
import world.Chunk;
import entity.Player;
import world.elementTypes.Air;
import world.elementTypes.Liquid;
import world.elementTypes.MovableSolid;
import world.elementTypes.Solid;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

import static java.lang.Math.clamp;
import static main.Utils.*;
import static world.Chunk.*;


public class GamePanel extends JPanel implements Runnable {

    public static final int STANDARDX = 16;
    public static final int STANDARDY = 9;
    public static final int RENDERSCALE = 15;

    final int maxX = STANDARDX * RENDERSCALE / CHUNKSIZE + 2;
    final int maxY = STANDARDY * RENDERSCALE / CHUNKSIZE + 2;

    int screenWidth = 1280;
    int screenHeight = 720;
    int realWidth = 0;
    int realHeight = 0;
    double scale = 0;

    BufferedImage tempScreen;
    Graphics2D g2;


    static final int FPS = 60;
    int currentFPS = 0;
    double updateTime = 0;

    final boolean TRUEFPS = false;

    KeyHandler keyH = new KeyHandler();
    MouseHandler mouse = new MouseHandler();
    MouseMoveHandler mousePos = new MouseMoveHandler();

    Thread gameThread;

    World world;

    Player player = new Player(this, keyH);

    public GamePanel() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                screenWidth = c.getWidth();
                screenHeight = c.getHeight();
                calculateRealSize();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouse.x = e.getX();
                mouse.y = e.getY();
                System.out.println("he");
            }
        });
        this.setPreferredSize(new Dimension(1280, 720));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mousePos);
        this.setFocusable(true);
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
        double tmpUpdateTime = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (TRUEFPS || delta >= 1) {
                update();
                drawToTempScreen();
                repaint();
                delta--;
                drawCount++;

                tmpUpdateTime = System.nanoTime() - currentTime;
            }
            if (timer > 1000000000) {
                currentFPS = drawCount;
                updateTime = tmpUpdateTime / 1000000;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        world.updateParticles();
        world.updateChunks();
        player.update();
    }

    public void initGame() {
        world = new World(player);

        tempScreen = new BufferedImage(STANDARDX * RENDERSCALE, STANDARDY * RENDERSCALE, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
        player.setDefaultValues();
        calculateRealSize();
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

        g2.drawString(currentFPS + "FPS", 20, 20);
        g2.drawString(updateTime + "ms", 20, 40);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tempScreen, (screenWidth - realWidth) / 2, (screenHeight - realHeight) / 2, realWidth, realHeight, null);
        g.dispose();
    }

    public void drawChunks(Graphics2D g2d) {
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
                    world.createNewChunk(chunkX, chunkY);
                }
            }
        }
    }

    public void drawCursor(Graphics2D g2d) {
        Point realMousePos = new Point();
        realMousePos.x = mousePos.x;
        realMousePos.y = mousePos.y;

        realMousePos.x -= (screenWidth - realWidth) / 2;
        realMousePos.y -= (screenHeight - realHeight) / 2;

        realMousePos.x = (int) clamp(realMousePos.x, 0, (float) realWidth);
        realMousePos.y = (int) clamp(realMousePos.y, 0, (float) realHeight);

        int frameMouseX = (int) (realMousePos.x / scale);
        int frameMouseY = (int) (realMousePos.y / scale);

        g2d.drawRect(frameMouseX - 5, frameMouseY - 5, 10, 10);

        if (mouse.down) {
            mouse.down = false;
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    int realX = frameMouseX + x + player.x - 5;
                    int realY = frameMouseY + y + player.y - 5;

                    int chunkX = getChunkCoord(realX);
                    int chunkY = getChunkCoord(realY);

                    int elementX = relativeElementCoordinate(realX);
                    int elementY = relativeElementCoordinate(realY);

                    Chunk chunk = world.chunks.get(chunkX + "," + chunkY);

                    if (chunk != null) {

                        if (chunk.elements[elementCoordinate(elementX, elementY)] instanceof Air) {
                            chunk.elements[elementCoordinate(elementX, elementY)] = new MovableSolid(realX, realY, new int[]{255, 120, 50, 0}, chunk);
                            chunk.hasUpdatedSinceImageBufferChange = true;
                            chunk.shouldStepNextFrame = true;
                        } else {
                            //chunk.elements[elementCoordinate(elementX, elementY)].convertToParticle(0, -5);
                        }
                    }
                }
            }
        }

    }


}