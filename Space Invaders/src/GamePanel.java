import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;
import javax.swing.*;
import java.sql.*;


public class GamePanel extends JPanel implements Runnable {
    Connection c = null;
    Statement stmt = null;

    /**
     * Initializari variabile joc
     * Dimenisune ferastra
     */
    static int GAME_WIDTH = 1280;
    static int GAME_HEIGHT = 520;
    final Dimension screen_size = new Dimension(GAME_WIDTH, GAME_HEIGHT + 200);

    int levelPassed = 0; //verificare promovare nivel
    static int level = 1; //nivel curent
    int gameFirstStart = 1; //verifica daca jocul incepe pentru prima data
    int fromGame = 0; //folosit pentru pauza joc: verifica daca se face pauza folosind ESC
    int bossEnabled = 0; //verifica daca boss-ul e activ sau nu
    int levelOfEnemiesAtOnce = 5; //numarul de inamici care exista in orice moment pe tabla
    int numberOfEnemiesToDefeat = 130;//numarul de inamici care trebuie sa fie impuscati sau care sa se despawneze ca sa determine
    static int clearedBulletsBeforeBoss = 0;
    static int savegameExists = 0;
    //stare:
    //-1->in menu; 0->in game; 1->game over; 2->level complete; 3->game finished
    int state = -1;
    double bgX1 = 0;

    public int highlighted = 0; //optiune selectata in meniu

    Diamond diamond;
    Chest chest;
    Random rand;
    Thread gameThread;
    Boss boss;
    Heart heart;
    Score score;
    Rocket Rocket;
    Image image;
    Graphics graphics;


