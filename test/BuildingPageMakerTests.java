import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BuildingPageMakerTests {

    @Test
    public void basicTest() {
        Building master = new Building();
        master.setName("14");
        master.setAddress("1122 Boogie Boogie Ave.");
        master.setSize(5,6,7);
        master.setRestrooms(2,1);
        master.setAirConditioning("yes");
        master.setThreePhase("YES");
        master.setDoors(2, "one 12x12");
        master.setRate(1200);

//        master.addFeature("not a shit-hole");
//        master.addFeature("perfectly suitable for wife");

        master.setIsOccupied(false);

        BuildingPageMaker buildingPageMaker = new BuildingPageMaker();
        final String result = buildingPageMaker.createPage(master);

        assertFalse(result.contains("Features"));


    }
}
