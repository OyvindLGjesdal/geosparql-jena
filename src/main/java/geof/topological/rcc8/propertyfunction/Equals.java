/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.rcc8.propertyfunction;

import geof.topological.GenericPropertyFunction;
import org.apache.jena.sparql.expr.Expr;
import queryrewrite.expr.rcc8.RCC8EQExprFunc;

/**
 *
 * @author haozhechen
 */
public class Equals extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new RCC8EQExprFunc(expr1, expr2);
    }

}
