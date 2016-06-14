package com.tahnik.DependencyAnalyzer;

import java.util.ArrayList;

/**
 * Created by Tahnik Mustasin on 11/06/2016.
 * This class will contain all the dependencies of a package. The ArrayList of dependencies is also type of Package.
 * As in real life I dependencies are also packages. In this way it's easy to find transitive dependencies with recursion
 */
public class Package {
    private String packageName = "";
    private ArrayList<Package> dependencies = null;

    public Package(String name){
        setPackageName(name);
    }

    /**
     * If there's no dependencies then the ArrayList stays null and will be caught saying there's no dependencies.
     * If a dependency is being added it will be initialized here first
     */
    private void initializeDependencies(){
        if(dependencies == null){
            dependencies = new ArrayList<>();
        }
    }

    /**
     * Set the dependencies of a package
     * @param pkg takes a package
     */
    public void setDependencies(Package pkg){
        initializeDependencies();
        dependencies.add(pkg);
    }

    /**
     * Get the dependencies of a package
     * @return the ArrayList of dependencies
     */
    public ArrayList<Package> getDependencies(){
        return dependencies;
    }

    /**
     * Get the name of the package
     * @return packageName Name of package
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Set the name of the package
     * @param packageName The name of package to be set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
