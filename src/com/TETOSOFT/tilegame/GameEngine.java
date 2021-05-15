package com.TETOSOFT.tilegame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.TETOSOFT.graphics.*;
import com.TETOSOFT.input.*;
import com.TETOSOFT.test.GameCore;
import com.TETOSOFT.tilegame.sprites.*;



/**
 * GameManager manages all parts of the game.
 */
public class GameEngine extends GameCore
{
    
    public static void main(String[] args) 
    {
            new GameEngine().run();

    }
    
    public static final float GRAVITY = 0.002f;
    private Font police=new Font("Arial",Font.PLAIN,18);
    private Point pointCache = new Point();
    private TileMap map;
    private MapLoader mapLoader;
    private InputManager inputManager;
    private TileMapDrawer drawer;
    
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction jump;
    private GameAction exit;
    private GameAction pause;
    private boolean GameOver=false;
    private boolean IsHighScore=false;
    private int collectedStars=0;
    //number of badguys killed
    private int CreaturesKilled=0;
    
    private int CreatureCoefficient=0;
    //time couonter variable
    private long elapsedtime=0;
    
    private int Score=0;
    
    private int numLives=2;



    public void init()
    {
        super.init();
        
        // set up input manager
        initInput();
        
        // start resource manager
        mapLoader = new MapLoader(screen.getFullScreenWindow().getGraphicsConfiguration());
        
        // load resources
        drawer = new TileMapDrawer();
        drawer.setBackground(mapLoader.loadImage("background.jpg"));
        
        // load first map
        map = mapLoader.loadNextMap();
    }
    
    
    /**
     * Closes any resurces used by the GameManager.
     */
    public void stop() {
        super.stop();
        
    }
    /**
     * */
    public void pauseGame() {
    	super.pauseGame();
    }
    
    
    private void initInput() {
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        jump = new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
        exit = new GameAction("exit",GameAction.DETECT_INITAL_PRESS_ONLY);
        pause=new GameAction("pause",GameAction.DETECT_INITAL_PRESS_ONLY);
        
        inputManager = new InputManager(screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
        
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_Q);
        inputManager.mapToKey(pause, KeyEvent.VK_P);
        
    }
    
    
    private void checkInput(long elapsedTime) 
    {
        
        if (exit.isPressed()) {
            stop();
        }
        
        Player player = (Player)map.getPlayer();
        if (player.isAlive()) 
        {
            float velocityX = 0;
            /** if  p key is pressed it executes pauseGame() method and make a 'return'
             * to make sure that others inputs commands are not executed */
            if(pause.isPressed()) {
            	System.err.println("pause is pressed");
            	pauseGame();
            	return;
            }
            
            	
            
            if (moveLeft.isPressed()) 
            {
                velocityX-=player.getMaxSpeed();
            }
            if (moveRight.isPressed()) {
                velocityX+=player.getMaxSpeed();
            }
            if (jump.isPressed()) {
                player.jump(false);
            }
            
            player.setVelocityX(velocityX);
        
        }
        
    }
    
    
    public void draw(Graphics2D g) {
        
        
        drawer.draw(g, map, screen.getWidth(), screen.getHeight());
        
        g.setColor(Color.GREEN);
        g.drawString("Score: "+Score,300.0f,20.0f);
        g.setColor(Color.YELLOW);
        g.drawString("Lives: "+(numLives),500.0f,20.0f );
        g.setColor(Color.WHITE);
        g.drawString("Home: "+mapLoader.currentMap,700.0f,20.0f);
        g.setFont(police);
        //g.drawString("Badguys killed: "+CreaturesKilled, 460, 25);
        String time="";
        if((int)elapsedtime/1000>60) time=(int)elapsedtime/1000/60+"min "+(int)elapsedtime/1000%60+" sec";
        else time=(int)elapsedtime/1000+" sec";
        g.setColor(Color.YELLOW);
        g.drawString("Time: "+time,10.0f,20.0f);
        
        if(ispause) {
        	g.setColor(Color.WHITE);
        	g.drawString("Pause/Resume : Press 'P'",200.0f,235.0f);
        	g.drawString("Quit : Press 'Q'",200.0f,265.0f);
        	g.drawString("Music ON/OFF : Press 'M'",200.0f,295.0f);
        	g.drawString("!!!!!!!!!!!!!!!!!!!!!!!!",200.0f,325.0f);
        	g.setColor(Color.RED);
        	g.setFont(new Font("Arial",Font.BOLD,40));
            g.drawString("Game Paused",180.0f,180.0f);


            GameoverMenu menu=new GameoverMenu(this, IsHighScore, screen, Score, 0);
            menu.update();
            /*screen.frame.add(panel.mainPanel);
            screen.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            screen.frame.update(screen.getGraphics());*/


            // comment this screen.frame.getContentPane().remove(panel);

        }
        
        if(GameOver) {

        	g.setColor(Color.RED);
        	g.setFont(new Font("Arial", Font.BOLD,35));
            g.drawString("Game Over",220.0f,180.0f);
            if(!IsHighScore) {
            g.setColor(Color.RED);
            g.drawString("Your score: "+Score,220.0f,240.0f);
            }
            else {
            	g.setColor(Color.GREEN);
                g.drawImage(mapLoader.loadImage("Nrecord.jpg"), 230, 220, null);
            	g.drawString("New HighScore score: "+Score,200.0f,360.0f);
            }


            JPanel panel=new JPanel();
            panel.setBounds(40,80,screen.getWidth(),screen.getHeight());
            panel.setBackground(Color.gray);
            JButton b1=new JButton("Button 1");
            b1.setBounds(50,100,80,30);
            b1.setBackground(Color.yellow);
            JButton b2=new JButton("Button 2");
            b2.setBounds(100,100,80,30);
            b2.setBackground(Color.green);
            panel.add(b1); panel.add(b2);
            screen.frame.add(panel);



            screen.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            screen.frame.update(screen.getGraphics());




        }
        

        }   
    
    
    /**this method count the score of the player thanks to this formula  10%time+20%coins+70%creatures-killed */
    private long UpdateScore( int Startsnbr) {
    	
    	Score+= (int)((0.1)*elapsedtime/1000 +0.2*Startsnbr+0.7*CreatureCoefficient)/10;
    	return  Score;
    }
    
    
    /**
     * Gets the current map.
     */
    public TileMap getMap() {
        return map;
    }
    
