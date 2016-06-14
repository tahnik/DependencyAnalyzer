import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tahnik Mustasin on 10/06/2016.
 * Unit test using JUnitTest.
 */
public class TestJunit extends TestCase {
    private Utilities utilities = null;

    /**
     * Sets up the utilities object
     */
    protected void setUp(){
        utilities = Utilities.getInstance();
    }

    /**
     * Tests different type of lines to verify
     */
    @Test
    public void testverifyLine(){
        System.out.println("Testing line verifications");
        assertFalse(utilities.verifyLine("gui nogui -> awtui swingui"));
        assertTrue(utilities.verifyLine("gui -> awtui swingui"));
        assertTrue(utilities.verifyLine("gui -> awtui        swingui"));
        assertTrue(utilities.verifyLine("    gui  -> awtui        swingui"));
        assertFalse(utilities.verifyLine("\"Promise me you'll look after your mother.\"\n"));
        assertFalse(utilities.verifyLine("'"));
        assertFalse(utilities.verifyLine("\""));
        assertFalse(utilities.verifyLine(".....,,,,*****$$$$$$"));
        assertFalse(utilities.verifyLine("#gui -> *awtui swin(gui"));
        assertTrue(utilities.verifyLine("213213546 -> gui swing"));
        assertFalse(utilities.verifyLine("\t\n"));
        assertFalse(utilities.verifyLine("->"));
        assertFalse(utilities.verifyLine("gui ->"));
        assertFalse(utilities.verifyLine("-> awtui swingui"));
        assertFalse(utilities.verifyLine("    "));
        assertFalse(utilities.verifyLine("gui->awtui"));
        assertFalse(utilities.verifyLine("gui->awtui swingui-> awtui"));
        assertTrue(utilities.verifyLine("gui1 -> awtui"));
        System.out.println("\n");
    }

    /**
     * Tests if the lines are split correctly
     */
    @Test
    public void testSplitLines(){
        System.out.println("Testing spliting the lines into words");
        ArrayList<String> test = new ArrayList<>();
        test.add("gui -> extensions framework");
        test.add("gui ->");
        test.add("gui -> extensions");
        utilities.splitLines(test);
        System.out.println("\n");
    }

    /**
     * Tests if the packages are added correctly
     */
    @Test
    public void testPackages(){
        System.out.println("Testing package creation from file");
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
        System.out.println("\n");
    }

    /**
     * Tests if the printing dependencies works
     */
    @Test
    public void testPrintPackageDependencies(){
        System.out.println("Testing package dependencies");
        String args[] = {"test.txt", "gui", "framework", "runner", "pkg23"};
        utilities.printPackageDependency(args);
        System.out.println("\n");
    }

    /**
     * Tests if the file name checking works
     */
    @Test
    public void testFileName(){
        System.out.println("Testing file name.....");
        String args[] = {"test.txt", "gui", "framework", "runner"};
        utilities.printPackageDependency(args);
        System.out.println("\n");
    }
}