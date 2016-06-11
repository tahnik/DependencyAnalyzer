package com.tahnik.DependencyAnalyzer;

import java.util.ArrayList;

/**
 * Created by Tahnik Mustasin on 11/06/2016.
 */
public class Package {
    private String packageName = "";
    private ArrayList<Package> dependencies = null;

    public Package(String name){
        this.packageName = name;
    }

    private void initializeDependencies(){
        if(dependencies == null){
            dependencies = new ArrayList<>();
        }
    }

    public void setDependencies(Package pkg){
        initializeDependencies();
        dependencies.add(pkg);
    }

    public ArrayList<Package> getDependencies(){
        return dependencies;
    }
}
