/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gerg
 */
public class ExpiringIndexTest {

    public ExpiringIndexTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of put method, of class ExpiringIndex.
     *
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testPut() throws InterruptedException {
        System.out.println("expiry");

        long expiryInterval = 2000l;
        long halfExpiryTime = expiryInterval / 2;

        ExpiringIndex<String, String> instance = new ExpiringIndex<>(5, expiryInterval, halfExpiryTime, "Test");
        instance.startExpiry();
        instance.put("key1", "value1");
        instance.put("key2", "value2");
        instance.put("key3", "value3");
        instance.put("key4", "value4");
        Thread.sleep(halfExpiryTime);
        instance.put("key5", "value5");
        instance.put("key6", "value6");
        System.out.println("Size Before: " + instance.size());
        Thread.sleep(expiryInterval);
        System.out.println("Size After: " + instance.size());
        int result = instance.size();
        int expResult = 1;

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
