/*
 *
Map Updater
Allows for easy-update re-drawing of the ever-changing Bunns Village
availabilities map.

USAGE: This tool is designed to take as arguments all AVAILABLE units in the
following format: mapupdate 11 13 21A 17B

NOTE: "3A 3B" will produce different results than just "3"

All buildings NOT specified are considered to be OCCUPIED

The MapUpdater is up first, generating the lists of occupied and available units.
The WebUpdater uses the data furnished by MapUpdater to generate a new website

OUTPUT: index.html and map.png

TODO: generate a new & updated version of each individual building web page
TODO: load, modify, and save a new version of the application web page using the
      // updated data to populate the drop-down box

 */

package bvupdate;

import java.sql.Connection;
import java.sql.DriverManager;

public class BVupdate {


    public static void main(String... args) {

        // Check for arguments:
        // If there were none, assume all building are set to occupiedBuildings
        if (args.length == 0) {
            System.out.println("Setting ALL BUILDINGS TO OCCUPIED!");
        }

        Connection connection;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:buildings.db");
            System.out.println("Database load success...");

            // Update the map and website
            // TODO: also create individual website for building
            // TODO: update application drop-down box to reflect availabilities

            MapUpdater mapupdater = new MapUpdater(connection, args);
            WebUpdater webupdater = new WebUpdater(mapupdater);

            connection.close();

        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }


    }

}

