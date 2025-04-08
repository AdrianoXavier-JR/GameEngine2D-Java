package org.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private int x, y;
    private int speed = 5;
    private boolean left, right;
    private boolean jumping = false;
    private int jumpPower = 20;
    private int gravity = 1;
    private int yVelocity = 0;
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

    public void update(int screenWidth, int screenHeight, ArrayList<Rectangle> platforms) {
        if (left && x > 0) x -= speed;
        if (right && x < screenWidth - WIDTH) x += speed;

        yVelocity += gravity;
        y += yVelocity;

        boolean onGround = false;
        Rectangle playerRect = new Rectangle(x, y, WIDTH, HEIGHT);

        for (Rectangle platform : platforms) {
            if (playerRect.intersects(platform)) {
                Rectangle intersection = playerRect.intersection(platform);
                if (intersection.height < HEIGHT && yVelocity >= 0 && y + HEIGHT - intersection.height <= platform.y) {
                    y = platform.y - HEIGHT;
                    yVelocity = 0;
                    jumping = false;
                    onGround = true;
                }
            }
        }

        if (!onGround) jumping = true;
    }

    public void draw(Graphics g) {
        g.drawImage(sprite, x, y, null);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !jumping) {
            jumping = true;
            yVelocity = -jumpPower;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public void respawn(int x, int y) {
        this.x = x;
        this.y = y;
        this.yVelocity = 0;
        this.jumping = false;
    }

    public boolean isFalling() {
        return yVelocity > 0;
    }
}
