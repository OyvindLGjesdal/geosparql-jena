/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_topology_extension.rcc8_query_functions;

import static conformance_test.ConformanceTestSuite.*;
import implementation.GeoSPARQLModel;

import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
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
 * A.4.4.1 /conf/geometry-topology-extension/rcc8-query-functions
 *
 * Requirement: /req/geometry-topology-extension/rcc8-query-functions
 * Implementations shall support geof:rcc8eq, geof:rcc8dc, geof:rcc8ec,
 * geof:rcc8po, geof:rcc8tppi, geof:rcc8tpp, geof:rcc8ntpp, geof:rcc8ntppi as
 * SPARQL extension functions, consistent with their corresponding DE-9IM
 * intersection patterns, as defined by Simple Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving each of the
 * following functions returns the correct result for a test dataset when using
 * the specified serialization and version: geof:rcc8eq, geof:rcc8dc,
 * geof:rcc8ec, geof:rcc8po, geof:rcc8tppi, geof:rcc8tpp, geof:rcc8ntpp,
 * geof:rcc8ntppi.
 *
 * c.) Reference: Clause 9.5 Req 24
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8tppTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        
        infModel = initWktModel();
    }
    private static InfModel infModel;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void positiveTest() {

        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("http://example.org/ApplicationSchema#C");

        String queryString = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:rcc8tpp(\"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.6 34.1, -83.4 34.1, -83.4 34.3, -83.6 34.3, -83.6 34.1))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, ?aWKT) ."
                + "}";
        ArrayList<String> result = resourceQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

    @Test
    public void negativeTest() {

        String queryString = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:rcc8tpp(\"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.3 34.0, -83.1 34.0, -83.1 34.2, -83.3 34.2, -83.3 34.0))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, ?aWKT) ."
                + "}";
        ArrayList<String> expResult = new ArrayList<>();

        assertEquals(expResult, resourceQuery(queryString, infModel));
    }

}