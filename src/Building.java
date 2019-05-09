import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Building implements Comparable {

    public String name;
    public String address;

    private final Set<Building> subunits;

    // By default buildings are occupied and should draw on the map
    public boolean isOccupied = true;
    public boolean shouldDraw = true;

    public ArrayList<String> features;
    public String overheadDoors;
    public int length, width, height, sqFeet;
    public int numManDoors, numRestrooms, numADA;
    public boolean threePhase, hasAC;
    public int monthlyRate;
    public Polygon polygon;


    public Building() {
        subunits = new HashSet<>();
        features = new ArrayList<>();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getMasterBuildingName() {
        if (!isSubunit()) return "";
        return name.substring(0, name.length() - 1);
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setIsOccupied(final boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setShouldDraw(final boolean shouldDraw) {
        this.shouldDraw = shouldDraw;
    }

    public boolean isSubunit() {
        return !subunits.isEmpty();
    }

    public void setPolygon(final Polygon polygon) {
        this.polygon = polygon;
    }

    public void setFeatures(final ArrayList<String> features) {
        this.features = features;
    }

    public void setSize(final int length, final int width, final int height) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.sqFeet = length * width;
    }

    public String getDimensions() {
        return this.length + "x" + this.width + "x" + this.height;
    }

    public String getSquareFootage() {
        return String.valueOf(this.length * this.width);
    }

    public void setDoors(final int numManDoors, final String overheadDoors) {
        this.numManDoors = numManDoors;
        this.overheadDoors = overheadDoors;
    }

    public String getManDoors() {
        return String.valueOf(this.numManDoors);
    }

    public void setRestrooms(final int numRestrooms, final int numADA) {
        this.numRestrooms = numRestrooms;
        this.numADA = numADA;
    }

    public String getRestrooms() {
        return numRestrooms + " (" + this.numADA + " ADA access)";
    }

    public void setThreePhase(final String threePhase) {
        if (threePhase.equalsIgnoreCase("yes")) {
            this.threePhase = true;
        }
    }

    public String hasThreePhase() {
        return this.threePhase ? "YES" : "NO";
    }

    public void setAirConditioning(final String ac) {
        if (ac.equalsIgnoreCase("yes")) {
            this.hasAC = true;
        }
    }

    public String hasAirConditioning() {
        return hasAC ? "YES" : "NO";
    }

    public void setRate(final int monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public String getMonthlyRate() {
        return String.valueOf(this.monthlyRate);
    }

    public void addFeature(final String feature) {
        features.add(feature);
    }

    public void addSubunit(final Building sub) {
        subunits.add(sub);
    }

    public Set<Building> getSubunits() {
        return subunits;
    }

    public int comparedTo(final Building compareAgainst) {
        final String modifiedName = this.name.replace("A", "").replace("B", "");
        final String comparedAgainstName = compareAgainst.name.replace("A", "").replace("B", "");

        try {
            final int myName = Integer.valueOf(modifiedName);
            final int yourName = Integer.valueOf(comparedAgainstName);
            if (myName > yourName)  return -1;
            else if (myName < yourName) return 1;
            else return 0;
        } catch (final Exception e) {
            System.out.println(e.toString());
            return 0;
        }
    }

    public String toString() {
        String x = "NAME: " + this.name + " (" + this.address + ") ";
        x += this.isSubunit() ? "SUBUNIT " : "MASTER ";
        x += this.isOccupied ? "OCCUPIED " : "AVAILABLE ";
        x += this.shouldDraw ? "DRAW " : "";
        x += " $" + this.monthlyRate;

        return x;
    }

    @Override
    public int compareTo(Object o) {
        try {
            final Building b = (Building) o;
            return b.comparedTo(this);
        } catch (final Exception e) {
            System.out.println(e.toString());
            return 0;
        }
    }
}
