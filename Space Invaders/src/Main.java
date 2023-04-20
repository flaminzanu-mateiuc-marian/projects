import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class Main {
    static public BufferedImage metal = null;
    static public BufferedImage bg1 = null;
    static public BufferedImage bg2 = null;
    static public BufferedImage bg3 = null;
    static public BufferedImage bg4 = null;
    static public BufferedImage bg5 = null;
    static public BufferedImage rocket = null;
    static public BufferedImage ufo = null;
    static public BufferedImage heart = null;
    static public BufferedImage chest = null;
    static public BufferedImage bomb = null;
    static public BufferedImage diamond = null;
    static public BufferedImage fire = null;
    static public BufferedImage missle_player = null;
    static public BufferedImage missle_enemy = null;
    static public BufferedImage boss1 = null;
    static public BufferedImage boss2 = null;
    static public BufferedImage boss3 = null;
    static public BufferedImage boss4 = null;
    static public BufferedImage boss5 = null;

    /**
     * Functia main
     */
    public static void main(String[] args) {
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (Exception e){
            System.out.println("eroare");
        }
        GameFrame gameFrame = new GameFrame();
        BufferedImage spriteSheet = null;

        try {
            metal = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\metal.png"));
            bg1 = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\background_1.png"));
            bg2 = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\background_2.png"));
            bg3 = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\background_3.png"));
            bg4 = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\background_4.png"));
            bg5 = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\background_5.png"));
            spriteSheet = ImageIO.read(new File("spritesheet.png"));
        }catch (Exception e){
            System.exit(JFrame.ERROR);
        }

        rocket  = spriteSheet.getSubimage(710,327,70,70);

        bomb  = spriteSheet.getSubimage(0,0,70,70);
        diamond  = spriteSheet.getSubimage(500,310,70,70);
        ufo  = spriteSheet.getSubimage(501,398,68,70);
        chest  = spriteSheet.getSubimage(571,398,70,70);
        heart  = spriteSheet.getSubimage(640,310,70,70);
        fire  = spriteSheet.getSubimage(570,310,70,70);
        //missles: 35x15
        missle_player  = spriteSheet.getSubimage(711,311,35,15);
        missle_enemy  = spriteSheet.getSubimage(748,311,35,15);

        boss1  = spriteSheet.getSubimage(70,0,250,175);
        boss2  = spriteSheet.getSubimage(320,0,250,175);
        boss3  = spriteSheet.getSubimage(0,175,250,310);
        boss4  = spriteSheet.getSubimage(250,175,250,310);
        boss5  = spriteSheet.getSubimage(570,0,250,310);

    }
}
