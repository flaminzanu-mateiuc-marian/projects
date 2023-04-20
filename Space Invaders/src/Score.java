import javax.swing.text.Style;
import java.awt.*;

public class Score {
    public static int score = 0;
    Score(){}
    public static void addScore(int value){
        score += value;
    }
    public void draw(Graphics g)
    {
        g.setColor(new Color(18, 147, 1));
        g.setFont(new Font("Monospaced", Font.BOLD,30));
        g.drawString("Score: "+ score,981,GamePanel.GAME_HEIGHT+122);

    }
}
