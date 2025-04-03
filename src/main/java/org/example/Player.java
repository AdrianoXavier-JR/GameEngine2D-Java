package org.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Player {
    private int x, y;
    private int speed = 5;
    private boolean left, right, up, down;
    private BufferedImage sprite;
    private final int WIDTH = 40, HEIGHT = 40;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        loadSprite();
    }

    private void loadSprite() {
        try {
            BufferedImage original = ImageIO.read(new File("res/player.png"));
            sprite = resizeImage(original, WIDTH, HEIGHT); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resizeImage(BufferedImage original, int width, int height) {
        Image temp = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public void update(int screenWidth, int screenHeight) {
        if (left && x > 0) x -= speed;
        if (right && x < screenWidth - WIDTH) x += speed;
        if (up && y > 0) y -= speed;
        if (down && y < screenHeight - HEIGHT) y += speed;
    }

    public void draw(Graphics g) {
        g.drawImage(sprite, x, y, null);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
        if (e.getKeyCode() == KeyEvent.VK_UP) up = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) down = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) up = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) down = false;
    }
}
