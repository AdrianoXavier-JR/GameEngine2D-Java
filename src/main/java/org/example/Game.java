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

        platforms.add(new Rectangle(0, HEIGHT - 50, WIDTH, 50)); // Chão

        if (level == 1) {
            platforms.add(new Rectangle(200, 600, 200, 20));
            platforms.add(new Rectangle(500, 450, 200, 20));
            platforms.add(new Rectangle(800, 300, 200, 20));

            coins.add(new Coin(250, 560));
            coins.add(new Coin(550, 410));
            coins.add(new Coin(850, 260));
        } else if (level == 2) {
            platforms.add(new Rectangle(300, 550, 150, 20));
            platforms.add(new Rectangle(600, 400, 150, 20));
            platforms.add(new Rectangle(900, 250, 150, 20));

            coins.add(new Coin(320, 510));
            coins.add(new Coin(620, 360));
            coins.add(new Coin(920, 210));
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

            // Detecta colisão com o chão em toda sua extensão
            Rectangle ground = platforms.get(0);
            Rectangle playerBounds = player.getBounds();
            if (playerBounds.y + playerBounds.height >= ground.y &&
                    playerBounds.x + playerBounds.width > ground.x &&
                    playerBounds.x < ground.x + ground.width) {

                System.out.println("Jogador tocou o chão!"); // Debug para verificar a colisão
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

            if (coins.isEmpty()) {
                level++;
                if (level > 2) {
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

        g.setColor(new Color(139, 69, 19)); // Marrom para o chão
        g.fillRect(0, HEIGHT - 50, WIDTH, 50);

        g.setColor(Color.GRAY);
        for (int i = 1; i < platforms.size(); i++) { // Ignora o chão (índice 0)
            Rectangle platform = platforms.get(i);
            g.fillRect(platform.x, platform.y, platform.width, platform.height);
        }

        for (Coin coin : coins) {
            coin.draw(g);
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