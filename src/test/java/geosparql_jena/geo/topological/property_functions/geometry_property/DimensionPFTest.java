/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geosparql_jena.geo.topological.property_functions.geometry_property;

import geosparql_jena.implementation.GeometryWrapper;
import geosparql_jena.implementation.datatype.WKTDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
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
public class DimensionPFTest {

    public DimensionPFTest() {
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
     * Test of applyPredicate method, of class DimensionPF.
     */
    @Test
    public void testApplyPredicate_0_Dimension() {
        System.out.println("applyPredicate_0_Dimension");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/27700> POINT(90 60)", WKTDatatype.URI);
        DimensionPF instance = new DimensionPF();
        Literal expResult = ResourceFactory.createTypedLiteral("0", XSDDatatype.XSDinteger);
        Literal result = instance.applyPredicate(geometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of applyPredicate method, of class DimensionPF.
     */
    @Test
    public void testApplyPredicate_1_Dimension() {
        System.out.println("applyPredicate_1_Dimension");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING(90 60, 100 70)", WKTDatatype.URI);
        DimensionPF instance = new DimensionPF();
        Literal expResult = ResourceFactory.createTypedLiteral("1", XSDDatatype.XSDinteger);
        Literal result = instance.applyPredicate(geometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of applyPredicate method, of class DimensionPF.
     */
    @Test
    public void testApplyPredicate_2_Dimension() {
        System.out.println("applyPredicate_2_Dimension");
        GeometryWrapper geometryWrapper = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))", WKTDatatype.URI);
        DimensionPF instance = new DimensionPF();
        Literal expResult = ResourceFactory.createTypedLiteral("2", XSDDatatype.XSDinteger);
        Literal result = instance.applyPredicate(geometryWrapper);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
