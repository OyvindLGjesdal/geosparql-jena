/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.wkt;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import implementation.DimensionInfo;
import implementation.datatype.parsers.ParseException;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import implementation.jts.CustomCoordinateSequenceFactory;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Greg
 */
public class WKTGeometryBuilder {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    private final CoordinateSequenceDimensions coordinateSequenceDimensions;
    private final Geometry geometry;

    public WKTGeometryBuilder(String shape, String dimension, String coordinates) {
        this.coordinateSequenceDimensions = convertDimensions(dimension);
        this.geometry = buildGeometry(shape, coordinates);
    }

    private static CoordinateSequenceDimensions convertDimensions(String dimensionsString) {

        CoordinateSequenceDimensions dims;
        switch (dimensionsString) {
            case "zm":
                dims = CoordinateSequenceDimensions.XYZM;
                break;
            case "z":
                dims = CoordinateSequenceDimensions.XYZ;
                break;
            case "m":
                dims = CoordinateSequenceDimensions.XYM;
                break;
            default:
                dims = CoordinateSequenceDimensions.XY;
                break;
        }
        return dims;
    }

    public DimensionInfo getDimensionInfo() {
        return new DimensionInfo(getCoordinateDimension(), getSpatialDimension(), geometry.getDimension());
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public int getSpatialDimension() {

        switch (coordinateSequenceDimensions) {
            default:
                return 2;
            case XYZ:
            case XYZM:
                return 3;
        }
    }

    public int getCoordinateDimension() {

        switch (coordinateSequenceDimensions) {
            default:
                return 2;
            case XYZ:
            case XYM:
                return 3;
            case XYZM:
                return 4;
        }
    }

    private Geometry buildGeometry(String shape, String coordinates) {

        Geometry geo;

        switch (shape) {
            case "point":
                CustomCoordinateSequence pointSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                geo = GEOMETRY_FACTORY.createPoint(pointSequence);
                break;
            case "linestring":
                CustomCoordinateSequence lineSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                geo = GEOMETRY_FACTORY.createLineString(lineSequence);
                break;
            case "polygon":
                geo = buildPolygon(coordinates);
                break;
            case "multipoint":
                CustomCoordinateSequence multiPointSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                geo = GEOMETRY_FACTORY.createMultiPoint(multiPointSequence);
                break;
            case "multilinestring":
                String[] splitCoordinates = splitCoordinates(coordinates);
                LineString[] lineStrings = splitLineStrings(splitCoordinates);
                geo = GEOMETRY_FACTORY.createMultiLineString(lineStrings);
                break;
            case "multipolygon":
                geo = buildMultiPolygon(coordinates);
                break;
            case "geometrycollection":
                geo = buildGeometryCollection(coordinates);
                break;
            default:
                throw new ParseException("Geometry shape not supported: " + shape);
        }

        return geo;
    }

    private String clean(String unclean) {
        return unclean.replace(")", "").replace("(", "").trim();
    }

    private Geometry buildGeometryCollection(String coordinates) throws ParseException {
        //Split coordinates

        String tidied = coordinates.substring(1, coordinates.length() - 1);
        tidied = tidied.replaceAll("[\\ ]?,[\\ ]?", ","); //Remove spaces around commas
        String[] partCoordinates = tidied.split("\\),(?=[^\\(])"); //Split whenever there is a ), but not ),(

        Geometry[] geometries = new Geometry[partCoordinates.length];

        for (int i = 0; i < partCoordinates.length; i++) {
            WKTGeometryBuilder partWKTInfo = extract(partCoordinates[i]);
            geometries[i] = partWKTInfo.geometry;
        }
        return GEOMETRY_FACTORY.createGeometryCollection(geometries);
    }

    private Geometry buildMultiPolygon(String coordinates) {
        String trimmed = coordinates.replace(")) ,", ")),");
        String[] multiCoordinates = trimmed.split("\\)\\),");
        Polygon[] polygons = new Polygon[multiCoordinates.length];
        for (int i = 0; i < multiCoordinates.length; i++) {
            polygons[i] = buildPolygon(multiCoordinates[i]);
        }

        return GEOMETRY_FACTORY.createMultiPolygon(polygons);
    }

    private Polygon buildPolygon(String coordinates) {

        Polygon polygon;

        String[] splitCoordinates = splitCoordinates(coordinates);
        if (splitCoordinates.length == 1) { //Polygon without holes.
            CustomCoordinateSequence shellSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
            polygon = GEOMETRY_FACTORY.createPolygon(shellSequence);
        } else {    //Polygon with holes
            String shellCoordinates = splitCoordinates[0];

            CustomCoordinateSequence shellSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(shellCoordinates));
            LinearRing shellLinearRing = GEOMETRY_FACTORY.createLinearRing(shellSequence);

            String[] splitHoleCoordinates = Arrays.copyOfRange(splitCoordinates, 1, splitCoordinates.length);
            LinearRing[] holesLinearRing = splitLinearRings(splitHoleCoordinates);

            polygon = GEOMETRY_FACTORY.createPolygon(shellLinearRing, holesLinearRing);

        }
        return polygon;
    }

    private String[] splitCoordinates(String coordinates) {

        String trimmed = coordinates.replace(") ,", "),");
        return trimmed.split("\\),");

    }

    private LineString[] splitLineStrings(String[] splitCoordinates) {

        LineString[] lineStrings = new LineString[splitCoordinates.length];

        for (int i = 0; i < splitCoordinates.length; i++) {
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(splitCoordinates[i]));
            LineString lineString = GEOMETRY_FACTORY.createLineString(sequence);
            lineStrings[i] = lineString;
        }

        return lineStrings;

    }

    private LinearRing[] splitLinearRings(String[] splitCoordinates) {

        LinearRing[] linearRings = new LinearRing[splitCoordinates.length];

        for (int i = 0; i < splitCoordinates.length; i++) {
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(splitCoordinates[i]));
            LinearRing linearRing = GEOMETRY_FACTORY.createLinearRing(sequence);
            linearRings[i] = linearRing;
        }

        return linearRings;

    }

    public static WKTGeometryBuilder extract(String wktText) throws ParseException {

        if (wktText.isEmpty()) {
            throw new ParseException("WKT string is empty.");
        }

        wktText = wktText.trim();
        wktText = wktText.toLowerCase();

        int coordinatesStart = wktText.indexOf("(");

        String coordinates = wktText.substring(coordinatesStart);

        //Check for "empty" keyword and remove.
        if (coordinates.equals("empty")) {
            coordinates = "";
        }

        String remainder = wktText.substring(0, coordinatesStart).trim();

        int firstSpace = remainder.indexOf(" ");
        String shape;
        String dimension;
        if (firstSpace != -1) {
            shape = remainder.substring(0, firstSpace);
            dimension = remainder.substring(firstSpace + 1);
        } else {
            shape = remainder;
            dimension = "";
        }

        return new WKTGeometryBuilder(shape, dimension, coordinates);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.coordinateSequenceDimensions);
        hash = 37 * hash + Objects.hashCode(this.geometry);
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
        final WKTGeometryBuilder other = (WKTGeometryBuilder) obj;
        if (this.coordinateSequenceDimensions != other.coordinateSequenceDimensions) {
            return false;
        }
        return Objects.equals(this.geometry, other.geometry);
    }

    @Override
    public String toString() {
        return "WKTInfo{" + "dimensions=" + coordinateSequenceDimensions + ", geometry=" + geometry + '}';
    }

}