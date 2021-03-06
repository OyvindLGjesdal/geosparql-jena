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

import org.apache.sis.referencing.CRS;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class UnitsOfMeasureTest {

    public UnitsOfMeasureTest() {
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
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test(expected = NullPointerException.class)
    public void testConversionRadianToMetre() throws FactoryException {
        System.out.println("conversionRadianToMetre");
        double distance = 0.5;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
        CoordinateReferenceSystem crs = CRS.forCode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB - metres projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        Double expResult = null;
        Double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test(expected = NullPointerException.class)
    public void testConversionMetreToDegree() throws FactoryException {
        System.out.println("conversionMetreToDegree");
        double distance = 100.0;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
        CoordinateReferenceSystem crs = CRS.forCode("http://www.opengis.net/def/crs/EPSG/0/4326");  //OSGB - metres projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        Double expResult = null;
        Double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testConversionMetreToMetre() throws FactoryException {
        System.out.println("conversionMetreToMetre");
        double distance = 100.0;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
        CoordinateReferenceSystem crs = CRS.forCode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB - metres projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = 100.0;
        double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testConversionDegreeToDegree() throws FactoryException {
        System.out.println("conversionDegreeToDegree");
        double distance = 100.0;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
        CoordinateReferenceSystem crs = CRS.forCode("http://www.opengis.net/def/crs/EPSG/0/4326");  //WGS84 degrees non-projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        double expResult = 100.0;
        double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test
    public void testConversionRadianToDegree() throws FactoryException {
        System.out.println("conversionRadianToDegree");
        double distance = 0.5;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
        CoordinateReferenceSystem crs = CRS.forCode("http://www.opengis.net/def/crs/EPSG/0/4326");  //WGS84 degrees non-projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);
        double radsToDegrees = 180 / Math.PI;
        double expResult = distance * radsToDegrees;
        double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of conversion method, of class UnitsOfMeasure.
     *
     * @throws org.opengis.util.FactoryException
     */
    @Test(expected = NullPointerException.class)
    public void testConversionDegreeToMetre() throws FactoryException {
        System.out.println("conversionDegreeToMetre");
        double distance = 10.0;
        String sourceDistanceUnitURI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
        CoordinateReferenceSystem crs = CRS.forCode("http://www.opengis.net/def/crs/EPSG/0/27700");  //OSGB metres projected

        UnitsOfMeasure targetUnitsOfMeasure = new UnitsOfMeasure(crs);

        Double expResult = null;
        Double result = UnitsOfMeasure.conversion(distance, sourceDistanceUnitURI, targetUnitsOfMeasure.getUnitURI());

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result, 0.0);
    }

}
