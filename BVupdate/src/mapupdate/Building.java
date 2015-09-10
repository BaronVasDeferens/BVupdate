/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapupdate;

import java.awt.Polygon;
import java.util.LinkedList;

/**
 *
 * @author skot
 */
public class Building {
    
    String name;
    String address;
    LinkedList<String> features;
    String overheadDoors;
    int length, width, height, sqFeet;
    int numManDoors, numRestrooms, numADA;
    boolean threePhase, hasAC;
    int monthlyRate;
    Polygon myPoly;
    
    //public Building(String name, String address, LinkedList<String> features, int length, int width, int monthlyRate, Polygon myPoly) {
    public Building(String name, String address, Polygon myPoly) {    
        this.name = name;
        this.myPoly = myPoly;
        this.address = address;
        
        this.features = new LinkedList();
                
        this.length = 0;
        this.width = 0;
        this.height = 0;
        this.sqFeet = 0;
        
        this.monthlyRate = 0;
        this.overheadDoors = null;
        this.numManDoors = 0;
        this.numRestrooms = 0;
        this.numADA = 0;
        
        this.threePhase = false;
        this.hasAC = false;
        
    }
    
    
    public void setSize(int length, int width, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
        
        this.sqFeet = length * width;
    }
    
    public void setDoors(int numManDoors, String overheadDoors) {
        this.numManDoors = numManDoors;
        this.overheadDoors = overheadDoors;
    }
    
    public void setRestrooms(int numRestrooms, int numADA) {
        this.numRestrooms = numRestrooms;
        this.numADA = numADA;
    }
    
    public void setRate(int monthlyRate) {
        this.monthlyRate = monthlyRate;
    }
    
    public void setOptions(boolean threePhase, boolean hasAC) {
        this.threePhase = threePhase;
        this.hasAC = hasAC;
    }
    
    public void addFeature(String feature) {
        features.add(feature);
    }
    
}
