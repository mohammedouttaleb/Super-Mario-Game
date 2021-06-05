package com.TETOSOFT.tilegame.tilegame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import com.TETOSOFT.tilegame.graphics.Sprite;
import com.TETOSOFT.tilegame.tilegame.sprites.Grub;


/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class MapLoader 
{
    private ArrayList tiles;
    public int currentMap;
    private GraphicsConfiguration gc;

    // host sprites used for cloning
    private com.TETOSOFT.tilegame.graphics.Sprite playerSprite;
    private com.TETOSOFT.tilegame.graphics.Sprite musicSprite;
    private com.TETOSOFT.tilegame.graphics.Sprite coinSprite;
    private com.TETOSOFT.tilegame.graphics.Sprite goalSprite;
    private com.TETOSOFT.tilegame.graphics.Sprite grubSprite;
    private com.TETOSOFT.tilegame.graphics.Sprite flySprite;

    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */
    public MapLoader(GraphicsConfiguration gc) 
    {
        this.gc = gc;
        loadTileImages();
        loadCreatureSprites();
        loadPowerUpSprites();
    }


    /**
        Gets an image from the images/ directory.
    */
    public Image loadImage(String name) 
    {
        String filename = "images/" + name;
        return new ImageIcon(filename).getImage();
    }


    public Image getMirrorImage(Image image) 
    {
        return getScaledImage(image, -1, 1);
    }


    public Image getFlippedImage(Image image) 
    {
        return getScaledImage(image, 1, -1);
    }


    private Image getScaledImage(Image image, float x, float y) 
    {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }


    public com.TETOSOFT.tilegame.tilegame.TileMap loadNextMap()
    {
        com.TETOSOFT.tilegame.tilegame.TileMap map = null;
        while (map == null) 
        {
            currentMap++;
            try {
                map = loadMap(
                    "maps/map" + currentMap + ".txt");
            }
            catch (IOException ex) 
            {
                if (currentMap == 2) 
                {
                    // no maps to load!
                    return null;
                }
                  currentMap = 0;
                map = null;
            }
        }

        return map;
    }


    public com.TETOSOFT.tilegame.tilegame.TileMap reloadMap()
    {
        try {
            return loadMap(
                "maps/map" + currentMap + ".txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private com.TETOSOFT.tilegame.tilegame.TileMap loadMap(String filename)
        throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        com.TETOSOFT.tilegame.tilegame.TileMap newMap = new com.TETOSOFT.tilegame.tilegame.TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, coinSprite, x, y);
                }
                else if (ch == '!') {
                    addSprite(newMap, musicSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, goalSprite, x, y);
                }
                else if (ch == '1') {
                    addSprite(newMap, grubSprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, flySprite, x, y);
                }
            }
        }

        // add the player to the map
        com.TETOSOFT.tilegame.graphics.Sprite player = (com.TETOSOFT.tilegame.graphics.Sprite)playerSprite.clone();
        player.setX(com.TETOSOFT.tilegame.tilegame.TileMapDrawer.tilesToPixels(3));
        player.setY(lines.size());
        newMap.setPlayer(player);

        return newMap;
    }


    private void addSprite(TileMap map,
						   com.TETOSOFT.tilegame.graphics.Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            com.TETOSOFT.tilegame.graphics.Sprite sprite = (Sprite)hostSprite.clone();

            // center the sprite
            sprite.setX(
                com.TETOSOFT.tilegame.tilegame.TileMapDrawer.tilesToPixels(tileX) +
                (com.TETOSOFT.tilegame.tilegame.TileMapDrawer.tilesToPixels(1) -
                sprite.getWidth()) / 2);

            // bottom-justify the sprite
            sprite.setY(
                TileMapDrawer.tilesToPixels(tileY + 1) -
                sprite.getHeight());

            // add it to the map
            map.addSprite(sprite);
        }
    }


    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------


    public void loadTileImages()
    {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        tiles = new ArrayList();
        char ch = 'A';
        
        while (true) 
        {
            String name = ch + ".png";
            File file = new File("images/" + name);
            if (!file.exists()) 
                break;
            
            tiles.add(loadImage(name));
            ch++;
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void loadCreatureSprites() 
    {

        Image[][] images = new Image[4][];

        // load left-facing images
        images[0] = new Image[] {
            loadImage("player.png"),         
            loadImage("fly1.png"),
            loadImage("fly2.png"),
            loadImage("fly3.png"),
            loadImage("grub1.png"),
            loadImage("grub2.png"),
        };

        images[1] = new Image[images[0].length];
        images[2] = new Image[images[0].length];
        images[3] = new Image[images[0].length];
        
        for (int i=0; i<images[0].length; i++) 
        {
            // right-facing images
            images[1][i] = getMirrorImage(images[0][i]);
            // left-facing "dead" images
            images[2][i] = getFlippedImage(images[0][i]);
            // right-facing "dead" images
            images[3][i] = getFlippedImage(images[1][i]);
        }

        // create creature animations
        com.TETOSOFT.tilegame.graphics.Animation[] playerAnim = new com.TETOSOFT.tilegame.graphics.Animation[4];
        com.TETOSOFT.tilegame.graphics.Animation[] flyAnim = new com.TETOSOFT.tilegame.graphics.Animation[4];
        com.TETOSOFT.tilegame.graphics.Animation[] grubAnim = new com.TETOSOFT.tilegame.graphics.Animation[4];
        
        for (int i=0; i<4; i++) 
        {
            playerAnim[i] = createPlayerAnim (images[i][0]);
            flyAnim[i] = createFlyAnim (images[i][1], images[i][1], images[i][3]);
            grubAnim[i] = createGrubAnim (images[i][4], images[i][5]);
        }

        // create creature sprites
        playerSprite = new com.TETOSOFT.tilegame.tilegame.sprites.Player(playerAnim[0], playerAnim[1],playerAnim[2], playerAnim[3]);
        flySprite = new com.TETOSOFT.tilegame.tilegame.sprites.Fly(flyAnim[0], flyAnim[1],flyAnim[2], flyAnim[3]);
        grubSprite = new Grub(grubAnim[0], grubAnim[1],grubAnim[2], grubAnim[3]);
    }


    private com.TETOSOFT.tilegame.graphics.Animation createPlayerAnim(Image player)
    {
        com.TETOSOFT.tilegame.graphics.Animation anim = new com.TETOSOFT.tilegame.graphics.Animation();
        anim.addFrame(player, 250);
     
        return anim;
    }


    private com.TETOSOFT.tilegame.graphics.Animation createFlyAnim(Image img1, Image img2, Image img3)
    {
        com.TETOSOFT.tilegame.graphics.Animation anim = new com.TETOSOFT.tilegame.graphics.Animation();
        anim.addFrame(img1, 50);
        anim.addFrame(img2, 50);
        anim.addFrame(img3, 50);
        anim.addFrame(img2, 50);
        return anim;
    }


    private com.TETOSOFT.tilegame.graphics.Animation createGrubAnim(Image img1, Image img2)
    {
        com.TETOSOFT.tilegame.graphics.Animation anim = new com.TETOSOFT.tilegame.graphics.Animation();
        anim.addFrame(img1, 250);
        anim.addFrame(img2, 250);
        return anim;
    }


    private void loadPowerUpSprites() 
    {
        // create "goal" sprite
        com.TETOSOFT.tilegame.graphics.Animation anim = new com.TETOSOFT.tilegame.graphics.Animation();
        anim.addFrame(loadImage("heart.png"), 150);
        goalSprite = new com.TETOSOFT.tilegame.tilegame.sprites.PowerUp.Goal(anim);

        // create "star" sprite
        anim = new com.TETOSOFT.tilegame.graphics.Animation();
        anim.addFrame(loadImage("coin1.png"),250 ) ;  
        anim.addFrame(loadImage("coin2.png"),250);
        anim.addFrame(loadImage("coin3.png"),250);
        anim.addFrame(loadImage("coin4.png"),250);
        anim.addFrame(loadImage("coin5.png"),250);
        coinSprite = new com.TETOSOFT.tilegame.tilegame.sprites.PowerUp.Star(anim);

        // create "music" sprite
        anim = new com.TETOSOFT.tilegame.graphics.Animation();
        anim.addFrame(loadImage("music1.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        anim.addFrame(loadImage("music3.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        musicSprite = new com.TETOSOFT.tilegame.tilegame.sprites.PowerUp.Music(anim);
        musicSprite=new com.TETOSOFT.tilegame.tilegame.sprites.PowerUp.Music(anim);
    }

}
