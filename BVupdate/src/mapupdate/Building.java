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
    int length, width, sqFeet;
    int monthlyRate;
    Polygon myPoly;
    
    //public Building(String name, String address, LinkedList<String> features, int length, int width, int monthlyRate, Polygon myPoly) {
    public Building(String name, Polygon myPoly) {    
        this.name = name;
//        this.address = address;
//        this.features = features;
//        this.length = length;
//        this.width = width;
//        this.sqFeet = length * width;
//        this.monthlyRate = monthlyRate;
        this.myPoly = myPoly;
    }
    
}
