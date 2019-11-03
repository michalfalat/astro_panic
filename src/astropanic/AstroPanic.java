/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astropanic;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Michal
 */
public class AstroPanic extends JFrame implements Runnable
{

    private int windowWidth = 700;
    private int windowHeight = 600;
    private int fps = 40;
    private int score = 0;
    private int maxScore=0;
    private int time_respawn = 1000; 
    private int levelLabelTime= 1500;
    private int current_time_respawn = time_respawn;    
    private int currentLevelLabelTime=levelLabelTime;
    private int level= 1;   
    private int enemiesOnLevel=5;
    private int enemiesActualCount = 0;
    private int enemiesKilledInLevel = 0;
    private float bloodingfloat = 1.0f;
    private float bloodBreathingfloat = 1.0f;
    
    
    
    private boolean collisionBulletOnEnemy = false;
    private boolean gameOver = false;
    private boolean levelLabel = false;
    private boolean levelDone=false;
    private boolean blooding = false;
    private boolean bloodBreathing = false;
    private boolean raiseBlooding = false;
    
    

    private BufferedImage img_background = null;
    private BufferedImage img_spaceship = null;
    private BufferedImage img_bullet = null;
    private BufferedImage img_enemy1 = null;
    private BufferedImage img_explosion = null;
    private BufferedImage img_frame = null;
    private BufferedImage img_healthBar = null;
    private BufferedImage img_bar_green = null;
    private BufferedImage img_bar_orange = null;
    private BufferedImage img_bar_red = null;
    private BufferedImage img_skull = null;
    private BufferedImage img_blood = null;    
    private BufferedImage img_explosionAnimation[] = null;
    private BufferedImage cursorImg;    
    private BufferedImage bf;
    
    private Cursor blankCursor;
    private Point mousePos;
    private JLabel lb_frameUpward;
    private Sounds sound;
    private Mainship ms;
    private File highScore = null;
    
    private FileInputStream inputScore = null;
    private FileOutputStream outputScore = null;

    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
    

   

    public static void main(String[] args)
    {
        {
            AstroPanic ap = new AstroPanic();
            ap.run();           
        }
    }

    @Override
    public void run()
    {
        
        try
        {
            this.initialize();
        }
        catch(IOException ex)
        {
            Logger.getLogger(AstroPanic.class.getName()).log(Level.SEVERE, null, ex);
        }     

        while(true)
        {
            long time = System.currentTimeMillis();
            time = (1000 / fps) - (System.currentTimeMillis() - time);
            
            try
            {
                Thread.sleep(time);
            }
            catch(Exception e)
            {
                
            }
            this.update(getGraphics());

        }

    }

    /**
     * This method will set up everything need for the game to run
     */
    void initialize() throws IOException
    {             
        
        this.setTitle("Astro Panic -  FAL0045");
        this.setSize(windowWidth, windowHeight);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);       
        
       
        loadResources();
        setCursor(blankCursor);
       
        
        
