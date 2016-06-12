package com.tahnik.DependencyAnalyzer;

import java.util.ArrayList;

/**
 * Created by Tahnik Mustasin on 11/06/2016.
 */
public class Package {
    private String packageName = "";
    private ArrayList<Package> dependencies = null;

    public Package(String name){
        setPackageName(name);
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
