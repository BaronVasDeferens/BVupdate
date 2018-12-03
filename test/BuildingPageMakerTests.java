import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingPageMakerTests {

    Building building;

    @BeforeEach
    void setUp() {
        building = new Building();
        building.setName("14");
        building.setAddress("1122 Boogie Boogie Ave.");
        building.setSize(5, 6, 7);
        building.setRestrooms(2, 1);
        building.setAirConditioning("yes");
        building.setThreePhase("YES");
        building.setDoors(2, "one 12x12");
        building.setRate(1200);
        building.setIsOccupied(false);
    }

    @Test
    void basicTest() {
        BuildingPageMaker buildingPageMaker = new BuildingPageMaker();
        assertNotNull(buildingPageMaker.createPage(building));
    }

    @Test
    void noFeaturesTest() {
        BuildingPageMaker buildingPageMaker = new BuildingPageMaker();
        final String result = buildingPageMaker.createPage(building);
        assertFalse(result.contains("Features"));

    }

    @Test
    void withFeatures() {
        building.addFeature("not a shit-hole");
        building.addFeature("perfectly suitable for wife");

        BuildingPageMaker buildingPageMaker = new BuildingPageMaker();
        final String result = buildingPageMaker.createPage(building);

        assertTrue(result.contains("Features"));
    }
}