    GamePanel() {
        Rocket = new Rocket(0, 0);
        rand = new Random();
        Boss.isMoving = 0;
        this.addKeyListener(new AL());
        this.setFocusable(true);
        this.setPreferredSize(screen_size);
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Resetare variabile joc
     */
    public void resetEnvironment() {
        Rocket.resetRocket();
        Enemy.numberOfWastedEnemies = 0;
        Enemy.clearEnemyList();
        Enemy.generateEnemyList(levelOfEnemiesAtOnce);
        Bullet.clearBulletList();
        Fireball.clearFireballList();
        Chest.resetChest();
        Heart.resetHeart();
        Diamond.resetDiamond();

        bossEnabled = 0;
        Boss.isMoving = 0;
        Boss.xBoss = 1280;
        Boss.yBoss = GAME_HEIGHT / 2;
        clearedBulletsBeforeBoss = 0;
    }

    /**
     *Generare mediu de joc
     */
    public void generateEnvironment() {
        diamond = new Diamond(3000, rand.nextInt(GAME_HEIGHT - 70));
        chest = new Chest(1400, rand.nextInt(GAME_HEIGHT - 70));
        heart = new Heart(2000, rand.nextInt(GAME_HEIGHT - 70));
        score = new Score();
        boss = new Boss(1280, GAME_HEIGHT / 2);
        Enemy.numberOfWastedEnemies = 0;
        Enemy.generateEnemyList(levelOfEnemiesAtOnce);
        clearedBulletsBeforeBoss = 0;

    }

    /**
     * Desenare componente joc
     * @param g
     */
    public void paint(Graphics g) {
        super.paintComponent(g);
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        super.paintComponent(g);
        /**
         * desenare joc propriu-zis
         */
        if (state == 0) {
            switch (level) {
                case 1: {
                    g.drawImage(Main.bg1, (int) bgX1, 0, 1280, 520, null);
                    g.drawImage(Main.bg1, (int) bgX1 + 1280, 0, 1280, 520, null);
                    break;
                }
                case 2: {
                    g.drawImage(Main.bg2, (int) bgX1, 0, 1280, 520, null);
                    g.drawImage(Main.bg2, (int) bgX1 + 1280, 0, 1280, 520, null);
                    break;
                }
                case 3: {
                    g.drawImage(Main.bg3, (int) bgX1, 0, 1280, 520, null);
                    g.drawImage(Main.bg3, (int) bgX1 + 1280, 0, 1280, 520, null);
                    break;
                }
                case 4: {
                    g.drawImage(Main.bg4, (int) bgX1, 0, 1280, 520, null);
                    g.drawImage(Main.bg4, (int) bgX1 + 1280, 0, 1280, 520, null);
                    break;
                }
                case 5: {
                    g.drawImage(Main.bg5, (int) bgX1, 0, 1280, 520, null);
                    g.drawImage(Main.bg5, (int) bgX1 + 1280, 0, 1280, 520, null);
                    break;
                }
                default: {
                    level = 1;
                    g.drawImage(Main.bg1, (int) bgX1, 0, 1280, 520, null);
                    g.drawImage(Main.bg1, (int) bgX1 + 1280, 0, 1280, 520, null);
                }
            }
            g.drawImage(Main.metal, 0, 520, 1280, 200, null);
            drawComponents(g);
        } else {
            /**
             * desenare meniu
             */
            Image menuBg = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "\\resources\\background_menu.png");
            g.drawImage(menuBg, 0, 0, 1280, 720, null);
            if (state == -1) {
                g.setColor(new Color(10, 173, 173));
                g.setFont(new Font("Monospaced", Font.BOLD, 50));
                if (highlighted == 0)
                    g.setColor(new Color(250, 242, 0));
                if (fromGame == 0)
                    g.drawString("Start new game ", GAME_WIDTH / 2 - 230, 250);
                else
                    g.drawString("Continue game", GAME_WIDTH / 2 - 230, 250);


                File file = new File("space_invaders.db");
                if (file.exists()) {
                    savegameExists = 1;
                    g.setColor(new Color(10, 173, 173));
                    if (highlighted == 1)
                        g.setColor(new Color(250, 242, 0));
                    g.drawString("Load Game ", GAME_WIDTH / 2 - 230, 350);
                } else {
                    g.setFont(new Font("Monospaced", Font.BOLD, 30));
                    g.setColor(new Color(137, 141, 141));
                    g.drawString("No savegame available", GAME_WIDTH / 2 - 230, 350);
                }

                g.setFont(new Font("Monospaced", Font.BOLD, 50));
                g.setColor(new Color(10, 173, 173));
                if (highlighted == 2)
                    g.setColor(new Color(250, 242, 0));
                g.drawString("Exit ", GAME_WIDTH / 2 - 230, 450);

                g.setFont(new Font("Monospaced", Font.BOLD, 20));
                g.setColor(new Color(250, 242, 0));
                g.drawString("In-menu controls ", GAME_WIDTH - 470, 20);
                g.setColor(new Color(10, 173, 173));
                g.drawString("W/S or Up/Down: navigate through menus ", GAME_WIDTH - 470, 40);
                g.drawString("Enter: Select Menu", GAME_WIDTH - 470, 60);
                g.setColor(new Color(250, 242, 0));
                g.drawString("In-game controls ", GAME_WIDTH - 470, 80);
                g.setColor(new Color(10, 173, 173));
                g.drawString("W/S or Up/Down: Control Rocket ", GAME_WIDTH - 470, 100);
                g.drawString("Space: Fire ", GAME_WIDTH - 470, 120);
                g.drawString("ESC: Return to main menu ", GAME_WIDTH - 470, 140);

            } else if (state == 1) {//game over
                g.setFont(new Font("Monospaced", Font.BOLD, 50));
                g.setColor(new Color(250, 242, 0));
                g.drawString("GAME OVER!", GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2);
                g.drawString("Press Enter to return to the main menu!", 30, 650);
                fromGame = 0;
                resetEnvironment();

            } else if (state == 2) {//level passed
                g.setFont(new Font("Monospaced", Font.BOLD, 40));
                g.setColor(new Color(250, 242, 0));
                g.drawString("LEVEL COMPLETE!", GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2);
                g.drawString("Press Enter save the game and proceed to the next level", 30, 650);
                g.drawString("or press ESC proceed without saving the progress!", 30, 680);
                fromGame = 1;
                resetEnvironment();
            } else {
                g.setFont(new Font("Monospaced", Font.BOLD, 40));
                g.setColor(new Color(250, 242, 0));
                g.drawString("THE END!", GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2);
                g.drawString("FINAL SCORE: " + Score.score, GAME_WIDTH / 2 - 100 + 50, GAME_HEIGHT / 2 + 50);
                g.drawString("Press Enter to return to the main menu!", 30, 350);
                fromGame = 0;
                resetEnvironment();
            }
        }
    }

    /**
     * deplasarea obiectelor pe ecran
     */
    public void move() {
        if (state == 0) {
            Rocket.move();
            Bullet.move();
            Enemy.move();
            if (Enemy.numberOfEnemies <= levelOfEnemiesAtOnce - 1 && bossEnabled == 0) {
                Enemy enemy = new Enemy(1280, rand.nextInt(GamePanel.GAME_HEIGHT - 70));
                Enemy.enemyList.add(enemy);
                Enemy.numberOfEnemies++;
            }
            Chest.move();
            Heart.move();
            Diamond.move();
            if (Enemy.numberOfWastedEnemies >= numberOfEnemiesToDefeat)
                bossEnabled = 1;
            if (Enemy.numberOfEnemies <= 0) {
                Boss.isMoving = 1;
                Boss.move();
            }
        }
    }

