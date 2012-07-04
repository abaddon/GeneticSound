/*
 * UpdateInfoThread.java
 *
 * Created on 27 settembre 2007, 0.31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geneticsound;

import soundGenetic.jgap.JGapSoundGenetic;

/**
 * @author   abaddon
 */
public class UpdateInfoThread extends Thread{
    private JGapSoundGenetic genetic=null;
    private MainFrame frame=null;
    public boolean isActive=true;
    private Thread jgap=null;
    
    /** Creates a new instance of UpdateInfoThread */
    public UpdateInfoThread(JGapSoundGenetic genetic,MainFrame frame,Thread jgap) {
       this.genetic=genetic;
       this.frame=frame;
       this.jgap=jgap;
    }
    
    public void run() {
        while(isActive){
            if(genetic.getFitnessValue()!=null){
                frame.updateInfo(genetic.getFitnessValue());
            }
//            if(!threadJGap.isAlive())
//                isActive=false;
            try {
                sleep(100);
                if(!jgap.isAlive()){
                    isActive=false;
                    sleep(200);
                    frame.updateInfo(genetic.getFitnessValue());
                    frame.StopSoundGenetic();
                    
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
        if(!isActive)
            System.out.println("Thread Spento");
            
    }
    
}
