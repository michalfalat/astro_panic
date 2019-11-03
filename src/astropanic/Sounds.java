/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
package astropanic;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Michal
 */
public class Sounds
{
    private Clip myClip;

    public Sounds()
    {
        try
        {
            File file = new File("resources/explode.wav");
            if(file.exists())
            {
                myClip = AudioSystem.getClip();
                AudioInputStream ais = AudioSystem.getAudioInputStream(file.toURI().toURL());
                myClip.open(ais);
            }
            else
            {
                throw new RuntimeException("Sound: file not found: ");
            }
        }
        catch(MalformedURLException e)
        {
            throw new RuntimeException("Sound: Malformed URL: " + e);
        }
        catch(UnsupportedAudioFileException e)
        {
            throw new RuntimeException("Sound: Unsupported Audio File: " + e);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Sound: Input/Output Error: " + e);
        }
        catch(LineUnavailableException e)
        {
            throw new RuntimeException("Sound: Line Unavailable: " + e);
        }
    }

    public void play()
    {
        myClip.setFramePosition(0);  // Must always rewind!
        myClip.loop(0);
        myClip.start();
    }

    public void loop()
    {
        myClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop()
    {
        myClip.stop();
    }
}
