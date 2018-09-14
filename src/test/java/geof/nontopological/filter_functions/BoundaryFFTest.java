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
package geof.nontopological.filter_functions;

import implementation.datatype.WKTDatatype;
import org.apache.jena.sparql.expr.NodeValue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 */
public class BoundaryFFTest {

    public BoundaryFFTest() {
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
     * Test of exec method, of class BoundaryFF.
     */
    @Test
    public void testExec_CRS84() {
        System.out.println("exec_CRS84");

        NodeValue v = NodeValue.makeNode("POLYGON((30 40, 30 70, 90 70, 90 40, 30 40))", WKTDatatype.INSTANCE);
        BoundaryFF instance = new BoundaryFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> LINESTRING(30 40, 30 70, 90 70, 90 40, 30 40)", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of exec method, of class BoundaryFF.
     */
    @Test
    public void testExec_WGS84() {
        System.out.println("exec_WGS84");

        NodeValue v = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> POLYGON((30 40, 30 70, 90 70, 90 40, 30 40))", WKTDatatype.INSTANCE);
        BoundaryFF instance = new BoundaryFF();
        NodeValue expResult = NodeValue.makeNode("<http://www.opengis.net/def/crs/EPSG/0/4326> LINESTRING(30 40, 30 70, 90 70, 90 40, 30 40)", WKTDatatype.INSTANCE);
        NodeValue result = instance.exec(v);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
