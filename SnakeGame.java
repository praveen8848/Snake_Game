import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

class Node {
    int x, y;
    Node next;
    
    Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.next = null;
    }
}

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 600, HEIGHT = 600, UNIT_SIZE = 20;
    private Node head, tail;
    private int foodX, foodY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        random = new Random();
        startGame();
    }

    public void startGame() {
        head = new Node(UNIT_SIZE * 5, UNIT_SIZE * 5);
        tail = head;
        generateFood();
        running = true;
        timer = new Timer(100, this);
        timer.start();
    }

    public void generateFood() {
        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        if (!running) return;
        int newX = head.x, newY = head.y;
        switch (direction) {
            case 'U': newY -= UNIT_SIZE; break;
            case 'D': newY += UNIT_SIZE; break;
            case 'L': newX -= UNIT_SIZE; break;
            case 'R': newX += UNIT_SIZE; break;
        }
        
        if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT || checkCollision(newX, newY)) {
            running = false;
            timer.stop();
            return;
        }

        Node newHead = new Node(newX, newY);
        newHead.next = head;
        head = newHead;

        if (newX == foodX && newY == foodY) {
            generateFood();
        } else {
            Node temp = head;
            while (temp.next.next != null) {
                temp = temp.next;
            }
            temp.next = null;
        }
    }

    public boolean checkCollision(int x, int y) {
        Node temp = head;
        while (temp != null) {
            if (temp.x == x && temp.y == y) return true;
            temp = temp.next;
        }
        return false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.GREEN);
            Node temp = head;
            while (temp != null) {
                g.fillRect(temp.x, temp.y, UNIT_SIZE, UNIT_SIZE);
                temp = temp.next;
            }
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: if (direction != 'R') direction = 'L'; break;
            case KeyEvent.VK_RIGHT: if (direction != 'L') direction = 'R'; break;
            case KeyEvent.VK_UP: if (direction != 'D') direction = 'U'; break;
            case KeyEvent.VK_DOWN: if (direction != 'U') direction = 'D'; break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game using Linked List");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
