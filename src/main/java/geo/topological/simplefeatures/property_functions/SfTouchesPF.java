/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.simplefeatures.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.simplefeatures.expression_functions.SfTouchesEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 * <<<<<<< HEAD
 * @a
 * uthor Gregory Albiston
 */
public class SfTouchesPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new SfTouchesEF(expr1, expr2);
    }

}