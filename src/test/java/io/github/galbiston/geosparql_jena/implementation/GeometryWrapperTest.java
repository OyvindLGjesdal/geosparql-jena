/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.implementation;

import io.github.galbiston.geosparql_jena.implementation.datatype.GMLDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomGeometryFactory;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.sis.referencing.CRS;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class GeometryWrapperTest {

    GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    public GeometryWrapperTest() {
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
     * Test of checkTransformCRS method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCheckCRS() throws Exception {
        System.out.println("checkCRS");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper sourceCRSGeometry = new GeometryWrapper(geometry, sourceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        //Only the SRS_URI is important in the instance.
        String targetSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper instance = new GeometryWrapper(geometry, targetSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        //Expecting the coordinates to be reveresed.
        Geometry geometryTarget = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        GeometryWrapper expResult = new GeometryWrapper(geometryTarget, targetSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());
        GeometryWrapper result = instance.checkTransformCRS(sourceCRSGeometry);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCRS method, of class GeometryWrapper.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testGetCRS() throws FactoryException {
        System.out.println("getCRS");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        CoordinateReferenceSystem expResult = CRS.forCode(sourceSRSURI);
        CoordinateReferenceSystem result = instance.getCRS();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getXYGeometry method, of class GeometryWrapper.
     */
    @Test
    public void testGetXYGeometry() {
        System.out.println("getXYGeometry");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        //Expect the coordinates to be reversed as JTS is x,y and WGS84 is y,x
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        Geometry result = instance.getXYGeometry();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getParsingGeometry method, of class GeometryWrapper.
     */
    @Test
    public void testGetParsingGeometry() {
        System.out.println("getParsingGeometry");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        //Expect coordinates to be same as supplied.
        Geometry expResult = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        Geometry result = instance.getParsingGeometry();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSrsURI method, of class GeometryWrapper.
     */
    @Test
    public void testGetSrsURI() {
        System.out.println("getSrsURI");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        String expResult = SRS_URI.WGS84_CRS;
        String result = instance.getSrsURI();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSRID method, of class GeometryWrapper.
     */
    @Test
    public void testGetSRID() {
        System.out.println("getSRID");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        String expResult = SRS_URI.WGS84_CRS;
        String result = instance.getSRID();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGeometryDatatypeURI method, of class GeometryWrapper.
     */
    @Test
    public void testGetGeometryDatatypeURI() {
        System.out.println("getGeometryDatatypeURI");
        Geometry geometry = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 2.0));
        String sourceSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper instance = new GeometryWrapper(geometry, sourceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        String expResult = WKTDatatype.URI;
        String result = instance.getGeometryDatatypeURI();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of distance same SRS_URI method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceSameCRSSameUnit() throws Exception {
        System.out.println("distance, same CRS, same Unit");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        String targetSRSURI = SRS_URI.OSGB36_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(12.0, 1.0));
        String instanceSRSURI = SRS_URI.OSGB36_CRS;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        //CRS is in metres.
        String distanceUnitsURL = Unit_URI.METRE_URL;

        double expResult = 10.0;
        double result = instance.distance(targetGeometry, distanceUnitsURL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of distance same SRS_URI method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceSameCRSDifferentUnit() throws Exception {
        System.out.println("distance, same CRS, different Unit");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(385458, 156785)); //LatLon - 51.31, -2.21
        String targetSRSURI = SRS_URI.OSGB36_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(487920, 157518)); //LatLon: 51.31, -0.74
        String instanceSRSURI = SRS_URI.OSGB36_CRS;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        //CRS is in metres.
        String distanceUnitsURL = Unit_URI.RADIAN_URL;

        double expResult = 0.025656; //Degree: 1.47
        double result = instance.distance(targetGeometry, distanceUnitsURL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.000001);
    }

    /**
     * Test of distance different SRS_URI method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceDifferentCRSSameUnit() throws Exception {
        System.out.println("distance, different CRS, same Unit");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 1.0));
        String targetSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 12.0));
        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        //CRS is in degrees.
        String distanceUnitsURL = Unit_URI.DEGREE_URL;

        double expResult = 10.0;
        double result = instance.distance(targetGeometry, distanceUnitsURL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of distance different SRS_URI method, of class GeometryWrapper.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDistanceDifferentCRSDifferentUnit() throws Exception {
        System.out.println("distance, different CRS, different Unit");

        Geometry targetGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(0.0, 1.0));
        String targetSRSURI = SRS_URI.WGS84_CRS;
        GeometryWrapper targetGeometry = new GeometryWrapper(targetGeo, targetSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint(new Coordinate(2.0, 0.0));
        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper instance = new GeometryWrapper(instanceGeo, instanceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        //CRS is in degrees.
        String distanceUnitsURL = Unit_URI.METRE_URL;

        double expResult = 111319.49079327357; //1.0 degree of longigtude at the equator is approx 111.32km.
        double result = instance.distance(targetGeometry, distanceUnitsURL);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of empty WKT GeometryWrapper.
     *
     */
    @Test
    public void testEmptyWKT() {
        System.out.println("emptyWKT");
        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint();
        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper result = new GeometryWrapper(instanceGeo, instanceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        GeometryWrapper expResult = GeometryWrapper.getEmptyWKT();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of empty WKT GeometryWrapper.
     *
     */
    @Test
    public void testEmptyWKTGeometryWrapper() {
        System.out.println("emptyWKTGeometryWrapper");

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint();
        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper result = new GeometryWrapper(instanceGeo, instanceSRSURI, WKTDatatype.URI, DimensionInfo.XY_POINT());

        GeometryWrapper expResult = GeometryWrapper.getEmptyWKT();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of empty GML GeometryWrapper.
     *
     */
    @Test
    public void testEmptyGMLGeometryWrapper() {
        System.out.println("emptyGMLGeometryWrapper");

        Geometry instanceGeo = GEOMETRY_FACTORY.createPoint();
        String instanceSRSURI = SRS_URI.DEFAULT_WKT_CRS84;
        GeometryWrapper result = new GeometryWrapper(instanceGeo, instanceSRSURI, GMLDatatype.URI, DimensionInfo.XY_POINT());

        GeometryWrapper expResult = GeometryWrapper.getEmptyGML();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of asLiteral.
     *
     */
    @Test
    public void testAsLiteral() {
        System.out.println("asLiteral");

        String lexicalForm = "POINT(-83.38 33.95)";
        GeometryWrapper instance = WKTDatatype.INSTANCE.parse(lexicalForm);

        Literal result = instance.asLiteral();
        Literal expResult = ResourceFactory.createTypedLiteral(lexicalForm, WKTDatatype.INSTANCE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of asLiteral conversion URI.
     *
     */
    @Test
    public void testAsLiteralConversionURI() {
        System.out.println("asLiteralConversionURI");

        String lexicalForm = "POINT(-83.38 33.95)";
        GeometryWrapper instance = WKTDatatype.INSTANCE.parse(lexicalForm);

        Literal result = instance.asLiteral(GMLDatatype.URI);
        String gmlGeometryLiteral = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";
        Literal expResult = ResourceFactory.createTypedLiteral(gmlGeometryLiteral, GMLDatatype.INSTANCE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of asLiteral conversion datatype.
     *
     */
    @Test
    public void testAsLiteralConversionDatatype() {
        System.out.println("asLiteralConversionDatatype");

        String lexicalForm = "POINT(-83.38 33.95)";
        GeometryWrapper instance = WKTDatatype.INSTANCE.parse(lexicalForm);

        Literal result = instance.asLiteral(GMLDatatype.INSTANCE);
        String gmlGeometryLiteral = "<gml:Point xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\"><gml:pos>-83.38 33.95</gml:pos></gml:Point>";
        Literal expResult = ResourceFactory.createTypedLiteral(gmlGeometryLiteral, GMLDatatype.INSTANCE);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
