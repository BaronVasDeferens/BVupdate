import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.plaf.ButtonUI;
import java.sql.*;
import java.sql.SQLException;


class MapUpdater {

    private final Color occupiedColor = new Color(180, 80, 0);
    private Map<String, Building> allBuildings;

    MapUpdater(Map<String, Building> allBuildings) {
        this.allBuildings = allBuildings;
    }

    public void drawAndSaveMap() {
        BufferedImage map = loadMapImage();
        drawBuildings(allBuildings, map);
        saveMap(map);
    }

    // LOAD MAP IMAGE
    // Loads the "blank" map
    private BufferedImage loadMapImage() {

        BufferedImage return_image = null;

        try (InputStream fin = getClass().getResourceAsStream("resources/blankmap.png")) {
            return_image = ImageIO.read(fin);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return (return_image);
    }


    // DRAW BUILDINGS
    // Accepts the list of available buildings and draws them to the map
    private void drawBuildings(final Map<String, Building> allBuildings, BufferedImage map) {

        // Setup the graphics context
        Graphics2D g2 = map.createGraphics();
        g2.setComposite(AlphaComposite.SrcOver.derive(0.8f));
        g2.setColor(occupiedColor);

        for (Building building : allBuildings.values()) {
            if (building.shouldDraw) {
                g2.fillPolygon(building.polygon);
            }
        }
    }

    // SAVE MAP
    // Handles the out-to-disk duties, saving the new map as "map.png"
    private void saveMap(final BufferedImage map) {

        if (map == null) {
            System.out.println("ERROR: map is null!");
            return;
        }

        try {
            File out = new File("map.png");
            ImageIO.write(map, "png", out);
            System.out.println("Map saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}