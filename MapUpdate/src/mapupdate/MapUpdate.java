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
import java.util.*;

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
    HashMap hash;
    
    // UPDATER CONSTRUCTOR
    
    public Updater(String ... args) {
        
        hash = new <String, Polygon>HashMap();
        populateHashmap();
        
        map = loadMapImage();
        drawBuildings(args);
        saveMap();
        
    }
    
    // POPULATE HASHMAP
    // Creates a total listing of the buildings and their positions within the image.
    private void populateHashmap() {
       
        Polygon poly;
        
        // #26
        poly = new Polygon();
        poly.addPoint(109,113);
        poly.addPoint(152,113);
        poly.addPoint(152,134);
        poly.addPoint(109,134);
        hash.put("26", poly);
        
        // #25
        poly = new Polygon();
        poly.addPoint(130,137);
        poly.addPoint(150,137);
        poly.addPoint(150,170);
        poly.addPoint(130,170);
        hash.put("25", poly);
        
        // #24
        poly = new Polygon();
        poly.addPoint(180,136);
        poly.addPoint(200,136);
        poly.addPoint(200,166);
        poly.addPoint(180,166);
        hash.put("24", poly);
        
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
        
        // Remove all of the "occupied" buildings from the hash map
        System.out.println("TOTAL BUILDINGS: " + hash.size());
        
        for (String bldg: args) {
            System.out.println("removing " + bldg);
            hash.remove(bldg);
        }

        // Setup the graphics context
        Graphics2D g2 = map.createGraphics();
        g2.setComposite(AlphaComposite.SrcOver.derive(0.3f));
        g2.setColor(Color.ORANGE);
        
        // Begin drawing over the sequence of occupied buildings
        Polygon p;
        Set<Map.Entry<String, Polygon>> set = hash.entrySet();

        for (Map.Entry<String, Polygon> me: set) {
            System.out.println("drawing " + me.getKey());
            p = (Polygon)me.getValue();
            g2.fillPolygon(p);
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
            System.out.println("SUCCESS!");
        }
        catch (IOException e) {
            System.out.println("ERROR saveMap(): " + e.toString());
        }
        
    }
    
    
}
