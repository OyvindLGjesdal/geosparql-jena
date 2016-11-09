/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static main.Main.init;
import main.TopologyRegistryLevel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author haozhechen
 *
 * A.3.2.3 /conf/geometry-extension/wkt-axis-order
 *
 * Requirement: /req/geometry-extension/wkt-axis-order
 * Coordinate tuples within geo:wktLiterals shall be interpreted using
 * the axis order defined in the spatial reference system used.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:wktLiteral values
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.5.1 Req 12
 *
 * d.) Test Type: Capabilities
 */
public class WktAxisOrderTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        init(TopologyRegistryLevel.DEFAULT);
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

}
