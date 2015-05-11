/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mpurita
 */
public class ReadMapNameTest {

    public ReadMapNameTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of readMap method, of class ReadMapName.
     */
    @Test
    public void testReadMap() throws Exception {
        System.out.println("readMap");
        ReadMapName instance = null;
        String expResult = "";
        String result = instance.readMap();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPath method, of class ReadMapName.
     */
    @Test
    public void testGetPath() {
        System.out.println("getPath");
        ReadMapName instance = null;
        String expResult = "";
        String result = instance.getPath();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}