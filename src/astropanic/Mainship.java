/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astropanic;

/**
 *
 * @author Michal
 */
public class Mainship extends Object
{
   
    private int maxSpeed;
    private int speed;
    private int windowWidth;
    private int windowHeight;
    private int health;

   
   
    
    public Mainship(int wW, int wH)
    {
        this.maxSpeed=30;
        this.speed=-20;
        this.windowHeight=wH;
        this.windowWidth=wW;
        this.yPos = this.windowHeight-85;
        this.xPos = this.windowWidth/2;
        this.sizeX=50;
        this.sizeY=80;
        this.health=16;
    }
    public void setPosition(int x)
    {
        this.xPos = x;
        if(x>this.windowWidth-this.sizeX)
            this.xPos=this.windowWidth-this.sizeX;
    }
    
    public int getMaxSpeed()
    {
        return maxSpeed;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getHealth()
    {
        return health;
    }
    
    public void healthDown()
    {
        this.health--;
    }
    
}
