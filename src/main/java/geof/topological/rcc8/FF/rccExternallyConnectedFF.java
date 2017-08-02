/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.rcc8.FF;

import geof.topological.rcc8.EF.rccExternallyConnectedEF;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;

/**
 *
 * @author Greg Albiston
 */
public class rccExternallyConnectedFF extends FunctionBase2 {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        Expr e1 = v1.getExpr();
        Expr e2 = v2.getExpr();

        rccExternallyConnectedEF func = new rccExternallyConnectedEF(e1, e2);

        return func.eval(v1, v2);
    }

}