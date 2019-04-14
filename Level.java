import java.awt.Color;
import java.util.concurrent.TimeUnit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
//ROTATION MEMORY:
//roverG2D.rotate(Math.toRadians(90), rover.x + rover.width/2, rover.y + rover.height/2);        
public class Level extends JPanel {
    //Tile images
    private BufferedImage surfaceImg;
    private BufferedImage roverImg;
    private BufferedImage rockImg;
    private BufferedImage mineralImg;
    private BufferedImage targetImg;
    private BufferedImage warningImg;
    //Tile paints - TODO: Create dynamically?
    private TexturePaint  surfacetp;
    private TexturePaint  rovertp;
    private TexturePaint  rocktp;
    private TexturePaint  mineraltp;
    private TexturePaint  targettp;
    private TexturePaint  warningtp;
    
    public  static int MAX_ROWS    = 15;
    public  static int MAX_COLUMNS = 15;    
    private static int TILE_SIZE   = 50;    
    
    Rectangle tile = new Rectangle(0,0,TILE_SIZE,TILE_SIZE);  

    private int roverX = 0;
    private int roverY = 0;
    
    private int iFrame;
    
    private int[][] tilemap = new int[MAX_ROWS][MAX_COLUMNS];
    
    private Rover rover = Rover.getRover();
    
    private ArrayList<Point> frames = new ArrayList<Point>();
    
    private static Level levelSingleton;
    /**
     * Factory method to get the canvas singleton object.
     */
    public static Level getLevel()
    {
        if(levelSingleton == null) {
            levelSingleton = new Level();
        }
        levelSingleton.setVisible(true);
        return levelSingleton;
    }
    
