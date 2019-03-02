import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildingMasterTests {

    @Test
    public void wholeUnitTest() {
        Building b = new Building();
        b.setName("15");
        assertFalse(b.isSubunit);
    }

    @Test
    public void subUnitTest() {
        Building b = new Building();
        b.setName("10A");
        assertTrue(b.isSubunit);
    }

    @Test
    @DisplayName("occupied sub-units of occupied master units should not render")
    public void occupiedMasterTest() {

        final HashMap<String, Building> allBuildings = new HashMap<>();

        Building master = new Building();
        master.setName("10");
        allBuildings.put(master.name, master);

        Building subUnitA = new Building();
        subUnitA.setName("10A");
        allBuildings.put(subUnitA.name, subUnitA);

        Building subUnitB = new Building();
        subUnitB.setName("10B");
        allBuildings.put(subUnitB.name, subUnitB);

        assertEquals(3, allBuildings.values().size());

        BuildingMaster buildingMaster = new BuildingMaster(allBuildings);
        buildingMaster.handleTransitiveOccupancy();

        assertTrue(master.isOccupied);
        assertTrue(master.shouldDraw);

        assertTrue(subUnitA.isOccupied);
        assertFalse(subUnitA.shouldDraw);

        assertTrue(subUnitB.isOccupied);
        assertFalse(subUnitB.shouldDraw);
    }

    @Test
    @DisplayName("available subunits should cause master unit not to render")
    public void occupiedSubunitTest() {

        final HashMap<String, Building> allBuildings = new HashMap<>();

        Building master = new Building();
        master.setName("10");
        allBuildings.put(master.name, master);

        Building subUnitA = new Building();
        subUnitA.setName("10A");
        allBuildings.put(subUnitA.name, subUnitA);

        // 10B is available
        Building subUnitB = new Building();
        subUnitB.setName("10B");
        subUnitB.setIsOccupied(false);
        allBuildings.put(subUnitB.name, subUnitB);

        assertEquals(3, allBuildings.values().size());

        BuildingMaster buildingMaster = new BuildingMaster(allBuildings);
        buildingMaster.handleTransitiveOccupancy();

        assertTrue(master.isOccupied);
        assertFalse(master.shouldDraw);

        assertTrue(subUnitA.isOccupied);
        assertTrue(subUnitA.shouldDraw);

        assertFalse(subUnitB.isOccupied);
        assertFalse(subUnitB.shouldDraw);
    }

    @Test
    @DisplayName("occupied subunits of occupied master units should not render")
    public void nothingTest() {

        final HashMap<String, Building> allBuildings = new HashMap<>();

        Building subUnitB = new Building();
        subUnitB.setName("10B");
        allBuildings.put(subUnitB.name, subUnitB);

        Building master = new Building();
        master.setName("10");
        allBuildings.put(master.name, master);

        Building subUnitA = new Building();
        subUnitA.setName("10A");
        allBuildings.put(subUnitA.name, subUnitA);


        BuildingMaster buildingMaster = new BuildingMaster(allBuildings);
        buildingMaster.handleTransitiveOccupancy();

        assertTrue(master.isOccupied);
        assertTrue(master.shouldDraw);

        assertTrue(subUnitA.isOccupied);
        assertFalse(subUnitA.shouldDraw);

        assertTrue(subUnitB.isOccupied);
        assertFalse(subUnitB.shouldDraw);
    }


}