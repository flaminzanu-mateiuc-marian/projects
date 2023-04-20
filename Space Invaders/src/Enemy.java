import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Enemy {
    static int numberOfWastedEnemies = 0;
    static int numberOfEnemies;
    static int enemySpeed = 8;
    static LinkedList<Enemy> enemyList = new LinkedList<>();
    static Random rand = new Random();
    int xEnemy, yEnemy;

    Enemy(int x, int y) {
        xEnemy = x;
        yEnemy = y;
        numberOfEnemies = enemyList.size();
    }

    /**
     * generare lista cu "nr" inamici
     * @param nr
     */
    public static void generateEnemyList(int nr) {
        for (int i = 0; i < nr; i++) {
            enemyList.add(new Enemy(GamePanel.GAME_WIDTH / 2 + rand.nextInt(0, GamePanel.GAME_WIDTH / 2), rand.nextInt(GamePanel.GAME_HEIGHT - 70)));
        }
        numberOfEnemies = nr;
    }

    /**
     * eliberare lista
     */
    public static void clearEnemyList() {
        if (enemyList.size() > 0 && enemyList != null)
            for (int i = 0; i < enemyList.size(); i++) {
                Enemy temp = enemyList.get(i);
                enemyList.remove(temp);
            }
        numberOfEnemies = 0;
    }

    /**
     * desenare inamici
     * @param g
     */
    public static void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(enemyList!= null)
            for (Enemy enemy : enemyList) {
                g2d.drawImage(Main.ufo, enemy.xEnemy, enemy.yEnemy, 70, 70, null);
            }
    }

    /**
     * Deplasare inamici
     */
    public static void move() {
        if (enemyList != null) {
            for (Enemy enem : enemyList) {
                enem.setXEnemy();
            }
            for (int i = 0; i < enemyList.size(); i++) {
                Enemy temp = enemyList.get(i);
                if (temp.xEnemy < -70) {
                    enemyList.remove(temp);
                    numberOfEnemies--;
                    numberOfWastedEnemies++;
                }
            }
        }
    }

    /**
     * verificare coliziuni cu racheta
     */
    public static void checkCollision() {
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy temp = enemyList.get(i);
            if (Rocket.xRocket + 70 >= temp.xEnemy && Rocket.yRocket + 70 >= temp.yEnemy && Rocket.yRocket <= temp.yEnemy + 70) {
                enemyList.remove(temp);
                Rocket.life--;
                numberOfEnemies--;
                numberOfWastedEnemies++;
            }
        }
    }

    /**
     * deplasare
     */
    public void setXEnemy() {
        xEnemy -= enemySpeed;
    }


}
