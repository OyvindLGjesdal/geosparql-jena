/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.*;
import static implementation.functionregistry.RegistryLoader.load;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 *
 * A.3.1.4 /conf/geometry-extension/query-functions
 *
 * Requirement: /req/geometry-extension/query-functions
 * Implementations shall support geof:distance, geof:buffer,
 * geof:convexHull, geof:intersection, geof:union, geof:difference,
 * geof:symDifference, geof:envelope and geof:boundary as SPARQL
 * extension functions, consistent with the definitions of the
 * corresponding functions (distance, buffer, convexHull, intersection,
 * difference, symDifference, envelope and boundary respectively) in
 * Simple Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each
 * of the following functions returns the correct result for a test
 * dataset when using the specified serialization and version:
 * geof:distance, geof:buffer, geof:convexHull, geof:intersection,
 * geof:union, geof:difference, geof:symDifference, geof:envelope and
 * geof:boundary.
 *
 * c.) Reference: Clause 8.7 Req 19
 *
 * d.) Test Type: Capabilities
 */
public class QueryFunctionsBufferTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
        initWktModel();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private ArrayList expectedList;
    private ArrayList actualList;

    @Before
    public void setUp() {
        this.expectedList = new ArrayList<>();
        this.actualList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        this.actualList.clear();
        this.expectedList.clear();
    }

    @Test
    public void positiveTest() {

        this.expectedList.add("http://example.org/ApplicationSchema#F");
        this.expectedList.add("http://example.org/ApplicationSchema#E");
        this.expectedList.add("http://example.org/ApplicationSchema#D");
        this.expectedList.add("http://example.org/ApplicationSchema#C");
        this.expectedList.add("http://example.org/ApplicationSchema#B");
        this.expectedList.add("http://example.org/ApplicationSchema#A");

        String Q1 = "SELECT ?place WHERE{ "
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " ex:B ex:hasExactGeometry ?bGeom . "
                + " ?bGeom geo:asWKT ?bWkt . "
                + " BIND( geof:buffer(?bWkt, 100000, uom:metre) AS ?buffer) . "
                + " FILTER ( geof:sfIntersects(?aWkt, ?buffer) )"
                + " }";
        this.actualList = resourceQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

    /**
     * The increase of the radius should lead the buffer intersects
     * with ex:E as well, compare to the result of positiveTest.
     */
    @Test
    public void anotherPositiveTest() {

        this.expectedList.add("http://example.org/ApplicationSchema#F");
        this.expectedList.add("http://example.org/ApplicationSchema#D");
        this.expectedList.add("http://example.org/ApplicationSchema#C");
        this.expectedList.add("http://example.org/ApplicationSchema#B");

        String Q1 = "SELECT ?place WHERE{ "
                + " ?place ex:hasExactGeometry ?aGeom . "
                + " ?aGeom geo:asWKT ?aWkt . "
                + " ex:B ex:hasExactGeometry ?bGeom . "
                + " ?bGeom geo:asWKT ?bWkt . "
                + " BIND( geof:buffer(?bWkt, 10000, uom:metre) AS ?buffer) . "
                + " FILTER ( geof:sfIntersects(?aWkt, ?buffer) )"
                + " }";
        this.actualList = resourceQuery(Q1, INF_WKT_MODEL);
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
