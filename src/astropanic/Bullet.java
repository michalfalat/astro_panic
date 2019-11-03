/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astropanic;

import java.util.Random;

/**
 *
 * @author Michal
 */
public class Bullet extends Object
{       
 
    private int speed;
    private int windowWidth;
    private int windowHeight;
    private int defaultYPos;
    private boolean exploded = false;

    

    
    
    
    
    public Bullet(int wW,int wH, int xPos,  int defaultYPos)
    {
       
        this.speed=-20;
        this.sizeX=15;
        this.sizeY=47;
        this.windowWidth=wW;
        this.windowHeight=wH;
        this.xPos = xPos;
        this.defaultYPos = defaultYPos;
        this.yPos = this.windowHeight-sizeY-80;
        this.xSpeed =0;
        this.ySpeed=speed;
        
      
    }
    
    public void Move()
    {
        this.xPos += this.xSpeed;
        this.yPos += this.ySpeed;       
    }
    
    public boolean isOutOfBount()
    {
        if(this.xPos < 0 )
        {
            return true;
        }
        if(this.yPos <  defaultYPos)
        {
            return true;
        }
        if(this.xPos > this.windowWidth - sizeX)
        {
            return true;
        }
        if(this.yPos > this.windowHeight - sizeY)
        {
            return true;
        }
        return false;
    }
    
    public boolean isExploded()
    {
        return exploded;
    }
    public void setExploded(boolean exploded)
    {
        this.exploded = exploded;
    }
}
