import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Rocket extends JComponent {
    public static int life = 3;
    public static int shells = 20;
    int ROCKET_WIDTH = 70;
    int ROCKET_HEIGHT = 70;
    int xVelocity;
    int yVelocity;
    int speed = 8;
    public static int yRocket;
    public static int xRocket;;
    Rocket(int x, int y) {
        xRocket = x;
        yRocket = y;
    }

    /**
     * Resetare parametri racheta
     */
    public void resetRocket(){
        life = 3;
        shells = 20;
    }

    /**
     * Metode care trateaza miscarea sus-jos a rachetei
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(-speed);
        }
        if (e.getKeyCode() == KeyEvent.VK_S  || e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(speed);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W  || e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_S  || e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(shells > 0 && Bullet.listSize < Bullet.maxNumberOfBulletsAtATime) {
                Bullet.addPlayerBullet(xRocket + ROCKET_WIDTH, yRocket + ROCKET_HEIGHT / 2);
                shells--;
            }
        }
    }

    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    /**
     * Miscarea rachetei
     */
    public void move() {
        xRocket = xRocket + xVelocity;
        yRocket = yRocket + yVelocity;

    }

    /**
     * Verificare coliziuni
     */
    public static void checkCollision(){
        if (Rocket.yRocket <= 0)
            Rocket.yRocket = 0;
        if (Rocket.yRocket >= GamePanel.GAME_HEIGHT - 70)
            Rocket.yRocket = GamePanel.GAME_HEIGHT - 70;
    }

    /**
     * Desenare racheta
     * @param g
     */
    public void draw(Graphics g) {
            g.drawImage(Main.rocket, xRocket, yRocket, null);
    }

    /**
     * Verficare sfarsit joc: viata rachetei sa fie zero
     * @return
     */
    public boolean isGameOver(){
        if(life <= 0)
            return true;
        return false;
    }
}
