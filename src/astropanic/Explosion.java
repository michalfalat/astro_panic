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
public class Explosion extends Object
{
   
    public int currentFrame;
    public static  int framesCount=12;
    
    
    public Explosion(int xPos, int yPos, int size)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.sizeX = size;
        this.sizeY = size;
        currentFrame=0;
        
    }
    
    public void changeFrame()
    {
        currentFrame++;
    }

    public boolean isEndOfAnimation()
    {
        if(currentFrame==framesCount)
            return true;
        else
            return false;
    }
        
    
    
}
