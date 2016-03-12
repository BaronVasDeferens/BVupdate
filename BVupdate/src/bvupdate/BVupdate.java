/*
 * 
Map Updater
Allows for easy-update re-drawing of the ever-changing Bunns Village
availabilities map.

USAGE: This tool is designed to take as arguments all AVAILABLE units in the
following format: mapupdate 11 13 21A 17B

All buildings NOT specified are considered to be OCCUPIED

The MapUpdater is up first, generating the lists of occupied and available units.
The WebUpdater uses the data furnished by MapUpdater to generate a new website
 
OUTPUT: index.html and map.png

 */
package bvupdate;

import java.sql.*;

public class BVupdate {


    public static void main(String ... args) {
        
        // Check for arguments:
        // If there were none, assume all building are set to occupied
        if (args.length <= 0) {
            System.out.println("MAP UPDATER:");
            System.out.println("Did you mean to set ALL BUILDINGS TO OCCUPIED?");
        }
        
        
        // Establish connection to database: buildings.db
        Connection connection = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:buildings.db");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (connection == null) {
            System.out.println("FATAL ERROR: Failed to load database!");
            System.exit(1);
        }
        
        System.out.println("Database load success...");
        
        
        
        
        MapUpdater mapupdater = new MapUpdater(args);
        WebUpdater webupdater = new WebUpdater(mapupdater);
        
        try {
            connection.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        
    }
    
}

