package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Game extends JPanel implements Runnable, KeyListener {
    private boolean running = false;
    private Thread gameThread;
    private final int WIDTH = 1550, HEIGHT = 810;
    private Player player;
    private ArrayList<Rectangle> platforms;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        player = new Player(100, 600);
        setupPlatforms();
    }

    private void setupPlatforms() {
        platforms = new ArrayList<>();
        platforms.add(new Rectangle(0, HEIGHT - 50, WIDTH, 50));
        platforms.add(new Rectangle(200, 600, 200, 20));
        platforms.add(new Rectangle(500, 450, 200, 20));
        platforms.add(new Rectangle(800, 300, 200, 20));
    }

    public void start() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            update();
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update(WIDTH, HEIGHT, platforms);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.GREEN);
        for (Rectangle platform : platforms) {
            g.fillRect(platform.x, platform.y, platform.width, platform.height);
        }

        player.draw(g);
    }

    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Engine 2D");
        Game game = new Game();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        game.start();
    }
}
