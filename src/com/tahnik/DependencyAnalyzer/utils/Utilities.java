package com.tahnik.DependencyAnalyzer.utils;

import com.tahnik.DependencyAnalyzer.Package;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
            The third part check if there's two package name before ->
            How second part works:
                The first part of the second check evaluates to true if there's any special characters. But the second
                part will become false if it's not -> separated by white space. Instead of (.*->.*) I have used
                [([a-z\s]*)]*\s->\s[([a-z\s]*)]* otherwise it will become false for any characters before or after
                ignoring any special characters.
            The third part works in a similar way
         */
        if(!line.matches("([a-z\\s]*)(.*->.*)")
                || (line.matches(".*\\p{Punct}.*") && !line.matches("[([a-z\\s]*)]*\\s->\\s[([a-z\\s]*)]*"))
                || (line.matches("[\\s]*[a-z]*[\\s]*[a-z]*[\\s]*->.*") && !line.matches("[\\s]*[a-z]*[\\s]*->.*")) ){
            verificationPassed = false;
        }
        return verificationPassed;
    }

    public HashMap splitLines(ArrayList<String> lines){
        HashMap splitLines = new HashMap();

        for (String line: lines) {
            String[] lineArray = line.split("\\s+");
            String packageName = lineArray[0];
            String[] dependencies = new String[lineArray.length - 2];
            if(lineArray.length > 1){
                int j = 0;
                for(int i = 2; i < lineArray.length ; i++){
                    dependencies[j] = lineArray[i];
                    j++;
                }
            }
            splitLines.put(packageName, dependencies);
        }
        return splitLines;
    }

    public boolean checkIfLineIsEmpty(String line){
        return line.equals("");
    }

    public ConcurrentHashMap<String, Package> getPackageList(String filename){
        ArrayList<String> lines =  readFile(filename);
        HashMap splitLines = splitLines(lines);

        /*
        Concurrent hash map is used as this map will be modified while being iterated
         */
        ConcurrentHashMap<String, Package> packages = new ConcurrentHashMap<>();

        Set lineSet = splitLines.entrySet();
        Iterator lineIterator = lineSet.iterator();
        while(lineIterator.hasNext()){
            Map.Entry me = (Map.Entry) lineIterator.next();

            packages.put(me.getKey().toString(), new Package(me.getKey().toString()));
        }

        Set packageSet = packages.entrySet();
        Iterator packageIterator = packageSet.iterator();
        while(packageIterator.hasNext()){
            Map.Entry me = (Map.Entry) packageIterator.next();

            String[] line = (String[]) splitLines.get(me.getKey().toString());
            Package pkg = (Package) me.getValue();
            for(String word : line){
                packages.putIfAbsent(word, new Package(word));
                pkg.setDependencies(packages.get(word));
            }
        }

        return packages;
    }

    public void printPackageDependency(String[] args){
        String filename = args[0];
        String[] packagesToDisplay = new String[args.length - 1];
        if(args.length > 1){
            int j = 0;
            for(int i = 1 ; i < args.length ; i++){
                packagesToDisplay[j] = args[i];
                j++;
            }
        }
        ConcurrentHashMap<String, Package> packages = utilities.getPackageList(filename);
        ArrayList<String> dependencies = new ArrayList<>();
        for(String pkg : packagesToDisplay){
            System.out.print(pkg + " -> ");

            try {
                ArrayList<Package> pkgDependecies = packages.get(pkg).getDependencies();
                printDependencies(pkgDependecies, dependencies, packages);
            }catch (NullPointerException e){
                //This means there's no dependency
            }


            Collections.sort(dependencies);
            for(String depTemp: dependencies){
                System.out.print(depTemp + " ");
            }
            System.out.println();
            dependencies.clear();
        }
    }

    public void printDependencies(ArrayList<Package> pkgs, ArrayList<String> dependencies, ConcurrentHashMap<String, Package> packages){
        ArrayList<Package> temporaryPackages = null;
        try {
            for (Package pkg : pkgs) {
                if(!dependencies.contains(pkg.getPackageName())) {
                    dependencies.add(pkg.getPackageName());
                }
                if (packages.get(pkg.getPackageName()) != null) {
                    temporaryPackages = pkg.getDependencies();
                    printDependencies(temporaryPackages , dependencies, packages);
                }
            }
        }catch (NullPointerException e){
            return;
        }
    }

}