    public Level()
    {
        //Initialise Tile Types
        try 
        {
            //Store Buffered Images
            surfaceImg = ImageIO.read(new File("surface.jpg"));
            roverImg   = ImageIO.read(new File("rover.png"));
            rockImg    = ImageIO.read(new File("rock.png"));
            mineralImg = ImageIO.read(new File("mineral.png"));
            targetImg  = ImageIO.read(new File("target.png"));
            warningImg = ImageIO.read(new File("warning.png"));
            //Create Texture Paints
            surfacetp  = new TexturePaint(surfaceImg, tile);                 
            rovertp    = new TexturePaint(roverImg,   tile);
            rocktp     = new TexturePaint(rockImg,    tile);        
            mineraltp  = new TexturePaint(mineralImg, tile);
            targettp   = new TexturePaint(targetImg,  tile);
            warningtp  = new TexturePaint(warningImg, tile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void doDrawing(Graphics g)    
    {   
        //Surface specific G2D is used to paint entire 
        //  grid regardless of tilemap data to provide 
        //  background for tiles with transparent areas
        Graphics2D surfaceG2D = (Graphics2D) g.create();
        surfaceG2D.setPaint(surfacetp);
        /*for(int i = 0; i < MAX_ROWS; i++)
        {
            for(int j = 0; j < MAX_COLUMNS; j++)
            {
                surfaceG2D.fillRect(i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }*/
        
        //Singular generic G2D is used for tilemap data
        Graphics2D G2D = (Graphics2D) g.create();
        
        //Paint relevant to tilemap
        for(int y = 0; y < MAX_ROWS; y++)
        {
            for(int x = 0; x < MAX_COLUMNS; x++)
            {
                //Switch out paint type
                switch(tilemap[x][y])
                {
                    case 0: //Surface
                        G2D.setPaint(surfacetp);
                        break;
                    case 1: //Rover
                        G2D.setPaint(rovertp);
                        roverX = x;
                        roverY = y;
                        break;
                    case 2: //Rock
                        G2D.setPaint(rocktp);
                        break;
                    case 3: //Mineral
                        G2D.setPaint(mineraltp);
                        break; 
                    case 4: //Target
                        G2D.setPaint(targettp);
                        break;
                    case 5: //Warning
                        G2D.setPaint(warningtp);
                        break;
                    default://Safety - shouldn't happen
                        G2D.setPaint(surfacetp);
                        break;
                }
                //Apply paint to current tile
                G2D.fillRect(x*TILE_SIZE, y*TILE_SIZE, //Coordinates
                                 TILE_SIZE, TILE_SIZE);    //Dimensions
            }
        }    
    }
    
    public void addFrame(Point newFrame)
    {
        frames.add(newFrame);
    }
    
    public void updateRover()
    {
        if(iFrame >= frames.size())
        {
            Rover.getRover().finished();
        }
        else
        {
            //Get new position
            roverX += frames.get(iFrame).x;
            roverY += frames.get(iFrame).y;
            
            //Update tilemap
            //First, remove current rover position
            for(int y = 0; y < MAX_ROWS; y++)
            {
                for(int x = 0; x < MAX_COLUMNS; x++)
                {
                    if(tilemap[x][y] == 1)
                    {
                        tilemap[x][y] = 0;
                    }
                }
            }
            
            //Check what exists on tile rover is moving into            
            switch(tilemap[roverX][roverY])
            {
                case 0: //Surface
                    tilemap[roverX][roverY] = 1;
                    break;
                case 1: //Rover - Safety, shouldn't happen
                    tilemap[roverX][roverY] = 1;
                    break;
                case 2: //Rock
                    System.out.println("CRASH DETECTED");
                    tilemap[roverX][roverY] = 4; //Draw warning
                    break;
                case 3: //Mineral
                    System.out.println("CRASH DETECTED");
                    tilemap[roverX][roverY] = 4; //Draw warning
                    break; 
                case 4: //Target
                    System.out.println("REACHED!");
                    tilemap[roverX][roverY] = 1;
                    break;
                case 5: //Warning - Safety, shouldn't happen
                    tilemap[roverX][roverY] = 4;
                    break;
                default://Safety - shouldn't happen
                    tilemap[roverX][roverY] = 1;
                    break;
            }
                
            iFrame++;  //inc framecounter
            repaint(); //repaint level
        }        
        System.out.println("UPDATED: " + roverX + ", " + roverY);
    }
    
    /**
     * Loads tilemap for level from .txt file
     * 
     * Scans each tile in a row, per row (left-right, top-bottom)
     * This should not need any future modification if the TILE_SIZE changes,
     * nor if new tile types or level are added. This would only need to be 
     * modified should the level file format change.
     * 
     * @param fileToLoad The level file containing the tilemap data to load
     */
    
    public void loadTilemap(File fileToLoad) 
    {
        File levelFile = fileToLoad;
        try
        {
            Scanner scanner = new Scanner(levelFile);
            //Strict capture, instead of hasNextLine()
            // to enforce level grid size
            //Collect data for each column in row, then go to next row
            // 0 = surface, 1 = rover, 2 = rock, 3 = mineral
            for(int y = 0; y < Level.MAX_ROWS; y++)
            {
                for(int x = 0; x < Level.MAX_COLUMNS; x++)
                {
                    tilemap[x][y] = scanner.nextInt();
                }
            }
            scanner.close();
            repaint();
        }
        catch (FileNotFoundException e) 
        {
            System.out.println("Invalid Level File");
            e.printStackTrace();
        }
    }
    
    /**
     * Loads the new level, from button press
     * 
     * This is not as dynamic as I'd like it to be, so any future
     * added levels will need to be hardcoded below. For 10-15 levels,
     * this is fine, but beyond that it could get messy.
     * 
     * @param level 
     */    
    public void loadLevel(int level)
    {
        switch(level)
        {
            case 0:
                loadTilemap(new File("levelzero.txt"));
                break;
            case 1:
                loadTilemap(new File("levelone.txt"));
                break;
            case 2:
                loadTilemap(new File("leveltwo.txt"));
                break;
            case 3:
                loadTilemap(new File("levelthree.txt"));
                break;
            case 4:
                loadTilemap(new File("levelfour.txt"));
                break;
            case 5:
                loadTilemap(new File("levelfive.txt"));
                break;
            default:
                System.out.println("Loading failed: Invalid level");
                break;
        }
    }
    
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        doDrawing(g);
    }
}