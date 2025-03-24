
/**
 * Write a description of class SnakeGame here.
 *
 * @author Rayhan A Shium
 * @version 02-17-2025
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 25;
    private final int GRID_WIDTH = 20;
    private final int GRID_HEIGHT = 20;
    private final int SCREEN_WIDTH = TILE_SIZE * GRID_WIDTH;
    private final int SCREEN_HEIGHT = TILE_SIZE * GRID_HEIGHT;
    private final int MAX_SNAKE_LENGTH = GRID_WIDTH * GRID_HEIGHT;
    
    private final int[] x = new int[MAX_SNAKE_LENGTH];
    private final int[] y = new int[MAX_SNAKE_LENGTH];
    private int snakeLength;
    private int foodX, foodY;
    private int score;
    private int speed;
    
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                    case KeyEvent.VK_SPACE:
                        if (!running) {
                            startGame();
                        }
                        break;
                }
            }
        });
        showStartScreen();
    }

    public void showStartScreen() {
        String[] options = {"Slow", "Normal", "Fast"};
        int choice = JOptionPane.showOptionDialog(this, "Choose Speed:", "Snake Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        switch (choice) {
            case 0:
                speed = 200;
                break;
            case 1:
                speed = 150;
                break;
            case 2:
                speed = 100;
                break;
            default:
                speed = 150;
        }
        startGame();
    }

    public void startGame() {
        snakeLength = 5;
        score = 0;
        direction = 'R';
        for (int i = 0; i < snakeLength; i++) {
            x[i] = (GRID_WIDTH / 2 - i) * TILE_SIZE;
            y[i] = GRID_HEIGHT / 2 * TILE_SIZE;
        }
        random = new Random();
        spawnFood();
        running = true;
        timer = new Timer(speed, this);
        timer.start();
        repaint();
    }

    public void spawnFood() {
        foodX = random.nextInt(GRID_WIDTH) * TILE_SIZE;
        foodY = random.nextInt(GRID_HEIGHT) * TILE_SIZE;
    }

    public void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U': y[0] -= TILE_SIZE; break;
            case 'D': y[0] += TILE_SIZE; break;
            case 'L': x[0] -= TILE_SIZE; break;
            case 'R': x[0] += TILE_SIZE; break;
        }
    }

    public void checkCollision() {
        for (int i = 1; i < snakeLength; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            snakeLength++;
            score += 10;
            spawnFood();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);
            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.CYAN);
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String text = "Game Over! Score: " + score;
        g.drawString(text, (SCREEN_WIDTH - metrics.stringWidth(text)) / 2, SCREEN_HEIGHT / 2);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Press SPACE to Play Again", (SCREEN_WIDTH - metrics.stringWidth("Press SPACE to Play Again")) / 2, SCREEN_HEIGHT / 2 + 40);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

