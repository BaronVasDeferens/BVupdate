import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BuildingMaster {

    private Connection connection;

    private HashMap<String, Building> allBuildings;

    public BuildingMaster(Connection connection, String[] args) {
        this.connection = connection;
        allBuildings = readBuildingsFromDatabase(args);
        handleTransitiveOccupancy();
    }

    // Package-private constructor for tests
    BuildingMaster(HashMap<String, Building> allBuildings) {
        this.allBuildings = allBuildings;
    }

    private HashMap<String, Building> readBuildingsFromDatabase(final String[] args) {

        String queryString = "SELECT * FROM buildings";
        PreparedStatement statement = null;
        ResultSet results = null;

        HashMap<String, Building> buildings = new HashMap<>();

        try {

            statement = connection.prepareStatement(queryString);
            results = statement.executeQuery();

            if (results != null)
                buildings = createBuildings(results, args);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                }
            }

            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                }
            }
        }

        return buildings;
    }

    // CREATE BUILDINGS
    // Takes the ResultSet from the database query;
    // populates hashmap "occupiedBuildings" using the data from result set
    private HashMap<String, Building> createBuildings(ResultSet results, String[] args) {

        HashMap<String, Building> allBldgs = new HashMap<>();

        try {
            while (results.next()) {

                Building building = new Building();

                // Grab values from the results
                building.setName(results.getString("number"));
                building.setAddress(results.getString("address"));
                building.setSize(
                        results.getInt("length"),
                        results.getInt("width"),
                        results.getInt("height"));
                building.setDoors(
                        results.getInt("mandoors"),
                        results.getString("ohd_desc"));
                building.setRestrooms(
                        results.getInt("restrooms"),
                        results.getInt("restrooms_ada"));
                building.setRate(results.getInt("rate"));
                building.setThreePhase(results.getString("three_phase"));
                building.setAirConditioning(results.getString("ac"));

                // Chunk up the "notes" and add them as Building.features.
                // Delineate by semicolon (up to max of 10 feature strings)
                final String notes = results.getString("notes");
                if (notes != null) {
                    String featureArray[] = notes.split(";", 10);
                    Arrays.stream(featureArray).forEach(building::addFeature);
                }

                // Get polygon coordinates
                final String coords = results.getString("coords");
                Polygon poly = new Polygon();
                if (coords != null) {
                    // Extract coordinates from "coords" column
                    String coordsArray[] = coords.split(",", 8);
                    for (int i = 0; i < 8; i += 2) {
                        poly.addPoint(Integer.parseInt(coordsArray[i]), Integer.parseInt(coordsArray[i + 1]));
                    }
                }
                building.setPolygon(poly);

                // Occupancy test:
                building.setIsOccupied(Arrays.stream(args).noneMatch((s) -> s.equalsIgnoreCase(building.name)));
//                building.setShouldDraw(Arrays.stream(args).noneMatch((s) -> s.equalsIgnoreCase(building.name)));

                allBldgs.put(building.name, building);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allBldgs;

    }

    // Handle Half Building
    // Set isOccupied on sub-units of occupied masters, and vice-versa
    // Determine whether a particular building should be drawn based on occupancy relationship
    void handleTransitiveOccupancy() {

        Set<Building> alreadyProcessed = new HashSet<>();

        // Pass one: Occupied "master" units: set sub-units to occupied
        for (Building building : allBuildings.values()) {

            if (building.isOccupied && !building.isSubunit) {

                Building unitA = allBuildings.get(building.name + "A");
                Building unitB = allBuildings.get(building.name + "B");

                if (unitA != null) {
                    unitA.setIsOccupied(true);
                    unitA.setShouldDraw(false);
                    alreadyProcessed.add(unitA);
                }

                if (unitB != null) {
                    unitB.setIsOccupied(true);
                    unitB.setShouldDraw(false);
                    alreadyProcessed.add(unitB);
                }

                alreadyProcessed.add(building);
                building.setShouldDraw(true);
            }
        }

        // Pass two: occupied sub-units set their masters to occupied
        for (Building building : allBuildings.values()) {

            if (alreadyProcessed.contains(building)) {
                continue;
            }

            // Occupied sub-units: set "master" units to occupied
            if (building.isOccupied && building.isSubunit) {
                final String masterName = building.name.substring(0, building.name.length() - 1);

                Building master = allBuildings.get(masterName);
                if (master != null) {
                    master.setIsOccupied(true);
                    master.setShouldDraw(false);
                    building.setShouldDraw(true);
                    System.out.println(building.name + " is occupied sub-unit of " + master.name);
                }
            }
        }
    }

    public HashMap<String, Building> getBuildings() {
        return this.allBuildings;
    }
}
