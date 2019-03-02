import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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

        System.out.println("Reading buildings from db...");

        String queryString = "SELECT * FROM buildings";     // FIX ME: bad practice
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
                if (notes != null && notes.length() > 0) {
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
                allBldgs.put(building.name, building);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(allBldgs.size() + " units read");
        return allBldgs;

    }

    // Handle Half Buildings
    // Set isOccupied on sub-units of occupied masters, and vice-versa
    // Determine whether a particular building should be drawn based on occupancy relationship
    void handleTransitiveOccupancy() {

        final Set<Building> alreadyProcessed = new HashSet<>();
        final List<Building> availableBuildings = allBuildings.values().stream().filter(bldg -> !bldg.isOccupied).collect(Collectors.toList());
        final List<Building> occupiedBuildings = allBuildings.values().stream().filter(building -> building.isOccupied).collect(Collectors.toList());

        availableBuildings.stream().forEach(bldg -> System.out.println("AVAILABLE: " + bldg.name));

        // Process available subunits
        availableBuildings.stream()
                .filter(building -> building.isSubunit)
                .forEach(building -> {
                    final Building master = allBuildings.get(building.getMasterBuildingName());
                    master.setIsOccupied(true);
                    master.setShouldDraw(false);
                    alreadyProcessed.add(master);
                    alreadyProcessed.add(building);
                });

        // Process available masters
        availableBuildings.stream()
                .filter(bldg -> !alreadyProcessed.contains(bldg))
                .filter(bldg -> !bldg.isSubunit)
                .forEach(building -> {

                    Building unitA = allBuildings.get(building.name + "A");
                    unitA.setShouldDraw(false);
                    unitA.setIsOccupied(true);
                    Building unitB = allBuildings.get(building.name + "B");
                    unitB.setShouldDraw(false);
                    unitB.setIsOccupied(true);

                    alreadyProcessed.add(unitA);
                    alreadyProcessed.add(unitB);
                    alreadyProcessed.add(building);
                });

    }

    public HashMap<String, Building> getBuildings() {
        return this.allBuildings;
    }
}
