import java.awt.*;
import java.util.Random;

public class Chest {
    static Random rand = new Random();
    static int xChest, yChest;

    Chest(int x, int y) {
        xChest = x;
        yChest = y;
    }

    /**
     * Resetare pozitie cufar
     */
    public static void resetChest() {
        xChest = 1400;
        yChest = rand.nextInt(GamePanel.GAME_HEIGHT-70);
    }

    /**
     * desenare
     * @param g
     */
    public static void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(Main.chest, xChest, yChest, 70, 70, null);

    }

    /**
     * Verificare coliziune cu racheta
     */
    public static void checkCollision() {
        if (Rocket.xRocket + 30 >= xChest && Rocket.yRocket + 70 >= yChest && Rocket.yRocket <= yChest + 70) {
            Rocket.shells += 30;
            xChest = 1400;
            yChest = rand.nextInt(GamePanel.GAME_HEIGHT-70);
        }
    }

    /**
     * deplasare cufar
     */
    public static void move() {
        xChest -= 2;
        if (xChest < -70) {
            xChest = 1400;
            yChest = rand.nextInt(GamePanel.GAME_HEIGHT-70);
        }
    }
}

