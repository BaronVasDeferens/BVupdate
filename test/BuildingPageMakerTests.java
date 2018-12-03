import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildingPageMakerTests {

    Building master;

    @BeforeEach
    void setUp() {
        master = new Building();
        master.setName("14");
        master.setAddress("1122 Boogie Boogie Ave.");
        master.setSize(5,6,7);
        master.setRestrooms(2,1);
        master.setAirConditioning("yes");
        master.setThreePhase("YES");
        master.setDoors(2, "one 12x12");
        master.setRate(1200);
        master.setIsOccupied(false);
    }

    @Test
    void basicTest() {
        BuildingPageMaker buildingPageMaker = new BuildingPageMaker();
        assertNotNull(buildingPageMaker.createPage(master));
    }

    @Test
    void noFeaturesTest() {
        BuildingPageMaker buildingPageMaker = new BuildingPageMaker();
        final String result = buildingPageMaker.createPage(master);
        assertFalse(result.contains("Features"));

    }

    @Test
    void withFeatures() {
        master.addFeature("not a shit-hole");
        master.addFeature("perfectly suitable for wife");

        BuildingPageMaker buildingPageMaker = new BuildingPageMaker();
        final String result = buildingPageMaker.createPage(master);

        assertTrue(result.contains("Features"));
    }
}
