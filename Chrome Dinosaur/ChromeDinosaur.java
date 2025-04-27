import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener {
    // Board
    final int boardWidth = 750;
    final int boardHeight = 250;

    // Images
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;
    Image bird1Img;
    Image bird2Img;

    // Dinosaur
    final int dinosaurWidth = 88;
    final int dinosaurHeight = 94;
    final int dinosaurX = 50;
    final int dinosaurGroundY = boardHeight - dinosaurHeight;
    Block dinosaur;

    // Obstacles
    final int cactus1Width = 34;
    final int cactus2Width = 69;
    final int cactus3Width = 102;
    final int cactusHeight = 70;
    final int birdWidth = 84;
    final int birdHeight = 54;

    ArrayList<Block> cactusArray;
    ArrayList<AnimatedBird> birdArray;

    // Physics
    int velocityX = -12;
    int velocityY = 0;
    int gravity = 1;

    // Game State
    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer cactusTimer;
    Timer birdTimer;

    public ChromeDinosaur() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        loadImages();

        dinosaur = new Block(dinosaurX, dinosaurGroundY, dinosaurWidth, dinosaurHeight, dinosaurImg);
        cactusArray = new ArrayList<>();
        birdArray = new ArrayList<>();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        cactusTimer = new Timer(1500, e -> placeCactus());
        cactusTimer.start();

        birdTimer = new Timer(2500, e -> placeBird());
        birdTimer.start();
    }

    void loadImages() {
        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();
        bird1Img = new ImageIcon(getClass().getResource("./img/bird1.png")).getImage();
        bird2Img = new ImageIcon(getClass().getResource("./img/bird2.png")).getImage();
    }

    void placeCactus() {
        if (gameOver) return;

        double chance = Math.random();
        if (chance > 0.90) {
            cactusArray.add(new Block(boardWidth, boardHeight - cactusHeight, cactus3Width, cactusHeight, cactus3Img));
        } else if (chance > 0.70) {
            cactusArray.add(new Block(boardWidth, boardHeight - cactusHeight, cactus2Width, cactusHeight, cactus2Img));
        } else if (chance > 0.50) {
            cactusArray.add(new Block(boardWidth, boardHeight - cactusHeight, cactus1Width, cactusHeight, cactus1Img));
        }

        if (cactusArray.size() > 10) cactusArray.remove(0);
    }

    void placeBird() {
        if (gameOver) return;

        if (Math.random() > 0.7) { // %30 ihtimalle kuş gelir
            int randomY;
            double r = Math.random();

            if (r < 0.4) { // %40 alçak
                randomY = boardHeight - 100;
            } else if (r < 0.75) { // %35 orta
                randomY = boardHeight - 150;
            } else { // %25 yüksek
                randomY = boardHeight - 200;
            }

            AnimatedBird bird = new AnimatedBird(boardWidth, randomY, birdWidth, birdHeight, bird1Img, bird2Img);
            birdArray.add(bird);

            if (birdArray.size() > 5) birdArray.remove(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    void draw(Graphics g) {
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        for (Block cactus : cactusArray) {
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        for (AnimatedBird bird : birdArray) {
            g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + score, 10, 35);
        } else {
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    void move() {
        // Dinosaur
        velocityY += gravity;
        dinosaur.y += velocityY;

        if (dinosaur.y > dinosaurGroundY) {
            dinosaur.y = dinosaurGroundY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        // Cactus
        for (Block cactus : cactusArray) {
            cactus.x += velocityX;
            if (collision(dinosaur, cactus)) {
                gameOver();
            }
        }

        // Birds
        for (AnimatedBird bird : birdArray) {
            bird.x += velocityX;
            bird.animate();
            if (collision(dinosaur, bird)) {
                gameOver();
            }
        }

        score++;
    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    void gameOver() {
        gameOver = true;
        dinosaur.img = dinosaurDeadImg;
        gameLoop.stop();
        cactusTimer.stop();
        birdTimer.stop();
    }

    void restart() {
        dinosaur.y = dinosaurGroundY;
        dinosaur.img = dinosaurImg;
        velocityY = 0;
        cactusArray.clear();
        birdArray.clear();
        score = 0;
        gameOver = false;
        gameLoop.start();
        cactusTimer.start();
        birdTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                restart();
            } else if (dinosaur.y == dinosaurGroundY) {
                velocityY = -17;
                dinosaur.img = dinosaurJumpImg;
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    // Block Class
    class Block {
        int x, y, width, height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    // Animated Bird Class
    class AnimatedBird extends Block {
        Image img1, img2;
        int animationCounter = 0;
        int animationSpeed = 5;

        AnimatedBird(int x, int y, int width, int height, Image img1, Image img2) {
            super(x, y, width, height, img1);
            this.img1 = img1;
            this.img2 = img2;
        }

        void animate() {
            animationCounter++;
            if (animationCounter >= animationSpeed) {
                img = (img == img1) ? img2 : img1;
                animationCounter = 0;
            }
        }
    }
}
