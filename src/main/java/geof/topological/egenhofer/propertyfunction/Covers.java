/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.egenhofer.propertyfunction;

import geof.topological.GenericPropertyFunction;
import org.apache.jena.sparql.expr.Expr;
import queryrewrite.expr.eh.EHCoversExprFunc;

/**
 *
 * @author haozhechen
 */
public class Covers extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new EHCoversExprFunc(expr1, expr2);
    }

}
