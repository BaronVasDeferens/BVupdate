import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BuildingMasterTests {

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

        assertFalse(buildings.get(master.name).isOccupied);
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

        assertEquals(2, buildings.values().size());
        assertFalse(buildings.get(subUnitB.name).shouldDraw);
        assertTrue(buildings.get(subUnitA.name).shouldDraw);
    }

    @Test
    @DisplayName("derp")
    void derp() {

    }

}