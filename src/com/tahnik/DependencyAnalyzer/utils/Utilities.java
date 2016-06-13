package com.tahnik.DependencyAnalyzer.utils;

import com.tahnik.DependencyAnalyzer.Package;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tahnik Mustasin on 10/06/2016.
 * This file contains all the utility method needed for this program.
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
     * This method reads a file and returns a list of lines.
     * It uses verifyLine() to verify each lines.
     * Terminated if a line doesn't have at least a package name and "->"
     * @param fileName the filename from where to read the line
     * @return lines The individual lines from the file. Ignores blank line
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
            System.out.println("Can't read file");
            System.exit(0);
        }
        return lines;
    }

    /**
     * This method verifies the lines in a file using complex regex
     * @param line Takes one line
     * @return verificationPassed If the line has passed the verification
     */
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

    /**
     * Splits the lines by space using a Hashmap where Package name is the key and dependencies are value
     * Reason for using a Hashmap here is it's easy to find the package when creating package class later and add dependencies
     * @param lines Takes ArrayList of lines which will be split up
     * @return splitLines Hashmap of lines split in words
     */
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

    /**
     * Checks if line is empty
     * @param line A single line
     * @return boolean is line empty or not
     */
    public boolean checkIfLineIsEmpty(String line){
        return line.equals("");
    }

    /**
     * This method uses the Hashmap of splitLines and uses them to create packages and add their dependencies
     * @param filename takes the filename of txt file
     * @return packages the concurrent hashmap of packages where the each packages contains all the dependencies
     */
    public ConcurrentHashMap<String, Package> getPackageList(String filename){
        /*
        Checking is the file name is correct
         */
        if(!testFileName(filename)){
            System.out.println("Please enter a valid file name");
            System.exit(0);
        }
        ArrayList<String> lines =  readFile(filename);
        HashMap splitLines = splitLines(lines);

        /*
        Concurrent hash map is used as this map will be modified while being iterated.
         */
        ConcurrentHashMap<String, Package> packages = new ConcurrentHashMap<>();

        /*
        Iterating over spliteLines to create package using the key of spliteLines. Once all the package has been created
        their dependencies will be added.
         */
        Set lineSet = splitLines.entrySet();
        Iterator lineIterator = lineSet.iterator();
        while(lineIterator.hasNext()){
            Map.Entry me = (Map.Entry) lineIterator.next();
            packages.put(me.getKey().toString(), new Package(me.getKey().toString()));
        }

        /*
        Iterating over the packges hashmap to add the dependencies. The dependencies are added from the package list if
        exists. Otherwise a new one is created. For example for awtui -> runner and runner -> extensions, the runner already
        exists in the packages list because it is listed as a package in the text file. So rather than creating a new package
        object the one in the package hashmap os retrieved and used again, just like a real life scenario a package will be used
        over and over again as needed.
         */
        Set packageSet = packages.entrySet();
        Iterator packageIterator = packageSet.iterator();
        while(packageIterator.hasNext()){
            Map.Entry me = (Map.Entry) packageIterator.next();

            //getting the list of dependencies
            String[] line = (String[]) splitLines.get(me.getKey().toString());
            Package pkg = (Package) me.getValue();

            //for each dependency search it in the packages list. If it's not there create a new one
            for(String word : line){
                packages.putIfAbsent(word, new Package(word));
                pkg.setDependencies(packages.get(word));
            }
        }

        return packages;
    }

    /**
     * Prints the requested package and their dependencies.
     * @param args takes command line arguments to find the text file and package request
     */
    public void printPackageDependency(String[] args){
        String filename = args[0];
        String[] packagesToDisplay = new String[args.length - 1];
        if(args.length > 1){
            int j = 0;
            for(int i = 1 ; i < args.length ; i++){
                packagesToDisplay[j] = args[i];
                j++;
            }
        }else{
            //exits if there's no other argument other than filename
            System.out.println("Please specify some packages to find their dependencies.");
            System.exit(0);
        }

        //get the package lists which includes dependencies as well
        ConcurrentHashMap<String, Package> packages = utilities.getPackageList(filename);
        //this arraylist will be used in the recursive function to print dependencies
        ArrayList<String> dependencies = new ArrayList<>();

        //go through each package to display dependencies
        for(String pkg : packagesToDisplay){
            //adding the package itself as dependencies for circular dependency check.
            dependencies.add(pkg);

            //
            /*
            The pkgDependencies holds the initial dependencies not the transitive dependencies. The list dependencies will hold
            all the dependencies including the transitive ones after it finishes the printDependencies() recursive functions
             */
            try {
                ArrayList<Package> pkgDependecies = packages.get(pkg).getDependencies();
                printDependencies(pkgDependecies, dependencies, packages);
            }catch (NullPointerException e){
                //This means there's no dependency
            }

            //prints the package name first
            System.out.print(dependencies.get(0) + " -> ");
            //removes the package name so it can be used with a foreach loop to print all dependencies
            dependencies.remove(0);

            //sorting the dependencies alphabetically
            Collections.sort(dependencies);

            //printing all the dependencies
            for(String depTemp: dependencies){
                System.out.print(depTemp + " ");
            }
            System.out.println();

            //clearing the dependencies to reuse it for the next one
            dependencies.clear();
        }
    }

    /**
     * Tests if the filename exists and it's not a directory
     * @param filename The name of text file
     * @return boolean if the filename is correct
     */
    public boolean testFileName(String filename){
        File f = new File(filename);
        if(f.exists() && !f.isDirectory()){
            return true;
        }
        return false;
    }

    /**
     * A recursive function which goes through each dependencies to find out their transitive dependencies. The reason
     * to use a recursive function is that the Package class contains an arraylist of dependencies the type of which is also Package.
     * Because all this dependencies might have their own dependencies, a recursive function is the best way get them.
     * @param pkgs Initial dependencies of a package
     * @param dependencies Dependencies which will include transitive dependencies
     * @param packages List of packages from the text file
     */
    public void printDependencies(ArrayList<Package> pkgs, ArrayList<String> dependencies, ConcurrentHashMap<String, Package> packages){
        ArrayList<Package> temporaryPackages = null;

        //If it goes to catch that means there's no more transitive dependencies
        try {
            //go through each dependencies
            for (Package pkg : pkgs) {
                /*
                First part checks if the dependency package exists in the package list from the file
                Second part checks if the dependencies already contain the package preventing circular dependencies
                 */
                if (packages.get(pkg.getPackageName()) != null && !dependencies.contains(pkg.getPackageName())) {
                    dependencies.add(pkg.getPackageName());
                    temporaryPackages = pkg.getDependencies();
                    printDependencies(temporaryPackages, dependencies, packages);
                }
            }
        }catch (NullPointerException e){
            return;
        }
    }

}
