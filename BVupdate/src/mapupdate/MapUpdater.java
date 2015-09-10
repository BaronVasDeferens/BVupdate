/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapupdate;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 *
 * @author skot
 */
public class MapUpdater {
    
    BufferedImage map = null;
    HashMap occupied;
    HashMap avails;
    
    // UPDATER CONSTRUCTOR
    
    public MapUpdater(String ... args) {
        
        occupied = new <Integer, Building>HashMap();
        avails = new <Integer, Building>HashMap();
        
        populateHashmap();
        sortBuildings(args);
        
        map = loadMapImage();
        drawBuildings();
        saveMap();
        
    }
    
    // POPULATE HASHMAP
    // Creates a total listing of the buildings and their positions within the image.
    private void populateHashmap() {
       
        Polygon poly;
        Building b;
        
        //Building  (String name, String address, LinkedList<String> features, 
        //          int length, int width, int monthlyRate, Polygon myPoly)
        
        // #27
        poly = new Polygon();
        poly.addPoint(175,108);
        poly.addPoint(206,108);
        poly.addPoint(206,130);
        poly.addPoint(175,130);
        b = new Building("27", "", poly);
        occupied.put("27".hashCode(), b);

        // #26
        poly = new Polygon();
        poly.addPoint(109,113);
        poly.addPoint(152,113);
        poly.addPoint(152,134);
        poly.addPoint(109,134);
        b = new Building("26", "2779 NE Bunn Road", poly);
        b.setSize(40, 88, 12);
        b.setRestrooms(1, 0);
        b.setDoors(2, "two 20'x10'");
        b.setOptions(true, true);
        b.addFeature("office space");
        b.addFeature("alarm system");
        b.addFeature("This unit includes yard space (gravelled, fenced, and gated).");
        occupied.put("26".hashCode(), b);
        
        // #25
        poly = new Polygon();
        poly.addPoint(130,137);
        poly.addPoint(150,137);
        poly.addPoint(150,170);
        poly.addPoint(130,170);
        b = new Building("25","2775 NE Bunn Road", poly);
        b.setSize(40,60,14);
        b.setDoors(2, "two 10'x10', one 16'x10', one 15'x10'");
        b.setRestrooms(1, 1);
        b.setOptions(true, true);
        b.addFeature("This unit includes yard space (gravelled, fenced, and gated).");
        occupied.put("25".hashCode(), b);
        
        // #24
        poly = new Polygon();
        poly.addPoint(180,136);
        poly.addPoint(200,136);
        poly.addPoint(200,166);
        poly.addPoint(180,166);
        b = new Building("24", "", poly);
        occupied.put("24".hashCode(), b); 

        // #23
        poly = new Polygon();
        poly.addPoint(178,181);
        poly.addPoint(200,181);
        poly.addPoint(200,212);
        poly.addPoint(178,212);
        b = new Building("23", "", poly);
        occupied.put("23".hashCode(), b);
        
        // #22
        poly = new Polygon();
        poly.addPoint(130,225);
        poly.addPoint(153,221);
        poly.addPoint(158,249);
        poly.addPoint(135,253);
        b = new Building("22","2763 NE Bunn Road",poly);
        b.setSize(40, 60, 12);
        b.setOptions(true, false);
        b.setRestrooms(1, 1);
        b.setDoors(2, "four 10'x10'");
        occupied.put("22".hashCode(), b);
        
        // #21 A -- Right side (South)
        poly = new Polygon();
        poly.addPoint(154,268);
        poly.addPoint(168,265);
        poly.addPoint(173,285);
        poly.addPoint(158,287);
        b = new Building("21A","2759 NE Bunn Road", poly);
        b.setSize(40,24,12);
        b.setRestrooms(1, 0);
        b.setOptions(true, true);
        b.setDoors(1, "one 10'x10'");
        occupied.put("21A".hashCode(), b);
        
        // #21 B -- Left side (North)
        poly = new Polygon();
        poly.addPoint(139,270);
        poly.addPoint(154,268);
        poly.addPoint(158,287);
        poly.addPoint(144,290);
        b = new Building("21B","2757 NE Bunn Road", poly);
        b.setSize(40,36,12);
        b.setRestrooms(1, 0);
        b.setOptions(true, true);
        b.setDoors(1, "two 10'x10'");
        occupied.put("21B".hashCode(), b);
        
        // #20
        poly = new Polygon();
        poly.addPoint(175,240);
        poly.addPoint(208,240);
        poly.addPoint(208,262);
        poly.addPoint(175,262);
        b = new Building("20","",poly);
        occupied.put("20".hashCode(), b);
            
        
        // #19
        poly = new Polygon();
        poly.addPoint(181,273);
        poly.addPoint(202,273);
        poly.addPoint(202,301);
        poly.addPoint(181,301);
        b = new Building("19","2749 NE Bunn Road",poly);
        b.setSize(40,60,12);
        b.setRestrooms(1, 0);
        b.setOptions(true, true);
        b.setDoors(1, "two 12'x12'");
        b.addFeature("Splittable into two 40'x30' units");
        occupied.put("19".hashCode(), b);
        
        // #18
        poly = new Polygon();
        poly.addPoint(215,298);
        poly.addPoint(244,309);
        poly.addPoint(237,327);
        poly.addPoint(208,315);
        b = new Building("18","2747 NE Bunn Road",poly);
        b.setSize(40,60,12);
        b.setRestrooms(1, 0);
        b.setOptions(true, false);
        b.setDoors(1, "two 10'x10'");
        occupied.put("18".hashCode(), b);
        
        // #17
        poly = new Polygon();
        poly.addPoint(250,317);
        poly.addPoint(280,327);
        poly.addPoint(273,345);
        poly.addPoint(243,335);
        b = new Building("17","2743 NE Bunn Road", poly);
        b.setSize(40,60,16);
        b.setRestrooms(1, 1);
        b.setOptions(true, true);
        b.setDoors(2, "two 20'x12'");
        occupied.put("17".hashCode(), b);
        
        // #16
        poly = new Polygon();
        poly.addPoint(268,264);
        poly.addPoint(285,270);
        poly.addPoint(280,284);
        poly.addPoint(262,278);
        b = new Building("16", "", poly);
        occupied.put("16".hashCode(), b);
        
        // #15A
        poly = new Polygon();
        poly.addPoint(236,232);
        poly.addPoint(251,232);
        poly.addPoint(251,253);
        poly.addPoint(236,253);
        b = new Building("15A","2731 NE Bunn Road", poly);
        occupied.put("15A".hashCode(), b);
        
        // #15B
        poly = new Polygon();
        poly.addPoint(221,232);
        poly.addPoint(236,232);
        poly.addPoint(236,253);
        poly.addPoint(221,253);
        b = new Building("15B","2733 NE Bunn Road", poly);
        b.setSize(40, 36, 12);
        b.setRestrooms(1, 0);
        b.setOptions(true, false);
        b.setDoors(1, "two 10'x10'");
        occupied.put("15B".hashCode(), b);
        
        
        
        // #14
        poly = new Polygon();
        poly.addPoint(306,230);
        poly.addPoint(327,234);
        poly.addPoint(317,277);
        poly.addPoint(296,273);
        b = new Building("14","2729 NE Bunn Road", poly);
        b.setSize(40, 96, 14);
        b.setRestrooms(1, 0);
        b.setOptions(true, true);
        b.setDoors(4, "two 10'x10'");
        b.addFeature("carpeted office space");
        b.addFeature("1920 sqft loft");
        b.addFeature("alarm system");
        occupied.put("14".hashCode(), b);
        
        // #13
        poly = new Polygon();
        poly.addPoint(222,180);
        poly.addPoint(253,180);
        poly.addPoint(252,202);
        poly.addPoint(222,202);
        b = new Building("13", "272 NE Bunn Road", poly);
        occupied.put("13".hashCode(), b);
        
        // #12
        poly = new Polygon();
        poly.addPoint(289,197);
        poly.addPoint(336,206);
        poly.addPoint(331,226);
        poly.addPoint(286,216);
        b = new Building("12","",poly);
        occupied.put("12".hashCode(), b);
        
        // #11
        poly = new Polygon();
        poly.addPoint(310,156);
        poly.addPoint(339,156);
        poly.addPoint(339,186);
        poly.addPoint(310,186);
        b = new Building("11","",poly);
        occupied.put("11".hashCode(), b);
        
        // #10
        poly = new Polygon();
        poly.addPoint(224,139);
        poly.addPoint(253,139);
        poly.addPoint(253,157);
        poly.addPoint(224,157);
        b = new Building("10","",poly);
        occupied.put("10".hashCode(), b);
        
        // #9
        poly = new Polygon();
        poly.addPoint(224,116);
        poly.addPoint(253,116);
        poly.addPoint(253,136);
        poly.addPoint(224,136);
        b = new Building("9","",poly);
        occupied.put("9".hashCode(), b);
        
        // #8
        poly = new Polygon();
        poly.addPoint(280,119);
        poly.addPoint(301,119);
        poly.addPoint(301,155);
        poly.addPoint(280,155);
        b = new Building("8","",poly);
        occupied.put("8".hashCode(), b);
        
        // #7
        poly = new Polygon();
        poly.addPoint(303,129);
        poly.addPoint(331,129);
        poly.addPoint(331,149);
        poly.addPoint(303,149);
        b = new Building("7","",poly);
        occupied.put("7".hashCode(), b);
        
        // #6
        poly = new Polygon();
        poly.addPoint(289,292);
        poly.addPoint(307,296);
        poly.addPoint(299,330);
        poly.addPoint(280,325);
        b = new Building("6","",poly);
        occupied.put("6".hashCode(), b);
        
        // #5
        poly = new Polygon();
        poly.addPoint(263,352);
        poly.addPoint(282,359);
        poly.addPoint(269,393);
        poly.addPoint(250,384);
        b = new Building("5","",poly);
        occupied.put("5".hashCode(), b);
        
        // #4B Top / North
        poly = new Polygon();
        poly.addPoint(155,319);
        poly.addPoint(176,313);
        poly.addPoint(183,337);
        poly.addPoint(162,343);
        b = new Building("4B","",poly);
        occupied.put("4B".hashCode(), b);
        
        // #4A Bottom / South
        poly = new Polygon();
        poly.addPoint(162,343);
        poly.addPoint(183,337);
        poly.addPoint(189,358);
        poly.addPoint(169,364);
        b = new Building("4A","",poly);
        occupied.put("4A".hashCode(), b);
        
        // #3
        poly = new Polygon();
        poly.addPoint(172,374);
        poly.addPoint(191,369);
        poly.addPoint(201,402);
        poly.addPoint(182,407);
        b = new Building("3","",poly);
        occupied.put("3".hashCode(), b);
        
        // #2
        poly = new Polygon();
        poly.addPoint(239,343);
        poly.addPoint(260,350);
        poly.addPoint(247,384);
        poly.addPoint(226,375);
        b = new Building("2","",poly);
        occupied.put("2".hashCode(), b);
        
        // #1A Left / North
        poly = new Polygon();
        poly.addPoint(220,401);
        poly.addPoint(240,409); // 1
        poly.addPoint(232,427);
        poly.addPoint(211,418);
        b = new Building("1A","8083 NW Hwy 99",poly);
        b.setSize(40, 48, 14);
        b.setRestrooms(1,1);
        b.setRate(1450);
        b.setDoors(1, "two 12'x12' overhead doors");
        b.setOptions(true, true);
        occupied.put("1A".hashCode(), b);
        
        // #1B Right / South
        poly = new Polygon();
        poly.addPoint(240,409);
        poly.addPoint(263,420);
        poly.addPoint(255,437);
        poly.addPoint(232,427);
        b = new Building("1B","",poly);

        occupied.put("1B".hashCode(), b);
           
    }
    
    
    // LOAD MAP IMAGE
    // Loads the "blank" map
    private BufferedImage loadMapImage() {
        
        BufferedImage return_image = null;
        InputStream fin = null;
        
        fin = getClass().getResourceAsStream("resources/blankmap.png");
            
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
    
    
    // SORT BUILDINGS
    // Separates the occupied buildings and available buildings into two distinct
    // hashmaps.
    private void sortBuildings(String ... args) {
        
        // Remove all of the "occupied" buildings from the occupied map
        // and store them in a seperate hash in order to later generate
        // a clickmap for the availabilities
        
        for (String bldg: args) {
            System.out.println("removing " + bldg);
            avails.put(bldg.hashCode(), occupied.get(bldg.hashCode()));
            occupied.remove(bldg.hashCode());
        }
        
    }
    
    // DRAW BUILDINGS
    // Accepts the list of available buildings and draws them to the map
    private void drawBuildings() {
        
        // Setup the graphics context
        Graphics2D g2 = map.createGraphics();
        g2.setComposite(AlphaComposite.SrcOver.derive(0.8f));
        g2.setColor(new Color(180,80,0));
        
        // Begin drawing over the sequence of occupied buildings
        Polygon p;
        Set<Map.Entry<String, Building>> set = occupied.entrySet();

        for (Map.Entry<String, Building> me: set) {
            //System.out.println("drawing " + me.getKey());
            p = (Polygon)me.getValue().myPoly;
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