    /**
     * metoda run()
     */
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                if (state == 0) {
                    if (level <= 5) {
                        bgX1 = bgX1 - 5;
                        if (bgX1 == -1280) {
                            bgX1 = 0;
                            repaint();
                        }
                        move();
                        checkCollision();

                    }

                    if (Rocket.isGameOver()) {
                        level = 1;
                        state = 1; //game over
                        resetEnvironment();
                    }
                    if (Boss.health <= 0) {
                        Boss.health = 100;
                        if (level == 5) {
                            state = 3; //final de joc
                            level = 1;
                        } else {
                            state = 2; //level complete
                            level++;
                        }
                        resetEnvironment();
                        levelPassed = 1;

                    }
                }
                repaint();
                delta--;
            }
        }
    }

    /**
     * clasa pentru tratarea apasarilor de tasta
     */
    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (state == 0)
                Rocket.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            if (state == -1) { //menu
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                    if (highlighted == 0)
                        highlighted = 2;
                    else highlighted--;
                    if (highlighted == 1 && savegameExists != 1) highlighted--;
                }
                if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (highlighted == 2)
                        highlighted = 0;
                    else highlighted++;
                    if (highlighted == 1 && savegameExists != 1)
                        highlighted++;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    switch (highlighted) {
                        case 0:
                            state = 0;
                            if (fromGame == 0) {
                                if (gameFirstStart == 1)
                                    generateEnvironment();
                                else
                                    resetEnvironment();
                            }
                            break;
                        case 1:
                            /**
                             * Incarcare salvare
                             */
                            if (savegameExists == 1) {
                                try {
                                    Class.forName("org.sqlite.JDBC");
                                    c = DriverManager.getConnection("jdbc:sqlite:space_invaders.db");
                                    stmt = c.createStatement();
                                    ResultSet rs = stmt.executeQuery("SELECT * FROM SAVEGAME;");
                                    while (rs.next()) {
                                        level= rs.getInt("LEVEL");
                                        Score.score = rs.getInt("SCORE");
                                    }
                                    rs.close();
                                    stmt.close();
                                    c.close();
                                } catch (Exception f) {
                                    System.out.println("Eroare la citirea din baza de date");
                                }
                                if (gameFirstStart == 1)
                                    generateEnvironment();
                                else
                                    resetEnvironment();
                                state = 0;
                                break;
                            }

                        case 2:
                            System.exit(0);
                        default:
                            break;
                    }
                }
            } else if (state == 0) { //game
                Rocket.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    state = -1;
                    fromGame = 1;
                    highlighted = 0;
                }
            } else if (state == 1 || state == 3) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resetEnvironment();
                    state = -1;
                    Score.score = 0;
                    if(state == 3){
                        File file = new File("space_invaders.db");
                        if(file.exists())
                            file.delete();
                    }
                }
            } else if (state == 2) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    resetEnvironment();
                    state = -1;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    /**
                     * creare salvare noua
                     */
                    try {
                        File file = new File("space_invaders.db");
                        if(file.exists())
                            file.delete();
                        Class.forName("org.sqlite.JDBC");
                        c = DriverManager.getConnection("jdbc:sqlite:space_invaders.db");
                        stmt = c.createStatement();
                        String sql = "CREATE TABLE SAVEGAME " +
                                "(ID INT PRIMARY KEY NOT NULL," +
                                " LEVEL INT NOT NULL, " +
                                " SCORE INT NOT NULL " +
                                " )";
                        stmt.execute(sql);
                        sql = "INSERT INTO SAVEGAME (ID,LEVEL,SCORE) VALUES (1," + level + "," + Score.score + ")";
                        stmt.execute(sql);
                        stmt.close();
                        c.close();
                    } catch (Exception f) {
                        System.err.println(f.getClass().getName() + ": " + f.getMessage());
                        System.exit(0);
                    }
                    resetEnvironment();
                    state = -1;
                }
            }
        }

    }

    /**
     * verificare coliziuni
     */
    public void checkCollision() {
        if (state == 0) {
            Rocket.checkCollision();
            Bullet.checkCollision();
            Enemy.checkCollision();
            Chest.checkCollision();
            Boss.checkCollision();
            Heart.checkCollision();
            Diamond.checkCollision();
        }
    }

    /**
     * clasa folosita pentru centralizarea apelurilor metodelor draw() din clasele implicate in joc
     * @param g
     */
    public void drawComponents(Graphics g) {
        if (state == 0) {
            if (Enemy.numberOfWastedEnemies >= numberOfEnemiesToDefeat)
                Boss.draw(g);
            Rocket.draw(g);
            Bullet.draw(g);
            Enemy.draw(g);
            Chest.draw(g);
            Diamond.draw(g);
            score.draw(g);
            Heart.draw(g);
            HUD.draw(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }
}
