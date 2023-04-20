import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Fireball {
    static public int maxNrOfFireballs = 5;
    static Random random = new Random();
    static LinkedList<Fireball> fireballList = new LinkedList<>();
    int xFireball, yFireball;
    static int listSize = 0;
    int speed = 25;

    Fireball(int x, int y) {
        xFireball = x;
        yFireball = y;
    }

    /**
     * eliberare lista mingi de foc
     */
    public static void clearFireballList() {
        if (fireballList != null) {
            for (int i = 0; i < fireballList.size(); i++) {
                Fireball temp = fireballList.get(i);
                fireballList.remove(temp);
            }
        }
    }

    /**
     * adaugare minge de foc in lista
     * @param x
     * @param y
     */
    public static void addFireball(int x, int y) {
        fireballList.add(new Fireball(x, y));
    }

    /**
     * generare lista mingi de foc
     * @return
     */
    public static int[] generateFireballList() {
        int[] randomY = new int[maxNrOfFireballs];
        if (Boss.isMoving == 1) {
            randomY[0] = random.nextInt(104);
            randomY[1] = 104 + random.nextInt(104);
            randomY[2] = 208 + random.nextInt(104);
            randomY[3] = 312 + random.nextInt(104);
            randomY[4] = 418 + random.nextInt(104 - 70);
        }
        return randomY;
    }

    /**
     * desenare mingi de foc
     * @param g
     */
    public static void draw(Graphics g) {
        if (fireballList!=null)
        for (Fireball fireball : Fireball.fireballList) {
            g.drawImage(Main.fire, fireball.xFireball, fireball.yFireball, null);
        }
    }

    /**
     * miscare mingi de foc
     */
    public static void move() {
        /**
         * verificare daca boss-ul se misca
         */
        if (Boss.isMoving == 1) {
            /**
             * populare lista
             */
            int[] randomY = new int[maxNrOfFireballs];
            if (listSize <= 0) {
                randomY = generateFireballList();
            }
            for (int random : randomY) {
                if (Boss.yBoss == random) {
                    addFireball(GamePanel.GAME_WIDTH - Boss.BOSS_WIDTH, random);
                    listSize++;
                }
            }
            if (listSize > 0) {
                for (Fireball fireball : Fireball.fireballList) {
                    fireball.setxFireball();
                }
                for (int i = 0; i < fireballList.size(); i++) {
                    Fireball temp = fireballList.get(i);
                    if (temp.xFireball < 0) {
                        fireballList.remove(temp);
                        listSize--;
                    }
                }
            }
        }
    }

    public void setxFireball() {
        xFireball = xFireball - speed;
    }

    /**
     * verificari coliziuni
     */
    public static void checkCollision() {
        if (Boss.isMoving == 1) {
            for (int i = 0; i < fireballList.size(); i++) {
                Fireball temp = fireballList.get(i);
                if (Rocket.xRocket + 30 >= temp.xFireball && Rocket.yRocket + 70 >= temp.yFireball && Rocket.yRocket <= temp.yFireball + 70) {
                    fireballList.remove(temp);
                    Rocket.life--;
                    listSize--;
                }
            }
        }
    }
}
