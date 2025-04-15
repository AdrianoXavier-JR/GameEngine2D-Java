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
    private ArrayList<Enemy> enemies; // Lista de inimigos
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
        enemies = new ArrayList<>(); // Inicializa os inimigos

        platforms.add(new Rectangle(0, HEIGHT - 50, WIDTH, 50)); // Chão

        if (level == 1) {
            platforms.add(new Rectangle(200, 600, 200, 20));
            platforms.add(new Rectangle(500, 450, 200, 20));
            platforms.add(new Rectangle(800, 300, 200, 20));

            coins.add(new Coin(250, 560));
            coins.add(new Coin(550, 410));
            coins.add(new Coin(850, 260));

            // Adicionando inimigos menores e mais rápidos no nível 1
            enemies.add(new Enemy(300, 580, 30, 30, 4)); // Inimigo menor e mais rápido
            enemies.add(new Enemy(600, 430, 30, 30, 5)); // Outro inimigo menor e mais rápido
        } else if (level == 2) {
            platforms.add(new Rectangle(300, 550, 150, 20));
            platforms.add(new Rectangle(600, 400, 150, 20));
            platforms.add(new Rectangle(900, 250, 150, 20));

            coins.add(new Coin(320, 510));
            coins.add(new Coin(620, 360));
            coins.add(new Coin(920, 210));

            // Adicionando inimigos menores e mais rápidos no nível 2
            enemies.add(new Enemy(350, 530, 30, 30, 5));
            enemies.add(new Enemy(650, 380, 30, 30, 6));
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

            // Detecta colisão com o chão
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

            // Remove moedas coletadas
            coins.removeIf(coin -> coin.getBounds().intersects(player.getBounds()));

            // Atualiza inimigos e verifica colisão com o jogador
            for (Enemy enemy : enemies) {
                enemy.update(WIDTH);
                if (enemy.getBounds().intersects(playerBounds)) {
                    System.out.println("Colisão com inimigo detectada!");
                    lives--; // Perde uma vida ao colidir com um inimigo
                    if (lives <= 0) {
                        gameOver = true;
                        restartButton.setVisible(true);
                    } else {
                        Rectangle firstPlatform = platforms.get(1);
                        player.respawn(firstPlatform.x + 10, firstPlatform.y - player.getHeight());
                    }
                }
            }

            // Passa para o próximo nível se todas as moedas forem coletadas
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

        for (Enemy enemy : enemies) {
            enemy.draw(g); // Desenha os inimigos na tela
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