package com.tahnik.DependencyAnalyzer.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Tahnik Mustasin on 10/06/2016.
 * The reason for making this utilities class singleton rather than static is to make unit test
 * easier on the class.
 */
public class Utilities {
    /* This is singleton instance */
    private Utilities utilities = null;

    /* Making the constructor private to prevent creating instances from other classes */
    private Utilities(){}

    /* returns the singleton instance */
    public Utilities getInstance(){
        if(utilities == null){
            utilities = new Utilities();
        }
        return utilities;
    }

    /**
     * This method reads a file and returns a list of lines
     * @param fileName
     */
    public ArrayList<String> readFile(String fileName){
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                lines.add(line);
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

}
