package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements Runnable, KeyListener {
    private boolean running = false;
    private Thread gameThread;
    private final int WIDTH = 1550, HEIGHT = 810;

    private Player player;
    private ArrayList<Rectangle> platforms;
    private ArrayList<Coin> coins;
    private ArrayList<Enemy> enemies;
    private int lives = 3;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private JButton restartButton;
    private int level = 1;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        setupRestartButton();
        player = new Player(250, 560);
        setupLevel(level);
    }

    private void setupRestartButton() {
        restartButton = new JButton("Reiniciar");
        restartButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 + 50, 150, 40);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        setLayout(null);
        add(restartButton);
    }

    private void setupLevel(int level) {
        platforms = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();

        platforms.add(new Rectangle(0, HEIGHT - 50, WIDTH, 50)); // Chão


        if (level == 1) {
            platforms.add(new Rectangle(200, 600, 200, 20));
            platforms.add(new Rectangle(500, 450, 200, 20));
            platforms.add(new Rectangle(800, 300, 200, 20));

            coins.add(new Coin(250, 580));
            coins.add(new Coin(550, 430));
            coins.add(new Coin(850, 280));

            enemies.add(new Enemy(300, 580, 30, 30, 4));
            enemies.add(new Enemy(600, 430, 30, 30, 5));
        } else if (level == 2) {
            platforms.add(new Rectangle(200, 650, 100, 20));
            platforms.add(new Rectangle(400, 500, 150, 20));
            platforms.add(new Rectangle(600, 350, 120, 20));
            platforms.add(new Rectangle(800, 200, 200, 20));
            platforms.add(new Rectangle(1000, 100, 180, 20));

            coins.add(new Coin(220, 630));
            coins.add(new Coin(430, 480));
            coins.add(new Coin(620, 320));
            coins.add(new Coin(820, 150));
            coins.add(new Coin(1020, 50));

            enemies.add(new Enemy(250, 640, 30, 30, 5));
            enemies.add(new Enemy(450, 490, 30, 30, 6));
            enemies.add(new Enemy(650, 340, 30, 30, 6));
            enemies.add(new Enemy(850, 190, 30, 30, 7));
        } else if (level == 3) {
            platforms.add(new Rectangle(100, 700, 100, 20));
            platforms.add(new Rectangle(300, 550, 100, 20));
            platforms.add(new Rectangle(500, 400, 120, 20));
            platforms.add(new Rectangle(700, 250, 100, 20));
            platforms.add(new Rectangle(900, 150, 150, 20));

            coins.add(new Coin(150, 680));
            coins.add(new Coin(350, 530));
            coins.add(new Coin(550, 380));
            coins.add(new Coin(750, 230));
            coins.add(new Coin(950, 130));

            enemies.add(new Enemy(250, 680, 30, 30, 7));
            enemies.add(new Enemy(400, 530, 30, 30, 8));
            enemies.add(new Enemy(600, 380, 30, 30, 9));
            enemies.add(new Enemy(800, 230, 30, 30, 10));
            enemies.add(new Enemy(1000, 130, 30, 30, 11));
        } else if (level == 4) {
            platforms.add(new Rectangle(50, 650, 100, 20));
            platforms.add(new Rectangle(250, 500, 150, 20));
            platforms.add(new Rectangle(450, 350, 180, 20));
            platforms.add(new Rectangle(650, 200, 200, 20));
            platforms.add(new Rectangle(850, 100, 220, 20));

            coins.add(new Coin(100, 630));
            coins.add(new Coin(270, 480));
            coins.add(new Coin(470, 320));
            coins.add(new Coin(670, 150));
            coins.add(new Coin(870, 50));

            enemies.add(new Enemy(150, 630, 30, 30, 10));
            enemies.add(new Enemy(350, 480, 30, 30, 12));
            enemies.add(new Enemy(550, 320, 30, 30, 14));
            enemies.add(new Enemy(750, 150, 30, 30, 16));
            enemies.add(new Enemy(950, 50, 30, 30, 18));
        } else if (level == 5) {
            platforms.add(new Rectangle(100, 750, 120, 20));
            platforms.add(new Rectangle(300, 600, 200, 20));
            platforms.add(new Rectangle(500, 450, 200, 20));
            platforms.add(new Rectangle(700, 300, 180, 20));
            platforms.add(new Rectangle(900, 150, 250, 20));

            coins.add(new Coin(120, 730));
            coins.add(new Coin(320, 580));
            coins.add(new Coin(520, 430));
            coins.add(new Coin(720, 280));
            coins.add(new Coin(920, 100));

            enemies.add(new Enemy(170, 730, 30, 30, 15));
            enemies.add(new Enemy(370, 580, 30, 30, 18));
            enemies.add(new Enemy(570, 430, 30, 30, 20));
            enemies.add(new Enemy(770, 280, 30, 30, 22));
            enemies.add(new Enemy(970, 100, 30, 30, 25));
        }

        Rectangle firstPlatform = platforms.get(1);
        player.respawn(firstPlatform.x + 10, firstPlatform.y - player.getHeight());
    }

    public void restartGame() {
        lives = 3;
        level = 1;
        gameOver = false;
        gameWon = false;
        restartButton.setVisible(false);
        setupLevel(level);
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
        if (!gameOver && !gameWon) {
            player.update(WIDTH, HEIGHT, platforms);

            Rectangle ground = platforms.get(0);
            Rectangle playerBounds = player.getBounds();

            if (playerBounds.y + playerBounds.height >= ground.y &&
                    playerBounds.x + playerBounds.width > ground.x &&
                    playerBounds.x < ground.x + ground.width) {
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                    restartButton.setVisible(true);
                } else {
                    Rectangle firstPlatform = platforms.get(1);
                    player.respawn(firstPlatform.x + 10, firstPlatform.y - player.getHeight());
                }
            }

            coins.removeIf(coin -> coin.getBounds().intersects(player.getBounds()));

            for (Enemy enemy : enemies) {
                enemy.update(WIDTH);
                if (enemy.getBounds().intersects(playerBounds)) {
                    lives--;
                    if (lives <= 0) {
                        gameOver = true;
                        restartButton.setVisible(true);
                    } else {
                        Rectangle firstPlatform = platforms.get(1);
                        player.respawn(firstPlatform.x + 10, firstPlatform.y - player.getHeight());
                    }
                }
            }

            if (coins.isEmpty()) {
                level++;
                if (level > 5) {
                    gameWon = true;
                    restartButton.setVisible(true);
                } else {
                    setupLevel(level);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(new Color(139, 69, 19));
        g.fillRect(0, HEIGHT - 50, WIDTH, 50);

        g.setColor(Color.GRAY);
        for (int i = 1; i < platforms.size(); i++) {
            Rectangle platform = platforms.get(i);
            g.fillRect(platform.x, platform.y, platform.width, platform.height);
        }

        for (Coin coin : coins) {
            coin.draw(g);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        player.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Vidas: " + lives, 20, 30);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.setColor(Color.RED);
            g.drawString("Fim de Jogo", WIDTH / 2 - 150, HEIGHT / 2 - 20);
        } else if (gameWon) {
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.setColor(Color.YELLOW);
            g.drawString("Você Venceu!", WIDTH / 2 - 180, HEIGHT / 2 - 20);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (!gameOver && !gameWon) player.keyPressed(e);
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
