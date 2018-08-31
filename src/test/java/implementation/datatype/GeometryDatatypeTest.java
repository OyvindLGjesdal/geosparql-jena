/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import implementation.GeoSPARQLSupport;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 */
public class GeometryDatatypeTest {

    public GeometryDatatypeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        GeoSPARQLSupport.loadFunctionsNoIndex();
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
     * Test of get method, of class DatatypeUtil.
     */
    @Test
    public void testGetGeometryDatatype_RDFDatatype() {
        System.out.println("getGeometryDatatype");
        RDFDatatype rdfDatatype = WKTDatatype.INSTANCE;
        GeometryDatatype expResult = WKTDatatype.INSTANCE;
        GeometryDatatype result = GeometryDatatype.get(rdfDatatype);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class DatatypeUtil.
     */
    @Test(expected = DatatypeFormatException.class)
    public void testGetGeometryDatatype_RDFDatatype_Fail() {
        System.out.println("getGeometryDatatype_Fail");
        RDFDatatype rdfDatatype = XSDDatatype.XSDdouble;
        GeometryDatatype.get(rdfDatatype);
        fail("Exception not thrown when expected.");
    }

    /**
     * Test of get method, of class DatatypeUtil.
     */
    @Test
    public void testGetGeometryDatatype_String() {
        System.out.println("getGeometryDatatype");
        String datatypeURI = WKTDatatype.URI;
        GeometryDatatype expResult = WKTDatatype.INSTANCE;
        GeometryDatatype result = GeometryDatatype.get(datatypeURI);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkURI method, of class DatatypeUtil.
     */
    @Test
    public void testCheckGeometryDatatypeURI() {
        System.out.println("checkGeometryDatatypeURI");
        String datatypeURI = WKTDatatype.URI;
        boolean expResult = true;
        boolean result = GeometryDatatype.checkURI(datatypeURI);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkURI method, of class DatatypeUtil.
     */
    @Test
    public void testCheckGeometryDatatypeURI_Fail() {
        System.out.println("checkGeometryDatatypeURI_Fail");
        String datatypeURI = XSDDatatype.XSDdouble.getURI();
        GeometryDatatype.checkURI(datatypeURI);
        boolean expResult = false;
        boolean result = GeometryDatatype.checkURI(datatypeURI);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class DatatypeUtil.
     */
    @Test
    public void testCheckGeometryDatatype() {
        System.out.println("checkGeometryDatatype");
        RDFDatatype rdfDatatype = WKTDatatype.INSTANCE;
        boolean expResult = true;
        boolean result = GeometryDatatype.check(rdfDatatype);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class DatatypeUtil.
     */
    @Test
    public void testCheckGeometryDatatype_Fail() {
        System.out.println("checkGeometryDatatype_Fail");
        RDFDatatype rdfDatatype = XSDDatatype.XSDdouble;
        boolean expResult = false;
        boolean result = GeometryDatatype.check(rdfDatatype);
        assertEquals(expResult, result);
    }

}