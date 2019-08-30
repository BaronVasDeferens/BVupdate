import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BuildingMasterTests {

    @Test
    @DisplayName("Building without subunits should not report as subunit")
    void subUnitTest() {
        final Building building = new Building();
        building.setName("10");
        assertFalse(building.isSubunit());
        assertEquals(building.getSubunits().size(), 0);
    }

    @Test
    @DisplayName("unoccupied building without sub-units should render as a single building")
    void legacyTest() {
        final Building b = new Building();
        b.setName("2");
        b.setIsOccupied(false);

        final HashMap<String, Building> allBuildings = new HashMap<>();
        allBuildings.put(b.name, b);
        BuildingMaster buildingMaster = new BuildingMaster(allBuildings);
        buildingMaster.handleTransitiveOccupancy();

        assertEquals(buildingMaster.getBuildings().size(), 1);
    }

    @Test
    @DisplayName("WTF is going on")
    void errorCaseTest() {

        final HashMap<String, Building> allBuildings = new HashMap<>();

        final Building b1 = new Building();
        b1.setName("1");
//        b1.setIsOccupied(true);
        allBuildings.put(b1.name, b1);

        final Building b2 = new Building();
        b2.setName("10");
        allBuildings.put(b2.name, b2);

//        final Building b2a = new Building();
//        b2a.setName("11A");
//        b2a.setIsOccupied(false);
//
//        final Building b2b = new Building();
//        b2b.setName("11B");
//        b2b.setIsOccupied(true);
//
//        b2.addSubunit(b2a);
//        b2.addSubunit(b2b);
//
//
//        allBuildings.put(b2a.name, b2a);
//        allBuildings.put(b2b.name, b2b);

        BuildingMaster buildingMaster = new BuildingMaster(allBuildings);
        buildingMaster.handleTransitiveOccupancy();

        assertEquals(buildingMaster.getBuildings().size(), buildingMaster.getAllBuildings().size());

    }


    @Test
    @DisplayName("sub-units of an occupied master units should not render")
    public void occupiedMasterTest() {

        final HashMap<String, Building> allBuildings = new HashMap<>();

        Building master = new Building();
        master.setName("10");
        master.setIsOccupied(false);
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

        final Map<String,Building> buildings = buildingMaster.getBuildings();

        assertFalse(buildings.get(master.name).getIsOccupied());
        assertFalse(buildings.get(master.name).shouldDraw);

        assertEquals(3, buildings.values().size());

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

        BuildingMaster buildingMaster = new BuildingMaster(allBuildings);
        buildingMaster.handleTransitiveOccupancy();

        final Map<String,Building> buildings = buildingMaster.getBuildings();

        assertEquals(3, buildings.values().size());
        assertFalse(buildings.get(subUnitB.name).shouldDraw);
        assertTrue(buildings.get(subUnitA.name).shouldDraw);
    }

}