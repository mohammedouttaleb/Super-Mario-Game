package com.TETOSOFT.test;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.TETOSOFT.graphics.ScreenManager;

/**
    Simple abstract class used for testing. Subclasses should
    implement the draw() method.
*/
public abstract class GameCore {

    protected static final int FONT_SIZE = 18;

    private static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 24, 0),
    };

    private boolean isRunning;
    protected boolean ispause=false;
    protected ScreenManager screen;
    private File highscorefile=new File("highscore.txt");
    private int[] highscorelist= {-1,-2,-3,-4,-5};
    
    
   
    
    
     
  

    /**
        Signals the game loop that it's time to quit
    */
    public void stop() {
        isRunning = false;
    }
    
    /**
     * this method make pause to the game
     * */
    public void pauseGame() {
    	ispause= !ispause;
    	
    	
    }


    /**
        Calls init() and gameLoop()
    */
    public void run() {
        try {
        	highScoreConfig();
            init();
            gameLoop();
        }
        finally {
            screen.restoreScreen();
            lazilyExit();
        }
    }


    /**
        Exits the VM from a daemon thread. The daemon thread waits
        2 seconds then calls System.exit(0). Since the VM should
        exit when only daemon threads are running, this makes sure
        System.exit(0) is only called if neccesary. It's neccesary
        if the Java Sound system is running.
    */
    public void lazilyExit() {
        Thread thread = new Thread() {
            public void run() {
                // first, wait for the VM exit on its own.
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex) { }
                // system is still running, so force an exit
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }


    /**
        Sets full screen mode and initiates and objects.
    */
    public void init() 
    {
        screen = new ScreenManager();
        DisplayMode displayMode =
        screen.findFirstCompatibleMode(POSSIBLE_MODES);
        screen.setFullScreen(displayMode);

        Window window = screen.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        window.setBackground(Color.BLACK);
        window.setForeground(Color.WHITE);

        isRunning = true;
    }
    
    /*** this method creates the high score file if it does'nt exists and 
         serialize the table of high scores into it*/
    private void highScoreConfig() {
    	
    	if(!highscorefile.exists()) {
    		try {
				highscorefile.createNewFile();
				FileOutputStream fos=new FileOutputStream(highscorefile);
				ObjectOutputStream oos=new ObjectOutputStream(fos);
				oos.writeObject(highscorelist);
				System.out.println("file created with success");
				
			} 
    		catch (Exception e) {e.printStackTrace();}
    	}
    	else {
    		System.out.println("file already exists");
    		
    		
    	}
    }
    
    /**
     * this method updates HighScores file if the score is sup then the minimum of Highscore_Table
     * @param Score :is the player score at the end of the game
     * **/
    
    public boolean UpdateHighScoreList( int Score) {
    	
    	boolean ishighscore=false;
    	
    	try {
			FileInputStream fis=new FileInputStream(highscorefile);
			ObjectInputStream ois=new ObjectInputStream(fis);
			
			highscorelist=(int[])ois.readObject();
			int min=highscorelist[0];
			int i,j=0;
			
			
			 if(!scorexists(Score)) {
				 
					 for (i=0; i < highscorelist.length; i++) {
						if(highscorelist[i]<min) {
							min=highscorelist[i];
							j=i;
						}
					}
					 System.out.println("i: "+j);
				 if(j<highscorelist.length &&  highscorelist[j]<Score) {
					 highscorelist[j]=Score;
					ishighscore=true;
				 }
				 else if( highscorelist[0]<Score) {
					 highscorelist[0]=Score;
					 ishighscore=true;
				 }
				 
			 }
			 for (i=0; i < highscorelist.length; i++) {
					System.out.println(highscorelist[i]);
				}
			 
			 FileOutputStream fos=new FileOutputStream(highscorefile);
				ObjectOutputStream oos=new ObjectOutputStream(fos);
				oos.writeObject(highscorelist);
				
				return ishighscore;
				
		}
    	catch (Exception e) {	e.printStackTrace();return ishighscore; }
    }
    
    private boolean scorexists(int number) {
    	for (int i = 0; i < highscorelist.length; i++) {
			if(highscorelist[i]==number) {
				return true;
			}
		}
    	return false;
    }


    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }


    /**
        Runs through the game loop until stop() is called.
    */
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (isRunning) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;
            
            

            // update
            update(elapsedTime);

            // draw the screen
            Graphics2D g = screen.getGraphics();
           
            draw(g);
            g.dispose();
            
             screen.update();
            
            

            // don't take a nap! run as fast as possible
            /*try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }*/
        }
    }


    /**
        Updates the state of the game/animation based on the
        amount of elapsed time that has passed.
    */
    public void update(long elapsedTime) {
        // do nothing
    }


    /**
        Draws to the screen. Subclasses must override this
        method.
    */
    public abstract void draw(Graphics2D g);
}
