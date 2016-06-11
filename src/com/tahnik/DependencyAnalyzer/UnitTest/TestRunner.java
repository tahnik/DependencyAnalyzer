package com.tahnik.DependencyAnalyzer.UnitTest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by Tahnik Mustasin on 10/06/2016.
 */
public class TestRunner {
    public static void main(String[] args){
        Result result = JUnitCore.runClasses(TestJunit.class);
        for(Failure failure: result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