    /**
     * Gets the tile that a Sprites collides with. Only the
     * Sprite's X or Y should be changed, not both. Returns null
     * if no collision is detected.
     */
    public Point getTileCollision(Sprite sprite, float newX, float newY) 
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);
        
        // get the tile locations
        int fromTileX = TileMapDrawer.pixelsToTiles(fromX);
        int fromTileY = TileMapDrawer.pixelsToTiles(fromY);
        int toTileX = TileMapDrawer.pixelsToTiles(
                toX + sprite.getWidth() - 1);
        int toTileY = TileMapDrawer.pixelsToTiles(
                toY + sprite.getHeight() - 1);
        
        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                        map.getTile(x, y) != null) {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }
        
        // no collision found
        return null;
    }
    
    
    /**
     * Checks if two Sprites collide with one another. Returns
     * false if the two Sprites are the same. Returns false if
     * one of the Sprites is a Creature that is not alive.
     */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }
        
        // if one of the Sprites is a dead Creature, return false
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false;
        }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }
        
        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());
        
        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
                s2x < s1x + s1.getWidth() &&
                s1y < s2y + s2.getHeight() &&
                s2y < s1y + s1.getHeight());
    }
    
    
    /**
     * Gets the Sprite that collides with the specified Sprite,
     * or null if no Sprite collides with the specified Sprite.
     */
    public Sprite getSpriteCollision(Sprite sprite) {
        
        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }
        
        // no collision found
        return null;
    }
    
    
    /**
     * Updates Animation, position, and velocity of all Sprites
     * in the current map.
     */
    public void update(long elapsedTime) {
        Creature player = (Creature)map.getPlayer();
        
        if(!ispause)   this.elapsedtime+=elapsedTime;
        
        
        
        
        // player is dead! start map over
        if (player.getState() == Creature.STATE_DEAD) {
            map = mapLoader.reloadMap();
            return;
        }
        
        // get keyboard/mouse input
        checkInput(elapsedTime);
        if(!ispause) {
        // update player
        updateCreature(player, elapsedTime);
        player.update(elapsedTime);
        
        // update other sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Creature) {
                Creature creature = (Creature)sprite;
                if (creature.getState() == Creature.STATE_DEAD) {
                    i.remove();
                } else {
                    updateCreature(creature, elapsedTime);
                }
            }
            // normal update
            sprite.update(elapsedTime);
        }
        }
    }
    
    
    /**
     * Updates the creature, applying gravity for creatures that
     * aren't flying, and checks collisions.
     */
    private void updateCreature(Creature creature,
            long elapsedTime) {
        
        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() +
                    GRAVITY * elapsedTime);
        }
        
        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
                getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        } else {
            // line up with the tile boundary
            if (dx > 0) {
                creature.setX(
                        TileMapDrawer.tilesToPixels(tile.x) -
                        creature.getWidth());
            } else if (dx < 0) {
                creature.setX(
                        TileMapDrawer.tilesToPixels(tile.x + 1));
            }
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature, false,elapsedTime);
        }
        
        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        } else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                        TileMapDrawer.tilesToPixels(tile.y) -
                        creature.getHeight());
            } else if (dy < 0) {
                creature.setY(
                        TileMapDrawer.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        if (creature instanceof Player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision((Player)creature, canKill,elapsedTime);
        }
        
    }
    
    
    /**
     * Checks for Player collision with other Sprites. If
     * canKill is true, collisions with Creatures will kill
     * them.
     */
    public void checkPlayerCollision(Player player,
            boolean canKill,long elapsedTime) {
        if (!player.isAlive()) {
            return;
        }
        
        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
            UpdateScore(50);
        } else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            if( badguy instanceof Fly ) {
            	//System.out.println("9atele faracha");
            	this.CreatureCoefficient=150;
            }
            else if(badguy instanceof Grub) {
            	//System.out.println("9atele doudaa");
            	this.CreatureCoefficient=100;
            }
            if (canKill) {
                // kill the badguy and make player bounce
                badguy.setState(Creature.STATE_DYING);
                player.setY(badguy.getY() - player.getHeight());
                player.jump(true);
                CreaturesKilled++;
                
                UpdateScore(0);
            } else {
                // player dies!
                player.setState(Creature.STATE_DYING);
                numLives--;
                if(numLives==0) {
                	GameOver=true;
                	IsHighScore=UpdateHighScoreList(Score);
                	System.out.println(IsHighScore);
                	draw(screen.getGraphics());
                	screen.update();
                    /*try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }*/
                    
                    
                    try {
            			Thread.sleep(1000);
            		} catch (InterruptedException e) {	e.printStackTrace(); }
                	finally {
                		 stop();
					}
                   
                    
                    
                    
                }
            }
        }
    }
    
   /* private void DisplayHighscoreFrame() {
    	System.out.println("inside display");
    	 JFrame highscoreframe=new JFrame();
     	JPanel highscorepanel=(JPanel)highscoreframe.getContentPane();
     	highscoreframe.setSize(300,300);
 		highscoreframe.setLocationRelativeTo(null);
     	highscoreframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     	highscorepanel.setLayout(new BorderLayout());
     	Label highscoretxt=new Label("Ur score is : "+Score);
     	highscorepanel.add(highscoretxt);
     	highscoreframe.setVisible(true);
    	
    	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) { 	e.printStackTrace();}
        finally{
            highscoreframe.setVisible(false);
            stop();
        }
    	
    	
    }*/
  
    
    
    /**
     * Gives the player the speicifed power up and removes it
     * from the map.
     */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);
        
        if (powerUp instanceof PowerUp.Star) {
            // do something here, like give the player points
            collectedStars++;
            if(collectedStars==100) 
            {
                numLives++;
                collectedStars=0;
            }
            
        } else if (powerUp instanceof PowerUp.Music) {
            // change the music
            
        } else if (powerUp instanceof PowerUp.Goal) {
            // advance to next map      
      
            map = mapLoader.loadNextMap();
            
        }
    }
    
      
}