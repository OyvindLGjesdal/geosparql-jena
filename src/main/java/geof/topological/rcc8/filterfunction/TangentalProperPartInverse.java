/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.rcc8.filterfunction;

import implementation.GeometryWrapper;
import geof.topological.GenericFilterFunction;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 * RCC8TPPI means tangential proper part inverse
 *
 * @author haozhechen
 */
public class TangentalProperPartInverse extends GenericFilterFunction {

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.relate(targetGeometry, "TTTFTTFFT");
    }

}
