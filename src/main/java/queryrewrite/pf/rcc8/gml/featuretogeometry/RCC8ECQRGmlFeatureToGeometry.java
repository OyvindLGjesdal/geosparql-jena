/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrewrite.pf.rcc8.gml.featuretogeometry;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Substitute;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.main.QC;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.pfunction.PropertyFunctionBase;
import geof.topological.rcc8.expressionfunction.ExternallyConnected;
import implementation.support.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class RCC8ECQRGmlFeatureToGeometry extends PropertyFunctionBase {

    public static int VARIABLE = 0;

    @Override
    public QueryIterator exec(Binding binding, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCxt) {

        argSubject = Substitute.substitute(argSubject, binding);
        argObject = Substitute.substitute(argObject, binding);

        Node nodeVar_SUB = argSubject.getArg();
        Node nodeVar_OBJ = argObject.getArg();

        // Build  SPARQL algebra expression
        Var GMLVar_SUB = createNewVar();
        Var GMLVar_OBJ = createNewVar();
        Var GeomVar_SUB = createNewVar();

        BasicPattern bp = new BasicPattern();

        Triple NodeVarHasGML_OBJ = new Triple(nodeVar_OBJ, Vocabulary.GML_PRO.asNode(), GMLVar_OBJ);

        Triple FeaHasGeom_SUB = new Triple(nodeVar_SUB, Vocabulary.GEOMEXACT_PRO.asNode(), GeomVar_SUB);
        Triple GeomHasGML_SUB = new Triple(GeomVar_SUB, Vocabulary.GML_PRO.asNode(), GMLVar_SUB);

        //Spefify the Expr Function type here:
        Expr expr = new ExternallyConnected(new ExprVar(GMLVar_SUB.getName()), new ExprVar(GMLVar_OBJ.getName()));

        bp.add(FeaHasGeom_SUB);
        bp.add(GeomHasGML_SUB);
        bp.add(NodeVarHasGML_OBJ);

        OpBGP op = new OpBGP(bp);
        Op filter = OpFilter.filter(expr, op);

        // Use the default, optimizing query engine.
        return QC.execute(filter, binding, execCxt);
    }

    // Create a new, hidden, variable.
    private static Var createNewVar() {
        VARIABLE++;
        String varName = "Var-" + VARIABLE;
        return Var.alloc(varName);
    }

}
