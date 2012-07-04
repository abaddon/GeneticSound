/*
 * SoundGeneticThread.java
 *
 * Created on 20 settembre 2007, 11.01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geneticsound;

import soundGenetic.jgap.JGapSoundGenetic;

/**
 *
 * @author abaddon
 */
public class SoundGeneticThread extends Thread{
    private int[] pitchsResult=null;
    private JGapSoundGenetic genetic=null;
   

    
    /** Creates a new instance of SoundGeneticThread */
    public SoundGeneticThread(JGapSoundGenetic genetic) {
        this.genetic=genetic;
        
    }
    
    @Override
    public void run() {
                pitchsResult=genetic.start();
                
    }
    
    public int[] getPitchResult(){
        return this.pitchsResult;
    }
    
}
