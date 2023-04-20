import java.awt.*;

public class HUD {
    HUD(){}
    public static void draw(Graphics g)
    {
        g.setColor(new Color(18, 147, 1));
        g.setFont(new Font("Monospaced", Font.BOLD,30));
        g.drawString("  Life: "+ Rocket.life,50,GamePanel.GAME_HEIGHT+100);
        if(Rocket.shells <= 0) {
            g.setColor(new Color(217, 8, 8));
        }
        g.drawString("Shells: "+ Rocket.shells,50,GamePanel.GAME_HEIGHT+150);

        if(Bullet.listSize == Bullet.maxNumberOfBulletsAtATime) {
            g.setColor(new Color(217, 8, 8));
            g.drawString("Maximum "+Bullet.maxNumberOfBulletsAtATime+" shells at a time!",330, GamePanel.GAME_HEIGHT + 130);
        }
        g.setFont(new Font("Monospaced", Font.BOLD,20));
        g.setColor(new Color(18, 147, 1));
        g.drawString("Press ESC to return to the main menu!",50,GamePanel.GAME_HEIGHT+170);


    }

}
