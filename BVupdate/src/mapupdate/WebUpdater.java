/*
WEB UPDATER
Generates an updated series of files based on data passed to it from MapUpdater.

The main updates are made:

In index.html:
    The clickmap for map.png is generated and written
    The main table of availabilities is updated
    The month is updated
In application.html:
    The dropdown list of available units is updated
 */
package mapupdate;
import java.awt.Polygon;
import java.time.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author skot
 */
public class WebUpdater {
    
    HashMap avails;
    PrintWriter index;
    
    public WebUpdater(MapUpdater mapUpdate) {
        
        index = null;
        
        // Basic checks to avoid disaster
        if (mapUpdate == null) {
            System.out.println("ERROR WebUpdater(): MapUpdater object is null...");
            System.exit(1);
        }
        
        if (mapUpdate.avails == null) {
            System.out.println("ERROR WebUpdater(): MapUpdater HashMap object is null...");
            System.exit(1);
        }
        
        avails = mapUpdate.avails;
        updateIndex();
    }
    
    // UPDATE INDEX
    // Creates a new "index.html" file by importing snippets of the finished file
    // and carefully inserting new portions where appropriate
    private void updateIndex() {
              
        // Open the various streams and file streams; fatal errors if any fail to open.
        try {
            index = new PrintWriter("index.html","UTF-8");
        }

        catch (IOException e) {
            System.out.println("FATAL ERROR: could not create index.html...");
        }

        
        // PART 1: Initial html transfer
        // Transfer the all of part1.txt to the index file (up to the date)
        loadAndAddContentFromFile("part1.txt");
        
        // Tack on the updated month...
        index.print("\n");
        index.print(updateAvailableMonth());
        
        // PART 2: Generate the new Clickmap
        generateClickMap();
        
        // PART 3: Generate the new availability tables
        generateAvailTable();
        
        // PART 4: Append the final bits of unchanging html
        loadAndAddContentFromFile("part2.txt");
        
        
        // Wrap it up, B!
        index.close();
        
    }
    
    private void loadAndAddContentFromFile(String filename) {
        
        BufferedReader in = null;
        InputStream fileIn = null;
        
        try {
            fileIn = getClass().getResourceAsStream("resources/" + filename);
            in = new BufferedReader(new InputStreamReader(fileIn));
            
            String input = in.readLine();
            while (input != null) {
                index.print(input);
                input = in.readLine();
            }
            
            // Close the undeed streams...
            in.close();
            fileIn.close();
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
        
    }
    
    
    // UPDATE AVAILABLE MONTH
    // Retruns <h3>AVAILABILITIES FOR MONTH 201X</h3>
    private String updateAvailableMonth() {
        String newTime = (LocalDateTime.now().getMonth() + " " +  LocalDateTime.now().getYear());
        String availMonth = "<h3>AVAILABILITIES FOR " + newTime + "</h3>";
        return availMonth;
         
    }
    
    // GENERATE CLICK MAP
    // Using data from the MApUpdate, create a new clickmap and pipe
    private void generateClickMap() {
        
        // Inject the next bit of HTML
        //index.print("<img src=\"http://www.bunnsvillage.com/images/map.png\" width=\"444\" class=\"picture\" usemap=\"#clickmap\"> \n<map name=\"clickmap\">");
        index.print("<img src=\"map.png\" width=\"444\" class=\"picture\" usemap=\"#clickmap\"> \n<map name=\"clickmap\">");
        

        Set<Map.Entry<String, Building>> set = avails.entrySet();
        Polygon p;
        int xVals[], yVals[];
        String coordString = "";
        
        for (Map.Entry<String, Building> me: set) {

            index.write("<area shape=\"poly\" coords=\"");
            
            // Extract a polygon and it's list posy x and y points
            p = (Polygon)me.getValue().myPoly;
            
            if (p == null)
                continue;
            
            xVals = p.xpoints;
            yVals = p.ypoints;
            
            // Evaluate the x/y pairs as integers, then write to string (adding a comma after all except the last)
            for (int i = 0; i < p.npoints; i++) {
                coordString += Integer.toString(xVals[i]) + "," + Integer.toString(yVals[i]);
                
                if (i != (p.npoints -1))
                    coordString += ",";
            }
            
            // Append the new string to the index file
            index.write(coordString);
            index.write("\" href=\"http://www.bunnsvillage.com/" + me.getValue().name + ".html\" alt=\"#" + me.getValue().name +"\">\n");
            coordString = "";

        }
        
        // cap off the map tag
        index.write("</map>");
        
    }
    
    
    // GENRATE AVAIL TABLE
    // Using the remainder of the building data, create the table and feature list...
    private void generateAvailTable() {
        
        
    }
    
    
}
