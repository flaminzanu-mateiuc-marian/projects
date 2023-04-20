import java.awt.*;
import java.util.Random;

public class Heart {
    static Random rand = new Random();
    static int xHeart, yHeart;

    Heart(int x, int y) {
        xHeart = x;
        yHeart = y;
    }

    public static void resetHeart() {
        xHeart = 2000;
        yHeart = rand.nextInt(GamePanel.GAME_HEIGHT-70);
    }

    public static void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(Main.heart, xHeart, yHeart, 70, 70, null);

    }

    public static void checkCollision() {
        if (Rocket.xRocket + 30 >= xHeart && Rocket.yRocket + 70 >= yHeart && Rocket.yRocket <= yHeart + 70) {
            if(Rocket.life!=5)
                Rocket.life ++;
            xHeart = 2000;
            yHeart = rand.nextInt(GamePanel.GAME_HEIGHT-70);
        }
    }

    public static void move() {
        xHeart -= 2;
        if (xHeart < -70) {
            xHeart = 2000;
            yHeart = rand.nextInt(GamePanel.GAME_HEIGHT-70);
        }
    }
}
