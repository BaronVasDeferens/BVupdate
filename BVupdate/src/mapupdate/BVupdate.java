/*
 * 
Map Updater
Allows for easy-update re-drawing of the ever-changing Bunns Village
availabilities map.

USAGE: This tool is designed to take as arguments all AVAILABLE units in the
following format: mapupdate 11 13 21A 17B

The MapUpdater is up first, generating the lists of occupied and available units.
The WebUpdater uses the data furnished by MapUpdater to generate a new website
 
 */
package mapupdate;


public class BVupdate {


    public static void main(String ... args) {
        
        // Check for minimum number of arguments
        if (args.length <= 0) {
            System.out.println("MAP UPDATER:");
            System.out.println("Did you mean to set ALL BUILDINGS TO OCCUPIED?");
        }
        
        //
        
        MapUpdater mapupdater = new MapUpdater(args);
        WebUpdater webupdater = new WebUpdater(mapupdater);
        
    }
    
}

