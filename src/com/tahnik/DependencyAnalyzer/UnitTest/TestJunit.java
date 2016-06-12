package com.tahnik.DependencyAnalyzer.UnitTest;

import com.tahnik.DependencyAnalyzer.Package;
import com.tahnik.DependencyAnalyzer.utils.Utilities;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tahnik Mustasin on 10/06/2016.
 */
public class TestJunit extends TestCase {
    private Utilities utilities = null;

    protected void setUp(){
        utilities = Utilities.getInstance();
    }

    @Test
    public void testverifyLine(){
        assertFalse(utilities.verifyLine("gui nogui -> awtui swingui"));
        assertTrue(utilities.verifyLine("gui -> awtui swingui"));
        assertTrue(utilities.verifyLine("gui -> awtui        swingui"));
        assertTrue(utilities.verifyLine("    gui  -> awtui        swingui"));
        assertFalse(utilities.verifyLine("\"Promise me you'll look after your mother.\"\n"));
        assertFalse(utilities.verifyLine("'"));
        assertFalse(utilities.verifyLine("\""));
        assertFalse(utilities.verifyLine(".....,,,,*****$$$$$$"));
        assertFalse(utilities.verifyLine("#gui -> *awtui swin(gui"));
        assertFalse(utilities.verifyLine("213213546 -> gui swing"));
        assertFalse(utilities.verifyLine("\t\n"));
        assertFalse(utilities.verifyLine("->"));
        assertFalse(utilities.verifyLine("gui ->"));
        assertFalse(utilities.verifyLine("-> awtui swingui"));
        assertFalse(utilities.verifyLine("    "));
        assertFalse(utilities.verifyLine("gui->awtui"));
        assertFalse(utilities.verifyLine("gui->awtui swingui-> awtui"));
    }

    @Test
    public void testSplitLines(){
        ArrayList<String> test = new ArrayList<>();
        test.add("gui -> extensions framework");
        test.add("gui ->");
        test.add("gui -> extensions");
        utilities.splitLines(test);
    }

    @Test
    public void testPackages(){
        ConcurrentHashMap<String, Package> packages = utilities.getPackageList("test.txt");
        Set packageSet1 = packages.entrySet();
        Iterator packageIterator1 = packageSet1.iterator();
        while(packageIterator1.hasNext()) {
            Map.Entry me = (Map.Entry) packageIterator1.next();

            Package pkg = (Package) me.getValue();
            //System.out.println(pkg.getPackageName());
            ArrayList<Package> templist = pkg.getDependencies();
            System.out.print(pkg.getPackageName() + ": ");
            try {
                for (Package temp : templist) {
                    System.out.print(temp.getPackageName() + " ");
                }
            } catch (NullPointerException e) {
                //means there's no dependency information for this
            }
            System.out.println();
        }
    }

}