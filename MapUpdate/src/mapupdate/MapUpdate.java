/*
 * Map Updater
   Allows for easy-update re-drawing of the ever-changing Bunns Village
   availabilities map.

USAGE: This tool is designed to take as arguments all AVAILABLE units in the
following format: mapupdate 11 13 21A 17B
 
 */
package mapupdate;

import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;


public class MapUpdate {


    public static void main(String ... args) {
        
        // Check for minimum number of arguments
        if (args.length <= 0) {
            System.out.println("MAP UPDATER:");
            System.out.println("EXAMPLE USAGE: java mapupdate 21A 13 1 15B");
            System.exit(1);
        }
        
        Updater updater = new Updater(args);
    }
    
}

class Updater {
    
    BufferedImage map = null;
    
    
    // UPDATER CONSTRUCTOR
    
    public Updater(String ... args) {
        
        map = loadMapImage();
        drawBuildings(args);
        saveMap();
        
    }
    
    // LOAD MAP IMAGE
    // Loads the "blank" map
    private BufferedImage loadMapImage() {
        
        BufferedImage return_image = null;
        InputStream fin = null;
        
        fin = getClass().getResourceAsStream("images/blankmap.png");
            
        if (fin == null)
        {
            System.out.println("ERROR: Updater cannot create inputStream into blankmap.png!");
            return null;
        }
       
        try {
            return_image = ImageIO.read(fin);
        }
        catch (IOException e)
        {
            System.out.println("ERROR: Updater cannot load blankmap.png!");
            System.exit(1);
        }
        
        return(return_image);
    }
    
    
    // DRAW BUILDINGS
    // Accepts the list of available buildings and draws them to the map
    private void drawBuildings(String ... args) {
        
        // Testing...
        for (String bldg: args) {
            System.out.println(bldg);
        }
        
    }
    
    
    // SAVE MAP
    // Handles the out-to-disk duties, saving the new map as "map.png"
    private void saveMap() {
        
        if (map == null) {
            System.out.println("ERROR: saveMap(): map is null!");
            return;
        }
        
        RenderedImage renderedMap = (RenderedImage)map;
        
        try {
            //OutputStream out = new FileOutputStream(new File("images/map.png"));
            File out = new File("map.png");
            ImageIO.write(renderedMap, "png", out);
            System.out.println("...map.png successfully written!");
        }
        catch (IOException e) {
            System.out.println("ERROR saveMap(): " + e.toString());
        }
        
    }
    
    
}
