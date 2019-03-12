import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class WebUpdater {

    Map<String, Building> allBuildings;

    public WebUpdater(Map<String, Building> allBuildings) {
        this.allBuildings = allBuildings;
        updateIndex();
    }

    // UPDATE INDEX
    // Creates a new "index.html" file by importing snippets of the finished file
    // and carefully inserting new portions where appropriate
    private void updateIndex() {

        System.out.println("Updating index.html...");

        PrintWriter index;

        // Open the various streams and file streams; fatal errors if any fail to open.
        try {
            index = new PrintWriter("index.html", StandardCharsets.UTF_8);

            // PART 1: Initial html transfer
            // Transfer the all of part1.html to the index file (up to the date)
            loadAndAddContentFromFile(index, "part1.html");

            // Updated month
            index.print("\n");
            index.print(updateAvailableMonth());

            // PART 2: Generate the new Clickmap
            generateClickMap(index);

            // PART 3: Generate the new availability tables
            generateAvailTable(index);

            // PART 4: Append the final bits of unchanging html
            loadAndAddContentFromFile(index, "part2.html");

            // Wrap it up, B!
            index.close();

        } catch (IOException e) {
            System.out.println("ERROR: could not create index.html...");
            throw new RuntimeException();
        }

    }

    // Load and Add Content From File
    // Accepts a filename of a TEXT FILE and adds all content found to
    // member PrintWriter "index"
    private boolean loadAndAddContentFromFile(PrintWriter index, String filename) {

        boolean returnVal = false;

        try (InputStream fileIn = getClass().getResourceAsStream("resources/" + filename);
             BufferedReader in = new BufferedReader(new InputStreamReader(fileIn))) {

            String input = in.readLine();
            while (input != null) {
                index.print(input);
                index.print("\n");
                input = in.readLine();
            }
            returnVal = true;

        } catch (IOException e) {
            System.out.println(e.toString());
            return (false);
        }

        return (returnVal);

    }


    // UPDATE AVAILABLE MONTH
    // Returns <h3>AVAILABILITIES FOR MONTH 201X</h3>
    private String updateAvailableMonth() {
        String newTime = (LocalDateTime.now().getMonth() + " " + LocalDateTime.now().getYear());
        return "<h3>AVAILABILITIES FOR " + newTime + "</h3>\n";

    }

    // GENERATE CLICK MAP
    // Using data from the MapUpdate, create a new clickmap and pipe
    private void generateClickMap(PrintWriter index) {

        // Inject the next bit of HTML
        //index.print("<img src=\"http://www.bunnsvillage.com/images/map.png\" width=\"444\" class=\"picture\" usemap=\"#clickmap\"> \n<map name=\"clickmap\">");
        index.print("<img src=\"map.png\" width=\"444\" class=\"picture\" usemap=\"#clickmap\"> \n<map name=\"clickmap\">\n");


        String coordString = "";

        // Iterate through the buildings.
        // If an erroneous building is entered, do not generate clickmap table entry for it
        for (Building b : allBuildings.values()) {

            if (b.isOccupied)
                continue;

            // Extract a polygon and it's list posy x and y points
            Polygon p = b.polygon;
            if (p == null)
                continue;

            int xVals[] = p.xpoints;
            int yVals[] = p.ypoints;

            index.write("<area shape=\"poly\" coords=\"");

            // Evaluate the x/y pairs as integers, then write to string (adding a comma after all except the last)
            for (int i = 0; i < p.npoints; i++) {
                coordString += Integer.toString(xVals[i]) + "," + Integer.toString(yVals[i]);

                if (i != (p.npoints - 1))
                    coordString += ",";
            }

            // Append the new string to the index file
            index.write(coordString);
            index.write("\" href=\"http://www.bunnsvillage.com/" + b.name + ".html\" alt=\"#" + b.name + "\">\n");
            coordString = "";

        }

        // cap off the map tag
        index.write("</map>\n");

    }


    // GENERATE AVAIL TABLE
    // Using the remainder of the building data, create the table and feature list
    private void generateAvailTable(PrintWriter index) {


        // Add the table information to the "index" PrintWriter
        if (!loadAndAddContentFromFile(index, "tablesetup.txt")) {
            System.out.println("ERROR: tablesetup.txt not found!");
            return;
        }


        if (allBuildings.values().stream().allMatch((b) -> b.isOccupied)) {
            index.println("</tbody>\n </table>");
            index.println("<h2>We're sorry-- there are no buildings currently available for lease.</h2>");
            return;
        }


        // For each available building, add attributes to the "avail table"
        final List<Building> sortedBuildings = allBuildings.values().stream()
                .filter(building -> !building.isOccupied)
                .sorted(Comparator.comparing(a -> a.name))
                .collect(Collectors.toList());

        for (Building b : sortedBuildings) {

            index.println("<tr align=\"center\">");

            index.println("<td width=\"100\">" + b.name + "</td>");
            index.println("<td width=\"100\">" + b.address + "</td>");
            index.print("<td width=\"100\">" + Integer.toString(b.length) + "x" + Integer.toString(b.width));

            if (b.height > 0)
                index.println("x" + Integer.toString(b.height) + "</td>");
            else
                index.println("</td>");

            // Include a feature from the feature list (if any)
            String feature = "";

            if (b.features.size() > 0)
                feature = b.features.get(0);

            index.println("<td width=\"100\">" + feature);
            index.println("<td width=\"100\">" + "$" + b.monthlyRate + "</td>");
            index.println("<td width=\"100\">" + "<a href = \"http://www.bunnsvillage.com/" + b.name + ".html\">NOW</a></td>");
            index.println("</tr>\n");

        }

        // Close the table out
        index.println("</tbody>\n </table>");

    }

}
