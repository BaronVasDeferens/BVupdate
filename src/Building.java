import java.awt.*;
import java.util.ArrayList;

public class Building {

    public String name;
    public String address;
    public boolean isSubunit = false;

    public boolean isOccupied = true;
    public boolean shouldDraw = false;

    public ArrayList<String> features;
    public String overheadDoors;
    public int length, width, height, sqFeet;
    public int numManDoors, numRestrooms, numADA;
    public boolean threePhase, hasAC;
    public int monthlyRate;
    public Polygon polygon;


    public Building() {
    }

    public void setName(final String name) {
        this.name = name;
        if (this.name.contains("A") || this.name.contains("B")) {
            this.isSubunit = true;
        }
        features = new ArrayList<>();
    }

    public String getMasterBuildingName() {
        if (this.isSubunit) {
            return name.substring(0, this.name.length() - 1);
        } else {
            return this.name;
        }
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setIsOccupied(final boolean isOccupied) {
        this.isOccupied = isOccupied;
        this.shouldDraw = isOccupied;
    }

    public void setShouldDraw(final boolean shouldDraw) {
        this.shouldDraw = shouldDraw;
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

    public String toString() {
        String x = "NAME: " + this.name + " (" + this.address + ") ";
        x += this.isSubunit ? "SUBUNIT " : "";
        x += this.isOccupied ? "OCCUPIED " : "";
        x += this.shouldDraw ? "DRAW" : "";
        x += " $" + this.monthlyRate;

        return x;
    }

}
