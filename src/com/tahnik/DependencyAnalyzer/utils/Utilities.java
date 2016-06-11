package com.tahnik.DependencyAnalyzer.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Tahnik Mustasin on 10/06/2016.
 */
public class Utilities {
    /* This is singleton instance */
    private static Utilities utilities = null;

    /* Making the constructor private to prevent creating instances from other classes */
    private Utilities(){}

    /* returns the singleton instance */
    public static Utilities getInstance(){
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
                if(!verifyLine(line)){
                    System.out.println("Please use the right format for package and dependency\n" +
                            "A correct format is: gui -> swingui awtui\n");
                    System.exit(0);
                }else if(!checkIfLineIsEmpty(line)){
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public boolean verifyLine(String line){
        Boolean verificationPassed = true;

        /*  The first part checks if the line has a-z, whitespace and -> sign
            The second part checks if there's any special characters other than ->
            How second part works:
                The first part of the second check evaluates to true if there's any special characters. But the second
                part will become false if it's not -> separated by white space. Instead of (.*->.*) I have used
                [([a-z\s]*)]*\s->\s[([a-z\s]*)]* otherwise it will become false for any characters before or after
                ignoring any special characters.
         */
        if(!line.matches("([a-z\\s]*)(.*->.*)") || (line.matches(".*\\p{Punct}.*") && !line.matches("[([a-z\\s]*)]*\\s->\\s[([a-z\\s]*)]*"))){
            verificationPassed = false;
        }
        return verificationPassed;
    }

    public boolean checkIfLineIsEmpty(String line){
        return line.equals("");
    }

}