        lb_frameUpward = new JLabel(new ImageIcon(img_frame));
        lb_frameUpward.setLocation(0, -5);
        lb_frameUpward.setSize(this.windowWidth, 80);
        lb_frameUpward.setVisible(true);
        this.add(lb_frameUpward);
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent me)
            {
                if(gameOver==false)
                    bullets.add(new Bullet(windowWidth, windowHeight, ms.xPos + ms.sizeX/2- 10,lb_frameUpward.getHeight() + 20));
                    
            }
        });
        
        
        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(gameOver==true)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {   
                        enemies.removeAll(enemies);
                        bullets.removeAll(bullets);
                        explosions.removeAll(explosions);
                        score= 0;
                        maxScore = readHighestScoreFromStream();
                        ms = new Mainship(windowWidth, windowHeight);
                        time_respawn = 2000;
                        current_time_respawn = time_respawn;
                        gameOver = false;
                        enemiesOnLevel=5;
                        enemiesActualCount = 0;
                        enemiesKilledInLevel = 0;
                        level=1;
                        levelDone = false;
                        blooding = false;
                        bloodBreathing= false;
                        Enemy.resetSpeed();
                        
                    }
                }
            }
        });

    }

    public void loadResources() throws IOException
    {
        try
        {
            img_background = ImageIO.read(new File("resources/background.png"));
            img_spaceship = ImageIO.read(new File("resources/spaceship.png"));            
            img_enemy1 = ImageIO.read(new File("resources/enemy1.png"));
            img_bullet = ImageIO.read(new File("resources/bullet.png"));
            img_frame = ImageIO.read(new File("resources/frame.jpg"));
            img_healthBar = ImageIO.read(new File("resources/healthBar.png"));
            img_bar_green = ImageIO.read(new File("resources/bar_green.png"));
            img_bar_orange = ImageIO.read(new File("resources/bar_orange.png"));
            img_bar_red = ImageIO.read(new File("resources/bar_red.png"));
            img_skull= ImageIO.read(new File("resources/skull.png"));
            img_blood = ImageIO.read(new File("resources/blood.png"));          
            img_explosionAnimation = new BufferedImage[Explosion.framesCount];
            for(int i = 0; i < Explosion.framesCount; i++)
            {
                img_explosionAnimation[i] = ImageIO.read(new File("resources/explosion" + (i + 1) + ".png"));
            }
            sound = new Sounds();
            ms = new Mainship(this.windowWidth, this.windowHeight);           

        }
        catch(IOException e)
        {
            System.err.println("Nepodarilo sa načítať súbory: " + e.getLocalizedMessage());
        }

        
        maxScore = readHighestScoreFromStream();
        
        
        //uprava kurzoru
        cursorImg = ImageIO.read(new File("resources/aim_cursor.png"));
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

    }

    /**
     * This method will check for input, move things around and check for win
     * conditions, etc
     *
     * @param g
     */
    @Override
    public void update(Graphics g)
    {
      
        

        mousePos = this.getMousePosition();
        if(mousePos != null)
        {
            ms.setPosition(mousePos.x);
        }

        if(gameOver == false)
        {

            if(levelLabel == false)
            {
                //generovanie nepriatelov
                current_time_respawn = current_time_respawn - 1000 / fps;
                if(current_time_respawn < 0)
                {

                    current_time_respawn = time_respawn;
                    if(enemiesActualCount == enemiesOnLevel)
                    {
                           //vygenerovany dostatocny pocet nepriatelov
                    }
                    else
                    {
                        enemiesActualCount++;
                        enemies.add(new Enemy(windowWidth, windowHeight, lb_frameUpward.getHeight() + 20));
                    }

                }
            }
            
            
            if(levelLabel==true)
            {                
                currentLevelLabelTime = currentLevelLabelTime - 1000/fps;
                if(currentLevelLabelTime < 0)
                {
                    currentLevelLabelTime = levelLabelTime;
                    levelLabel = false;
                    levelDone = false;

                }
            }
        
            //pohyb nepriatelov
            for(Enemy en : enemies)
            {
                en.Move();
            }

            //pohyb striel
            for(Bullet b : new ArrayList<> (bullets))
            {
                b.Move();            
                if(b.isOutOfBount() || b.isExploded()==true)
                {
                    bullets.remove(b);
                    break;
                }
            }
           

            //kolizia striel z nepriatelmi
            try
            {
                for(Bullet b : bullets)
                {
                    Rectangle bulletRect = new Rectangle(b.xPos, b.yPos, b.sizeX, b.sizeY);

                    for(Enemy en : enemies)
                    {
                        Rectangle enemyRect = new Rectangle(en.xPos, en.yPos, en.sizeX, en.sizeY);
                        if(bulletRect.intersects(enemyRect))
                        {
                            score++;
                            enemiesKilledInLevel++;
                            sound.play();
                            enemies.remove(en);
                            explosions.add(new Explosion(en.xPos, en.yPos, en.sizeX));
                            collisionBulletOnEnemy = true;
                            break;
                        }
                        else
                        {
                            collisionBulletOnEnemy = false;
                        }

                    }
                    if(collisionBulletOnEnemy == true)
                    {

                        b.setExploded(true);
                        collisionBulletOnEnemy = false;
                        break;
                    }

                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            
            
            
            if(enemiesKilledInLevel==enemiesOnLevel)
            {
                levelDone= true;
                levelLabel= true;
                level++;
                Enemy.raiseSpeed();
                enemiesActualCount = 0;
                enemiesKilledInLevel=0;
                enemiesOnLevel +=5; 
                time_respawn -= 200;
                if(time_respawn <100)
                    time_respawn=100;
            }


            //detekcia nepriatelov s materkou lodou
            Rectangle msRect = new Rectangle(ms.xPos, ms.yPos, ms.sizeX, ms.sizeY);
            for(Enemy en : enemies)
            {
                Rectangle enemyRect = new Rectangle(en.xPos, en.yPos, en.sizeX, en.sizeY);
                if(msRect.intersects(enemyRect))
                {
                    sound.play();
                    ms.healthDown();
                    enemiesActualCount--;
                    if(blooding == true)
                    {
                        bloodingfloat = 1.0f;
                    }
                    blooding = true;

                    enemies.remove(en);
                    explosions.add(new Explosion(en.xPos, en.yPos, en.sizeX));
                    if(ms.getHealth() == 0)
                    {
                        gameOver = true;
                        if(this.score > this.maxScore)
                        {
                            writeHighestScoreToStream(this.score);
                        }

                    }
                    break;
                }
            }    
        }
        
        
        //pocitanie krvacajucej obrazovky
        if(blooding==true)
        {
            bloodingfloat -= 0.03f;
           
            if(bloodingfloat<0.05f)
            {
                    bloodingfloat = 1.0f;                    
                    blooding= false;                   
            }
        }
        
        if(ms.getHealth()<2)
            bloodBreathing=true;
        
        //pocitanie krvacajucej obrazovky
        if(bloodBreathing==true)
        {      
           
            if(raiseBlooding==false)
            {
                bloodBreathingfloat -= 0.015f;
                if(bloodBreathingfloat < 0.25f) 
                {
                    raiseBlooding = !raiseBlooding;
                }
            }
            else
            {
                bloodBreathingfloat += 0.015f;
                if(bloodBreathingfloat > 0.8f) 
                {
                    raiseBlooding = !raiseBlooding;
                }
            }
        }
        
        //zmena animacie vybuchu
        for(Explosion exp : explosions)
        {
            exp.changeFrame();
            if(exp.isEndOfAnimation() == true)
            {
                explosions.remove(exp);
                break;
            }
        }
        

        paint(g);
    }

    /**
     * This method will draw everything
     *
     * @param g
     */
    @Override
    public void paint(Graphics g)
    {
        bf = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        try
        {
            draw(bf.getGraphics());
            g.drawImage(bf, 0, 0, null);
        }
        catch(Exception ex)
        {

        }

    }
    
    
    
    

    void draw(Graphics g)
    {
        super.paint(g);

        g.drawImage(img_background, 0, lb_frameUpward.getHeight() + 20, windowWidth, windowHeight, this);        
        g.drawImage(img_spaceship, ms.xPos, ms.yPos, ms.sizeX, ms.sizeY, this); 
         

        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Najvyššie Skóre: " + maxScore, windowWidth - 225, 53);
        g.drawString("Skóre: " + score, windowWidth - 225, 78);
        g.drawString("LEVEL: " + level, windowWidth - 400, 70);
        
        
        
        
        g.drawImage(img_healthBar, 40, 50, this);
        if(ms.getHealth()>=10 && ms.getHealth()<=16)
            for(int i=0;i<ms.getHealth();i++)
            {
                g.drawImage(img_bar_green, 43+(i*10), 54 , this);
            }
        else if(ms.getHealth()>=5 && ms.getHealth()<=9)
            for(int i=0;i<ms.getHealth();i++)
            {
                g.drawImage(img_bar_orange, 43+(i*10), 54 , this);
            }
        else 
            for(int i=0;i<ms.getHealth();i++)
            {
                g.drawImage(img_bar_red, 43+(i*10), 54 , this);
            }
        
        if(levelLabel==true)
        {
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
             g.setColor(Color.WHITE);
            g.drawString("Misia splnená!"  , 250, 200);
        }

        
        
        for(Enemy en : enemies)
        {
            g.drawImage(img_enemy1, en.xPos, en.yPos, 40, 40, this);
        }

        for(Bullet b : bullets)
        {
            g.drawImage(img_bullet, b.xPos, b.yPos, b.sizeX, b.sizeY, this);
        }

        for(Explosion exp : explosions)
        {
            g.drawImage(img_explosionAnimation[exp.currentFrame], exp.xPos, exp.yPos, this);
        }
        
        if(gameOver==true)
        {
            g.drawImage(img_skull, 220,38, 45, 45, this);
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            g.setColor(Color.RED);
            g.drawString("GAME OVER!" , 160, 300);
            if(this.score> this.maxScore)
            {
                g.setFont(new Font("TimesRoman", Font.BOLD, 30));
                g.setColor(Color.WHITE);
                g.drawString("Nové najvyššie skóre: " + this.score , 175, 350);
            }
        }
        if(blooding == true)
        {
           
            Graphics2D g2d = (Graphics2D)g;           
            Composite original = g2d.getComposite();
            Composite translucent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bloodingfloat);
            g2d.setComposite(translucent);
            g2d.drawImage(img_blood, 0, 0, windowWidth, windowHeight, this);
            g2d.setComposite(original);
        }
        
        //dýchanie
        if(bloodBreathing)
        {
           
            Graphics2D g2d = (Graphics2D)g;           
            Composite original = g2d.getComposite();
            Composite translucent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bloodBreathingfloat);
            g2d.setComposite(translucent);
            g2d.drawImage(img_blood, 0, 0, windowWidth, windowHeight, this);
            g2d.setComposite(original);
        }
            

    }
    private byte[] toByteArray(int value)
    {
        return  ByteBuffer.allocate(4).putInt(value).array();
    }
    private int fromByteArray(byte[] bytes) 
    {
        return ByteBuffer.wrap(bytes).getInt();
    }
    
    
    
    
    private int readHighestScoreFromStream()
    {
        int _score = 0;
        try
        {
            highScore = new File("resources/highestScore.txt");
            highScore.createNewFile();
            inputScore = new FileInputStream(highScore);
            byte fileContent[] = new byte[(int)highScore.length()];
            inputScore.read(fileContent);
            _score = fromByteArray(fileContent);
        }
        catch(Exception ex)
        {
            
        }
        finally 
        {  
           // close the streams using close method
            try 
            {
                if (inputScore != null) 
                {
                    inputScore.close();
                }
            }
            catch (IOException ioe) 
            {
                System.out.println("Error while closing stream: " + ioe);
            }
            
            
        }
        return _score;
    }
    
    private void writeHighestScoreToStream(int newScore)
    {
        try
        {
            outputScore = new FileOutputStream(highScore, false);
            outputScore.write(toByteArray(score));
        }
        catch(IOException ex)
        {
            Logger.getLogger(AstroPanic.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if(outputScore != null)
            {
                try
                {
                    outputScore.close();
                }
                catch(IOException ex)
                {
                    Logger.getLogger(AstroPanic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
