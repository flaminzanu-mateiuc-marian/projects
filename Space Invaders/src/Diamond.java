import java.awt.*;
import java.util.Random;

public class Diamond {
    static Random rand = new Random();
    static int xDiamond, yDiamond;

    Diamond(int x, int y) {
        xDiamond = x;
        yDiamond = y;
    }

    public static void resetDiamond() {
        xDiamond = 3000;
        yDiamond = rand.nextInt(GamePanel.GAME_HEIGHT-70);
    }

    public static void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(Main.diamond, xDiamond, yDiamond, 70, 70, null);

    }

    public static void checkCollision() {
        if (Rocket.xRocket + 30 >= xDiamond && Rocket.yRocket + 70 >= yDiamond && Rocket.yRocket <= yDiamond + 70) {
            Score.score += 300;
            xDiamond = 3000;
            yDiamond = rand.nextInt(GamePanel.GAME_HEIGHT-70);
        }
    }

    public static void move() {
        xDiamond -= 2;
        if (xDiamond < -70) {
            xDiamond = 3000;
            yDiamond = rand.nextInt(GamePanel.GAME_HEIGHT-70);
        }
    }
}
