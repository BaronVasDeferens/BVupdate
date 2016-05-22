/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvupdate;

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
import java.sql.*;
import java.sql.SQLException;


/**
 *
 * @author skot
 */
public class MapUpdater {
    
    BufferedImage map = null;
    HashMap occupied;
    HashMap avails;
    Connection connection;
    
    // UPDATER CONSTRUCTOR
    
    public MapUpdater(Connection connection, String ... args) {
        
        this.connection = connection;
        
        occupied = new <Integer, Building>HashMap();
        avails = new <Integer, Building>HashMap();
        
        populateHashmap(args);
        sortBuildings(args);
        
        map = loadMapImage();
        drawBuildings();
        saveMap();
        
    }
    
    // CREATE BUILDINGS
    // Takes the ResultSet from the database query; 
    // populates hashmap "occupied" using the data from resultset
    private void createBuildings(ResultSet results) {
        
        try {
            while (results.next()) {
                String number, address, coords, ohdDescription, notes, temp;
                int length, width, height, sqfeet, mandoors, restrooms, restrooms_ada, rate;
                Boolean threePhase = false, hasAC = false;
                Polygon poly;
                Building b;

                // Grab values from the results
                number = results.getString("number");
                address = results.getString("address");
                coords = results.getString("coords");
                length = results.getInt("length");
                width = results.getInt("width");
                height = results.getInt("height");
                sqfeet = length * width;
                mandoors = results.getInt("mandoors");
                ohdDescription = results.getString("ohd_desc");
                restrooms = results.getInt("restrooms");
                restrooms_ada = results.getInt("restrooms_ada");
                rate = results.getInt("rate");
                notes = results.getString("notes");
                
                temp = results.getString("three_phase");
                if (temp != null)
                    if (temp.matches("yes"))
                       threePhase = true;
                
                temp = results.getString("ac");
                if (temp != null)
                    if (temp.matches("yes"))
                       hasAC = true;

                
                poly = new Polygon();
                poly.reset();
                
                if (coords != null) {
                // Extract coordinates from "coords"
                    String coordsArray[] = coords.split(",", 8);
                    for (int i = 0; i < 8; i+= 2) {
                        poly.addPoint(Integer.parseInt(coordsArray[i]), Integer.parseInt(coordsArray[i+1]));
                    }
                }
               
                // Construct new building objkect from db results
                // and store it in the hash, indexed by it's unique "number" field
                b = new Building(number,address, poly);
                b.setDoors(mandoors, ohdDescription);
                b.setOptions(threePhase, hasAC);
                b.setRate(rate);
                b.setRestrooms(restrooms, restrooms_ada);
                b.setSize(length, width, height);
                
                // Chunk up the "notes" and add them as Building.features.
                // Delineate by semicolon (up to max of 10 feature strings)
                if (notes != null) {
                    String featureArray[] = notes.split(";",10);
                    for (int i = 0; i < featureArray.length; i++) {
                        b.addFeature(featureArray[i]);
                    }
                }
                
                occupied.put(number.hashCode(), b);

            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // POPULATE HASHMAP
    // Creates a total listing of the buildings, their attributes, and corresponding 
    //positions within the image.
    private void populateHashmap(String ... args) {
       
        Polygon poly;
        Building b;
        String queryString = "select * from buildings";
               
        Statement statment = null;
        ResultSet results = null;
        
        // IF THE PROGRAM DID NOT REQUIRE ALL BUILDING DATA:
        // Build the appropriate SQL query by finding all entries whose "number"
        // attribute matches a String in "args."
        // Example: if args.length is non-zero, the "where" keyword is appended

//        if (args.length > 0) {
//            queryString += (" where number = \"" + args[0] + "\"");
//            int i;
//            for (i = 1; i < args.length; i++) {
//                queryString += (" OR number = \"" + args[i] + "\"");
//            }
//        }
//        
//        System.out.println(queryString);
        
        try {
            statment = connection.createStatement();
            results = statment.executeQuery(queryString);
            
            if (results != null)
                createBuildings(results);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            
            if (statment != null) {
                try {
                    statment.close();
                }
                catch (SQLException e) {
                    System.out.println(e.toString());
                }
            }
            
            if (results != null) {
                try {
                    results.close();
                }
                catch (SQLException e) {
                    System.out.println(e.toString());
                }
            }
        }
           
    }
    
    
    // LOAD MAP IMAGE
    // Loads the "blank" map
    private BufferedImage loadMapImage() {
        
        BufferedImage return_image = null;
       
        try (InputStream fin = getClass().getResourceAsStream("resources/blankmap.png");){
            return_image = ImageIO.read(fin);
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
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
            

            System.out.print("removing " + bldg);
            
            if (!occupied.containsKey(bldg.hashCode()))
                System.out.print("...NOT FOUND!\n");
            else {
                avails.put(bldg.hashCode(), occupied.get(bldg.hashCode()));
                occupied.remove(bldg.hashCode());
                System.out.print("...OK!\n");
            }
            
            // In the event that user input is something like "3A 3B", the entry for "3"
            // should be scrubbed
            if (bldg.endsWith("A") || (bldg.endsWith("B"))) { 
                
                bldg = bldg.substring(0, (bldg.length() - 1));
                System.out.print("...also removing " + bldg);
                
                if (occupied.containsKey(bldg.hashCode())) {
                    occupied.remove(bldg.hashCode());
                    System.out.println("...OK!");
                }
                else
                    System.out.println("...NOT FOUND!");
            }
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