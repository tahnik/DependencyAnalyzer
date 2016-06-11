package com.tahnik.DependencyAnalyzer.UnitTest;

import com.tahnik.DependencyAnalyzer.utils.Utilities;
import junit.framework.TestCase;
import org.junit.Test;

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
}