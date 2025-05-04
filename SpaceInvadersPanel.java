import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class SpaceInvadersPanel extends JPanel implements ActionListener, KeyListener {
    int tileSize = 32, rows = 16, columns = 16;
    int boardWidth = tileSize * columns;
    int boardHeight = tileSize * rows;

    Image shipImg, alienImg, alienCyanImg, alienMagentaImg, alienYellowImg;
    ArrayList<Image> alienImgArray;

    BufferedImage bgImg;

    class Block {
        int x, y, width, height;
        Image img;
        boolean alive = true, used = false;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x; this.y = y; this.width = width; this.height = height; this.img = img;
        }
    }

    int shipWidth = tileSize * 2, shipHeight = tileSize;
    int shipX = tileSize * columns / 2 - tileSize, shipY = tileSize * rows - tileSize * 2;
    int shipVelocityX = tileSize;
    Block ship;

    ArrayList<Block> alienArray = new ArrayList<>();
    int alienWidth = tileSize * 2, alienHeight = tileSize;
    int alienX = tileSize, alienY = tileSize;
    int alienRows = 2, alienColumns = 3, alienCount = 0, alienVelocityX = 1, alienWaveOffset = 0;

    ArrayList<Block> bulletArray = new ArrayList<>();
    int bulletWidth = tileSize / 8, bulletHeight = tileSize / 2, bulletVelocityY = -10;

    ArrayList<Block> alienBulletArray = new ArrayList<>();
    int alienBulletVelocityY = 5;

    Timer gameLoop;
    boolean gameOver = false;
    int score = 0;
    Random rand = new Random();

    Clip laserClip, bgClip;

    public SpaceInvadersPanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        try {
            bgImg = ImageIO.read(new File("bgimg.png"));
        } catch (IOException e) {
            System.err.println("Error loading background image: bgimg.png");
            e.printStackTrace();
            bgImg = null;
        }

        shipImg = new ImageIcon("ship.png").getImage();
        alienImg = new ImageIcon("alien.png").getImage();
        alienCyanImg = new ImageIcon("alien-cyan.png").getImage();
        alienMagentaImg = new ImageIcon("alien-magenta.png").getImage();
        alienYellowImg = new ImageIcon("alien-yellow.png").getImage();

        alienImgArray = new ArrayList<>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);

        createAliens();
        gameLoop = new Timer(1000 / 60, this);

        laserClip = loadClip("laser.wav");
        bgClip = loadClip("spacebg.wav");
        if (bgClip != null) bgClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private Clip loadClip(String path) {
        try {
            File soundFile = new File(path);
            AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
        } catch (Exception e) {
            System.err.println("Error loading sound: " + path);
            return null;
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
        gameLoop.start();
        if (bgClip != null) bgClip.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) move();
        repaint();
        if (gameOver && bgClip != null) bgClip.stop();
    }

    public void move() {
        alienWaveOffset++;

        for (Block alien : alienArray) {
            if (alien.alive) {
                alien.x += alienVelocityX;
                alien.y += (int)(5 * Math.sin((alien.x + alienWaveOffset) * 0.05));

                if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
                    alienVelocityX *= -1;
                    for (Block a : alienArray) a.y += alienHeight;
                    break;
                }

                if (rand.nextInt(1000) < 3) {
                    Block bullet = new Block(alien.x + alienWidth / 2, alien.y + alienHeight, bulletWidth, bulletHeight, null);
                    alienBulletArray.add(bullet);
                }

                if (alien.y + alien.height >= ship.y) {
                    gameOver = true;
                }
            }
        }

        for (Block bullet : bulletArray) {
            bullet.y += bulletVelocityY;
            for (Block alien : alienArray) {
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }

        for (Block bullet : alienBulletArray) {
            bullet.y += alienBulletVelocityY;
            if (!bullet.used && detectCollision(bullet, ship)) {
                bullet.used = true;
                gameOver = true;
            }
        }

        bulletArray.removeIf(b -> b.used || b.y < 0);
        alienBulletArray.removeIf(b -> b.used || b.y > boardHeight);

        if (alienCount == 0) {
            score += alienColumns * alienRows * 100;
            alienColumns = Math.min(alienColumns + 1, columns / 2 - 2);
            alienRows = Math.min(alienRows + 1, rows - 6);
            alienArray.clear();
            bulletArray.clear();
            alienBulletArray.clear();

            // Reset alien spawn position
            alienX = tileSize;
            alienY = tileSize;
            createAliens();
        }
    }

    public void createAliens() {
        for (int c = 0; c < alienColumns; c++) {
            for (int r = 0; r < alienRows; r++) {
                int imgIndex = rand.nextInt(alienImgArray.size());
                Block alien = new Block(alienX + c * alienWidth, alienY + r * alienHeight, alienWidth, alienHeight, alienImgArray.get(imgIndex));
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Block a, Block b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bgImg != null) {
            g.drawImage(bgImg, 0, 0, boardWidth, boardHeight, this);
        } else {
            g.setColor(Color.black);
            g.fillRect(0, 0, boardWidth, boardHeight);
        }

        if (ship.alive)
            g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, this);

        for (Block alien : alienArray)
            if (alien.alive)
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, this);

        g.setColor(Color.WHITE);
        for (Block bullet : bulletArray)
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);

        g.setColor(Color.RED);
        for (Block bullet : alienBulletArray)
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        if (gameOver)
            g.drawString("Game Over! Press any key to restart", boardWidth / 2 - 100, boardHeight / 2);
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            ship.x = shipX;
            bulletArray.clear();
            alienArray.clear();
            alienBulletArray.clear();
            gameOver = false;
            score = 0;
            alienColumns = 3;
            alienRows = 2;
            alienVelocityX = 1;

            alienX = tileSize;
            alienY = tileSize;
            createAliens();
            gameLoop.start();
            if (bgClip != null) bgClip.setFramePosition(0);
            if (bgClip != null) bgClip.start();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0) {
            ship.x -= shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipVelocityX + ship.width <= boardWidth) {
            ship.x += shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Block bullet = new Block(ship.x + shipWidth / 2 - bulletWidth / 2, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
            if (laserClip != null) {
                laserClip.stop();
                laserClip.setFramePosition(0);
                laserClip.start();
            }
        }
    }
}
