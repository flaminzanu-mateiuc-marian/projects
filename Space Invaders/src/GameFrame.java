import javax.swing.*;

public class GameFrame extends JFrame
{
    /**
     * Clasa ce definieste parametrii ferestrei si instantiaza un obiect de tip GamePanel
     */
    GameFrame()
    {
        GamePanel gpl  = new GamePanel();
        this.add(gpl);
        this.setTitle("Space Impact");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
