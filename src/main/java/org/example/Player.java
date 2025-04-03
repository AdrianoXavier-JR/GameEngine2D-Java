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
    private boolean left, right;
    private boolean jumping = false;
    private int jumpPower = 15; // Aumentei o poder do pulo para melhor resposta
    private int gravity = 1;
    private int velocityY = 0;
    private BufferedImage sprite;
    private final int WIDTH = 40, HEIGHT = 40;
    private final int FLOOR_Y = 760; // Chão fixo

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

        // Aplicando gravidade
        velocityY += gravity;
        y += velocityY;

        // Impedindo o personagem de cair além do chão
        if (y >= FLOOR_Y) {
            y = FLOOR_Y;
            velocityY = 0;
            jumping = false;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(sprite, x, y, null);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;

        // Pulo corrigido
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !jumping) {
            jumping = true;
            velocityY = -jumpPower; // A força do pulo agora é maior
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
    }
}
