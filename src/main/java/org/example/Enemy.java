package org.example;

import java.awt.*;

public class Enemy {
    private int x, y, width, height, speed;
    private boolean movingRight = true;

    public Enemy(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void update(int screenWidth) {
        if (movingRight) {
            x += speed;
            if (x + width > screenWidth) {
                movingRight = false;
            }
        } else {
            x -= speed;
            if (x < 0) {
                movingRight = true;
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
