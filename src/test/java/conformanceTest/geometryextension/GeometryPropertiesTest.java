/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import java.util.ArrayList;
import static main.Main.init;
import main.TopologyRegistryLevel;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import vocabulary.Prefixes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 *
 * A.3.1.3 /conf/geometry-extension/geometry-properties
 *
 * Requirement: /req/geometry-extension/geometry-properties
 * Implementations shall allow the properties geo:dimension,
 * geo:coordinateDimension, geo:spatialDimension, geo:isEmpty,
 * geo:isSimple, geo:hasSerialization to be used in SPARQL graph
 * patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving these properties
 * return the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.4 Req 9
 *
 * d.) Test Type: Capabilities
 */
public class GeometryPropertiesTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        init(TopologyRegistryLevel.DEFAULT);
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

        this.expectedList.add("0^^http://www.w3.org/2001/XMLSchema#integer");
        this.expectedList.add("0^^http://www.w3.org/2001/XMLSchema#integer");
        this.expectedList.add("0^^http://www.w3.org/2001/XMLSchema#integer");
        this.expectedList.add("false^^http://www.w3.org/2001/XMLSchema#boolean");
        this.expectedList.add("true^^http://www.w3.org/2001/XMLSchema#boolean");

        String Q1 = "SELECT ?dimension ?coordinateDimension ?spatialDimension ?isEmpty ?isSimple WHERE{"
                + " ntu:A geo:hasGeometry ?aGeom ."
                + " ?aGeom geo:dimension ?dimension ."
                + " ?aGeom geo:coordinateDimension ?coordinateDimension ."
                + " ?aGeom geo:spatialDimension ?spatialDimension ."
                + " ?aGeom geo:isEmpty ?isEmpty ."
                + " ?aGeom geo:isSimple ?isSimple ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Literal dimension = solution.getLiteral("?dimension");
                Literal coordinateDimension = solution.getLiteral("?coordinateDimension");
                Literal spatialDimension = solution.getLiteral("?spatialDimension");
                Literal isEmpty = solution.getLiteral("?isEmpty");
                Literal isSimple = solution.getLiteral("?isSimple");
                this.actualList.add(dimension.toString());
                this.actualList.add(coordinateDimension.toString());
                this.actualList.add(spatialDimension.toString());
                this.actualList.add(isEmpty.toString());
                this.actualList.add(isSimple.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }

}
