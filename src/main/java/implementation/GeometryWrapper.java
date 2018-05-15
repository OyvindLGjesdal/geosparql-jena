/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import com.vividsolutions.jts.geom.Point;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.index.GeometryTransformIndex;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import implementation.jts.CustomGeometryFactory;
import implementation.registry.CRSRegistry;
import implementation.registry.MathTransformRegistry;
import implementation.registry.UnitsRegistry;
import implementation.support.GeoSerialisationEnum;
import implementation.vocabulary.SRS_URI;
import implementation.vocabulary.Unit_URI;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GeometryWrapper implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Geometry xyGeometry;
    private final Geometry parsingGeometry;
    private final String srsURI;
    private final GeoSerialisationEnum serialisation;
    private final CoordinateReferenceSystem crs;
    private final UnitsOfMeasure unitsOfMeasure;
    private final DimensionInfo dimensionInfo;

    public GeometryWrapper(Geometry geometry, String srsURI, GeoSerialisationEnum serialisation, DimensionInfo dimensionInfo) {

        this.serialisation = serialisation;

        if (srsURI.isEmpty()) {
            srsURI = SRS_URI.DEFAULT_WKT_CRS84;
        }
        this.srsURI = srsURI;
        this.crs = CRSRegistry.addCRS(srsURI);
        this.unitsOfMeasure = CRSRegistry.getUnitsOfMeasure(srsURI);

        this.dimensionInfo = dimensionInfo;

        this.xyGeometry = GeometryReverse.check(geometry, crs);
        this.parsingGeometry = geometry;
    }

    /**
     * Default to WGS84 and XY coordinate dimensions.
     *
     * @param geometry
     * @param serialisation
     */
    public GeometryWrapper(Geometry geometry, GeoSerialisationEnum serialisation) {
        this(geometry, "", serialisation, DimensionInfo.xyPoint());
    }

    /**
     * Default to XY coordinate dimensions.
     *
     * @param geometry
     * @param srsURI
     * @param serialisation
     */
    public GeometryWrapper(Geometry geometry, String srsURI, GeoSerialisationEnum serialisation) {
        this(geometry, srsURI, serialisation, DimensionInfo.xyPoint());
    }

    transient private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    /**
     * Empty geometry with specified parameters.
     *
     * @param srsURI
     * @param serialisation
     * @param dimensionInfo
     */
    public GeometryWrapper(String srsURI, GeoSerialisationEnum serialisation, DimensionInfo dimensionInfo) {
        this(new CustomCoordinateSequence(DimensionInfo.xyPoint().getDimensions()), srsURI);
    }

    /**
     * Point geometry with specified SRS.
     *
     * @param pointCoordinateSequence
     * @param srsURI
     */
    public GeometryWrapper(CustomCoordinateSequence pointCoordinateSequence, String srsURI) {
        this(GEOMETRY_FACTORY.createPoint(pointCoordinateSequence), srsURI, GeoSerialisationEnum.WKT, DimensionInfo.xyPoint());
    }

    public GeometryWrapper(GeometryWrapper geometryWrapper) {

        this.xyGeometry = geometryWrapper.xyGeometry;
        this.parsingGeometry = geometryWrapper.parsingGeometry;
        this.srsURI = geometryWrapper.srsURI;
        this.serialisation = geometryWrapper.serialisation;

        this.crs = geometryWrapper.crs;
        this.unitsOfMeasure = geometryWrapper.unitsOfMeasure;
        this.dimensionInfo = geometryWrapper.dimensionInfo;
    }

    /**
     * Transforms, if necessary, the provided GeometryWrapper according to the
     * current GeometryWrapper SRS_URI.
     *
     * @param sourceGeometryWrapper
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper checkTransformCRS(GeometryWrapper sourceGeometryWrapper) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometryWrapper;

        if (srsURI.equals(sourceGeometryWrapper.srsURI)) {
            transformedGeometryWrapper = sourceGeometryWrapper;
        } else {
            transformedGeometryWrapper = GeometryTransformIndex.transform(sourceGeometryWrapper, srsURI);
        }

        return transformedGeometryWrapper;
    }

    /**
     * Returns this geometry wrapper converted to the SRS_URI URI.
     *
     * @param srsURI
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper convertCRS(String srsURI) throws FactoryException, MismatchedDimensionException, TransformException {
        return GeometryTransformIndex.transform(this, srsURI);
    }

    public CoordinateReferenceSystem getCRS() {
        return crs;
    }

    /**
     * Geometry with coordinates in x,y order, regardless of SRS_URI.
     *
     * @return
     */
    public Geometry getXYGeometry() {
        return xyGeometry;
    }

    /**
     * Geometry with coordinates as originally provided.
     *
     * @return
     */
    public Geometry getParsingGeometry() {
        return parsingGeometry;
    }

    public String getSrsURI() {
        return srsURI;
    }

    /**
     * getSRID used in GeoSPARQL Standard page 22 to refer to srsURI. i.e.
     * getSrsURI and getSRID are the same.
     *
     * @return
     */
    public String getSRID() {
        return srsURI;
    }

    public GeoSerialisationEnum getGeoSerialisation() {
        return serialisation;
    }

    public GeometryWrapper buffer(double distance, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        //Check whether the source geometry is linear units for cartesian calculation. If not then transform to default GeoCentric CRS GeometryWrapper.
        GeometryWrapper transformedGeometryWrapper;
        Boolean isTransformNeeded = !unitsOfMeasure.isLinearUnits();
        if (isTransformNeeded) {
            String utmURI = findUTMZoneURI();
            transformedGeometryWrapper = GeometryTransformIndex.transform(this, utmURI);
        } else {
            transformedGeometryWrapper = this;
        }

        //Check whether the units of the distance need converting.
        double transformedDistance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, transformedGeometryWrapper.unitsOfMeasure.getUnitURI());

        //Buffer the transformed geometry
        Geometry geometry = transformedGeometryWrapper.xyGeometry.buffer(transformedDistance);
        DimensionInfo bufferedDimensionInfo = new DimensionInfo(dimensionInfo.getCoordinate(), dimensionInfo.getSpatial(), geometry.getDimension());
        GeometryWrapper bufferedGeometryWrapper = new GeometryWrapper(geometry, transformedGeometryWrapper.srsURI, transformedGeometryWrapper.serialisation, bufferedDimensionInfo);

        //Check whether need to transform back to the original srsURI.
        if (isTransformNeeded) {
            return GeometryTransformIndex.transform(bufferedGeometryWrapper, srsURI);
        } else {
            return bufferedGeometryWrapper;
        }
    }

    private String findUTMZoneURI() throws FactoryException, MismatchedDimensionException, TransformException {

        //Find a point in the parsing geometry.
        Coordinate coord = parsingGeometry.getCoordinate();
        Point point = GEOMETRY_FACTORY.createPoint(coord);
        //Convert to WGS84.
        CoordinateReferenceSystem wgs84CRS = CRSRegistry.getCRS(SRS_URI.WGS84_CRS);
        MathTransform transform = MathTransformRegistry.getMathTransform(crs, wgs84CRS);

        Point wgs84Point = (Point) JTS.transform(point, transform);

        //Find the UTM zone.
        return CRSRegistry.findUTMZoneURIFromWGS84(wgs84Point.getX(), wgs84Point.getY());
    }

    public GeometryWrapper convexHull() {
        Geometry geo = this.xyGeometry.convexHull();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry geo = this.xyGeometry.difference(transformedGeometry.xyGeometry);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    /**
     * Distance defaulting to metres.
     *
     * @param targetGeometry
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public double distance(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return distance(targetGeometry, Unit_URI.METRE_URI);
    }

    public double distance(GeometryWrapper targetGeometry, UnitsOfMeasure unitsOfMeasure) throws FactoryException, MismatchedDimensionException, TransformException {
        return distance(targetGeometry, unitsOfMeasure.getUnitURI());
    }

    public double distance(GeometryWrapper targetGeometry, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        Boolean isUnitsLinear = unitsOfMeasure.isLinearUnits();
        Boolean isTargetUnitsLinear = UnitsRegistry.isLinearUnits(targetDistanceUnitsURI);

        GeometryWrapper preparedSourceGeometry;
        GeometryWrapper preparedTargetGeometry;

        if (isUnitsLinear.equals(isTargetUnitsLinear)) {
            //Source geometry and target units are both the same. Convert the target geometry if required.
            preparedSourceGeometry = this;
            preparedTargetGeometry = checkTransformCRS(targetGeometry);
        } else if (isTargetUnitsLinear) {
            //Source geometry is not linear but targets are so convert to linear CRS.
            preparedSourceGeometry = GeometryTransformIndex.transform(this, SRS_URI.GEOTOOLS_GEOCENTRIC_CARTESIAN);
            preparedTargetGeometry = GeometryTransformIndex.transform(targetGeometry, SRS_URI.GEOTOOLS_GEOCENTRIC_CARTESIAN);
        } else {
            //Source geometry is linear but targets are not so convert to nonlinear CRS.
            preparedSourceGeometry = GeometryTransformIndex.transform(this, SRS_URI.DEFAULT_WKT_CRS84);
            preparedTargetGeometry = GeometryTransformIndex.transform(targetGeometry, SRS_URI.DEFAULT_WKT_CRS84);
        }

        double distance = preparedSourceGeometry.xyGeometry.distance(preparedTargetGeometry.xyGeometry);
        String unitsURI = preparedSourceGeometry.unitsOfMeasure.getUnitURI();
        double targetDistance = UnitsOfMeasure.conversion(distance, unitsURI, targetDistanceUnitsURI);

        return targetDistance;
    }

    public GeometryWrapper boundary() {
        Geometry geo = this.xyGeometry.getBoundary();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper envelope() {
        Geometry geo = this.xyGeometry.getEnvelope();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry geo = this.xyGeometry.intersection(transformedGeometry.xyGeometry);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public IntersectionMatrix relate(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return xyGeometry.relate(transformedGeometry.xyGeometry);
    }

    public boolean relate(GeometryWrapper targetGeometry, String intersectionPattern) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return xyGeometry.relate(transformedGeometry.xyGeometry, intersectionPattern);
    }

    public GeometryWrapper symDifference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry geo = this.xyGeometry.symDifference(transformedGeometry.xyGeometry);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry geo = this.xyGeometry.union(transformedGeometry.xyGeometry);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public boolean contains(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.contains(transformedGeometry.xyGeometry);
    }

    public boolean crosses(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.crosses(transformedGeometry.xyGeometry);
    }

    public boolean disjoint(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.disjoint(transformedGeometry.xyGeometry);
    }

    public boolean equals(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.equals((Geometry) transformedGeometry.xyGeometry);
    }

    public boolean intersects(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.intersects(transformedGeometry.xyGeometry);
    }

    public boolean overlaps(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.overlaps(transformedGeometry.xyGeometry);
    }

    public boolean touches(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.touches(transformedGeometry.xyGeometry);
    }

    public boolean within(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.within(transformedGeometry.xyGeometry);
    }

    public NodeValue asNode() throws DatatypeFormatException {
        Literal literal = asLiteral();
        return NodeValue.makeNode(literal.getLexicalForm(), literal.getDatatype());
    }

    public Literal asLiteral() throws DatatypeFormatException {
        RDFDatatype datatype;

        switch (serialisation) {
            case WKT:
                datatype = WKTDatatype.THE_WKT_DATATYPE;
                break;
            case GML:
                datatype = GMLDatatype.THE_GML_DATATYPE;
                break;
            default:
                throw new DatatypeFormatException("Serialisation is not WKT or GML.");
        }

        String lexicalForm = datatype.unparse(this);
        return ResourceFactory.createTypedLiteral(lexicalForm, datatype);
    }

    public int getCoordinateDimension() {
        return dimensionInfo.getCoordinate();
    }

    public int getSpatialDimension() {
        return dimensionInfo.getSpatial();
    }

    public int getTopologicalDimension() {
        return dimensionInfo.getTopological();
    }

    public CoordinateSequenceDimensions getCoordinateSequenceDimensions() {
        return dimensionInfo.getDimensions();
    }

    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    public UnitsOfMeasure getUnitsOfMeasure() {
        return unitsOfMeasure;
    }

    public DimensionInfo getDimensionInfo() {
        return dimensionInfo;
    }

    public boolean isEmpty() {
        return this.xyGeometry.isEmpty();
    }

    public boolean isSimple() {
        return this.xyGeometry.isSimple();
    }

    private static final WKTDatatype WKT_DATATYPE = WKTDatatype.THE_WKT_DATATYPE;
    private static final GMLDatatype GML_DATATYPE = GMLDatatype.THE_GML_DATATYPE;

    /**
     * Returns null if invalid node value provided.
     *
     * @param nodeValue
     * @return
     */
    public static final GeometryWrapper extract(NodeValue nodeValue) {

        if (!nodeValue.isLiteral()) {
            return null;
        }

        Node node = nodeValue.asNode();
        String datatypeURI = node.getLiteralDatatypeURI();
        String lexicalForm = node.getLiteralLexicalForm();
        return extract(lexicalForm, datatypeURI);
    }

    /**
     * Returns null if invalid literal provided.
     *
     * @param geometryLiteral
     * @return
     */
    public static final GeometryWrapper extract(Literal geometryLiteral) {
        return extract(geometryLiteral.getLexicalForm(), geometryLiteral.getDatatypeURI());
    }

    private static GeometryWrapper extract(String lexicalForm, String datatypeURI) {
        GeometryWrapper geometry;

        switch (datatypeURI) {
            case WKTDatatype.THE_TYPE_URI:
                geometry = WKT_DATATYPE.parse(lexicalForm);
                break;
            case GMLDatatype.THE_TYPE_URI:
                geometry = GML_DATATYPE.parse(lexicalForm);
                break;
            default:
                LOGGER.warn("Literal is not a WKT or GML Literal: {} {}", lexicalForm, datatypeURI);
                return null;
        }

        return geometry;
    }

    public static final GeometryWrapper EMPTY_WKT = WKTDatatype.THE_WKT_DATATYPE.read("POINT EMPTY");

    /*
    //TODO - empty GML GeometryWrapper creation.
    public static final GeometryWrapper emptyGML() {

    }
     */
    //If xyGeometry are equal then parsingGeometry will be equal.
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.xyGeometry);
        hash = 11 * hash + Objects.hashCode(this.srsURI);
        hash = 11 * hash + Objects.hashCode(this.serialisation);
        hash = 11 * hash + Objects.hashCode(this.crs);
        hash = 11 * hash + Objects.hashCode(this.unitsOfMeasure);
        hash = 11 * hash + Objects.hashCode(this.dimensionInfo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GeometryWrapper other = (GeometryWrapper) obj;
        if (!Objects.equals(this.srsURI, other.srsURI)) {
            return false;
        }
        if (!Objects.equals(this.xyGeometry, other.xyGeometry)) {
            return false;
        }
        if (this.serialisation != other.serialisation) {
            return false;
        }
        if (!Objects.equals(this.crs, other.crs)) {
            return false;
        }
        if (!Objects.equals(this.unitsOfMeasure, other.unitsOfMeasure)) {
            return false;
        }
        return Objects.equals(this.dimensionInfo, other.dimensionInfo);
    }

    @Override
    public String toString() {
        return "GeometryWrapper{" + "xyGeometry=" + xyGeometry + ", parsingGeometry=" + parsingGeometry + ", srsURI=" + srsURI + ", serialisation=" + serialisation + ", crs=" + crs + ", unitsOfMeasure=" + unitsOfMeasure + ", dimensionInfo=" + dimensionInfo + '}';
    }

}
