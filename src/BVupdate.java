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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.HashMap;

public class BVupdate {


    public static void main(String... args) {

        // Check for arguments:
        // If there were none, assume all building are set to occupiedBuildings
        if (args.length == 0) {
            System.out.println("Setting ALL BUILDINGS TO OCCUPIED!");
        } else {
            Arrays.stream(args).forEach(System.out::println);
        }

        // TODO: also create individual website for building
        // TODO: update application drop-down box to reflect availabilities

        Connection connection;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:buildings.db");

            BuildingMaster buildingMaster = new BuildingMaster(connection, args);
            connection.close();

            final HashMap<String, Building> allBuildings = buildingMaster.getBuildings();

            // Craft the site
            WebUpdater webUpdater = new WebUpdater(allBuildings);

            // Draw the map
            MapUpdater mapUpdater = new MapUpdater(allBuildings);
            mapUpdater.drawAndSaveMap();

            BuildingPageMaker buildingPageMaker = new BuildingPageMaker();

            Arrays.stream(args).forEach( (bld) -> {

                Building building = allBuildings.get(bld);

                if (building != null) {
                    try {
                        FileUtils.write(new File(building.name + ".html"), buildingPageMaker.createPage(building));
                        System.out.println("Created " + building.name + ".html");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });


        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }


    }

}

