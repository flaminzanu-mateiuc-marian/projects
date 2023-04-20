import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Bullet extends JComponent {
    static LinkedList<Bullet> playerBulletList = new LinkedList<>();
    int xBullet, yBullet;
    static int listSize = 0;
    final static int maxNumberOfBulletsAtATime = 7;
    int speed = 10;

    Bullet(int x, int y) {
        xBullet = x;
        yBullet = y;
    }

    /**
     * eliberare gloante din lista
     */
    public static void clearBulletList() {
        for (int i = 0; i < playerBulletList.size(); i++) {
            Bullet temp = playerBulletList.get(i);
            playerBulletList.remove(temp);
            listSize--;
        }
    }

    /**
     * Adaugare glont nou in lista
     *
     * @param x
     * @param y
     */
    public static void addPlayerBullet(int x, int y) {
        if (listSize < maxNumberOfBulletsAtATime) {
            playerBulletList.add(new Bullet(x, y));
            listSize++;
        }
    }

    public static void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (playerBulletList != null)
            for (Bullet bullet : playerBulletList)
                g2d.drawImage(Main.missle_player, bullet.xBullet, bullet.yBullet, null);
    }

    public static void move() {
        for (Bullet bullet : playerBulletList) {
            bullet.setXBullet();
        }
        for (int i = 0; i < playerBulletList.size(); i++) {
            Bullet temp = playerBulletList.get(i);
            if (temp.xBullet > 1280) {
                playerBulletList.remove(temp);
                listSize--;
            }
        }
    }

    public void setXBullet() {
        xBullet = xBullet + speed;
    }

    public static void checkCollision() {
        int i, j;
        for (i = 0; i < playerBulletList.size(); i++) {
            Bullet tempBullet = playerBulletList.get(i);
            for (j = 0; j < Enemy.enemyList.size(); j++) {
                Enemy tempEnemy = Enemy.enemyList.get(j);
                if (tempBullet.xBullet + 11 >= tempEnemy.xEnemy && tempBullet.yBullet >= tempEnemy.yEnemy && tempBullet.yBullet <= tempEnemy.yEnemy + 70) {
                    playerBulletList.remove(tempBullet);
                    listSize--;
                    Enemy.enemyList.remove(tempEnemy);
                    Enemy.numberOfEnemies--;
                    Enemy.numberOfWastedEnemies++;
                    Score.addScore(100);
                }
            }
        }
    }
}
