/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.simplefeatures.filter_functions;

import geof.topological.simplefeatures.expression_functions.SfDisjointEF;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;

/**
 *
 * 
 */
public class SfDisjointFF extends FunctionBase2 {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        Expr e1 = v1.getExpr();
        Expr e2 = v2.getExpr();

        SfDisjointEF func = new SfDisjointEF(e1, e2);

        return func.eval(v1, v2);
    }

}
