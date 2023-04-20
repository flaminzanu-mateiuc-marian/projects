import java.awt.*;

public class Boss {
    static int isMoving = 0;
    final static int BOSS_WIDTH = 250, BOSS_HEIGHT = 175;
    static int health = 100;
    static int xBoss, yBoss, speed = 2;

    Boss(int x, int y) {
        xBoss = x;
        yBoss = y;
    }

    /**
     * desenare boss
     * @param g
     */
    public static void draw(Graphics g) {
        if (isMoving == 1) {
            /**
             * desenare boss in functie de nivel
             */
            switch (GamePanel.level) {
                case 1: {
                    g.drawImage(Main.boss1, xBoss, yBoss, BOSS_WIDTH, BOSS_HEIGHT, null);
                    break;
                }
                case 2: {
                    g.drawImage(Main.boss2, xBoss, yBoss, BOSS_WIDTH, BOSS_HEIGHT, null);
                    break;
                }
                case 3: {
                    g.drawImage(Main.boss3, xBoss, yBoss, BOSS_WIDTH, BOSS_HEIGHT, null);
                    break;
                }
                case 4: {
                    g.drawImage(Main.boss4, xBoss, yBoss, BOSS_WIDTH, BOSS_HEIGHT, null);
                    break;
                }
                case 5: {
                    g.drawImage(Main.boss5, xBoss, yBoss, BOSS_WIDTH, BOSS_HEIGHT, null);
                    break;
                }
                default: {
                    g.drawImage(Main.boss1, xBoss, yBoss, BOSS_WIDTH, BOSS_HEIGHT, null);
                    break;
                }

            }
            /**
             * concomitent, se deseneaza si mingile de foc
             */
            Fireball.draw(g);
            g.setColor(new Color(18, 147, 1));
            g.setFont(new Font("Monospaced", Font.BOLD, 30));
            g.drawString("  Boss Life: " + health, 350, GamePanel.GAME_HEIGHT + 100);
        }
    }

    /**
     * verificare coliziune intre gloatele rachetei si boss
     */
    public static void checkCollision() {
        if (isMoving == 1) {
            for (int i = 0; i < Bullet.playerBulletList.size(); i++) {
                Bullet temp = Bullet.playerBulletList.get(i);
                if (temp.xBullet + 100 >= xBoss && temp.yBullet + 35 >= yBoss+30 && temp.yBullet + 35 <= yBoss + 250) {
                    health -= 3;
                    Score.score += 100;
                    Bullet.playerBulletList.remove(temp);
                    Bullet.listSize--;
                }
            }
        }
    }

    /**
     * miscare boss
     */
    public static void move() {
        isMoving = 1;
        Fireball.checkCollision();
        if (xBoss >= 900) {
            setxBoss(-3);
        }
        setyBoss(speed);
        if (yBoss <= 0 || yBoss >= GamePanel.GAME_HEIGHT - BOSS_HEIGHT) {
            speed = (-1) * speed;
        }
        Fireball.move();

    }

    public static void setxBoss(int val) {
        xBoss += val;
    }

    public static void setyBoss(int val) {
        yBoss += val;
    }

}
