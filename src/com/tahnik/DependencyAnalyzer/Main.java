package com.tahnik.DependencyAnalyzer;

import com.tahnik.DependencyAnalyzer.utils.Utilities;

public class Main{
    public static void main(String[] args){
        Utilities utilities = Utilities.getInstance();
        utilities.readFile("test.txt");
    }
}