/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometrytopology;

import static conformanceTest.ConformanceTestSuite.*;
import implementation.functionregistry.RegistryLoader;
import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haozhechen
 *
 * A.4.1.1 /conf/geometry-topology-extension/relate-query-function
 *
 * Requirement: /req/geometry-topology-extension/relate-query-function
 * Implementations shall support geof:relate as a SPARQL extension function,
 * consistent with the relate operator defined in Simple Features [ISO 19125-1].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving the
 * geof:relate function returns the correct result for a test dataset when using
 * the specified serialization and version.
 *
 * c.) Reference: Clause 9.2 Req 21
 *
 * d.) Test Type: Capabilities
 */
public class RelateQueryFunctionTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();
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
    public void pointTest() {

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#A");

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:relate(?aWKT, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, \"T*F**FFF*\") ."
                + "}";
        ArrayList<String> actualList = resourceQuery(Q1, infModel);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void lineTest() {

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#B");

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:relate(?aWKT, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LineString(-83.4 34.0, -83.3 34.3)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, \"TFFFTFFFT\") ."
                + "}";
        ArrayList<String> actualList = resourceQuery(Q1, infModel);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void polygonTest() {

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("http://example.org/ApplicationSchema#E");

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:relate(?aWKT, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Polygon((-83.2 34.3, -83.0 34.3, -83.0 34.5, -83.2 34.5, -83.2 34.3))\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, \"TFFFTFFFT\") ."
                + "}";
        ArrayList<String> actualList = resourceQuery(Q1, infModel);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void negativeTest() {

        String Q1 = "SELECT ?place WHERE{"
                + "?place ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + " FILTER geof:relate(?aWKT, \"<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-86.4 31.4)\"^^<http://www.opengis.net/ont/geosparql#wktLiteral>, \"TFFFTFFFT\") ."
                + "}";
        assertFalse(emptyQuery(Q1, infModel));
    }
}
