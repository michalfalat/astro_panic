/*
 * To change this licthisse header, choose Licthisse Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and opthis the template in the editor.
 */
package astropanic;

import java.util.Random;

/**
 *
 * @author Michal
 */
public class Enemy extends Object
{
    
   
    private int windowWidth;
    private int windowHeight;
    private int defultYPos;
    private boolean initialize ;
    private static int maxSpeed = 15;
   
    private Random rand = new Random();
    
    public Enemy(int wW,int wH, int defaultYpos)
    {
        this.initialize = true;        
        this.sizeX=40;
        this.sizeY=40;
        this.windowWidth=wW;
        this.windowHeight=wH;
        this.xPos = rand.nextInt(windowWidth-sizeX) ;
        this.yPos = 0;
        this.defultYPos = defaultYpos;
        this.xSpeed = rand.nextInt(maxSpeed) -(maxSpeed/2);
        this.ySpeed = rand.nextInt(maxSpeed) -(maxSpeed/2);
        if(xSpeed==0)
        {
            xSpeed=2;
        }
        if(ySpeed==0)
        {
            ySpeed=2;
        }
    }
    public static void raiseSpeed()
    {
        maxSpeed +=5;
    }
    public static void resetSpeed()
    {
        maxSpeed =15;
    }
    
    public void Move()
    {
        this.xPos += this.xSpeed;
        this.yPos += this.ySpeed;
        
        if(initialize==false)
        {
        
            if(this.xPos < 0)
            {
                this.xSpeed = rand.nextInt((maxSpeed / 2));
            }       
            if(this.yPos <  defultYPos)
            {
                this.ySpeed = rand.nextInt((maxSpeed / 2));
            }
            if(this.xPos > this.windowWidth - sizeX)
            {
                this.xSpeed = -rand.nextInt((maxSpeed / 2));
            }
            if(this.yPos > this.windowHeight - sizeY)
            {
                this.ySpeed = -rand.nextInt((maxSpeed / 2 - 3) + 3);
            }
        }
        else
        {
            this.ySpeed=7;
            this.xSpeed = 0;
            if(this.yPos>defultYPos+10)
            {
                xSpeed = rand.nextInt(maxSpeed) -(maxSpeed/2);
                ySpeed = rand.nextInt((maxSpeed / 2));
                if(xSpeed==0)
                {
                    xSpeed=2;
                }
                if(ySpeed==0)
                {
                    ySpeed=2;
                }
                initialize = false;
            }
        }
            

    }
}
