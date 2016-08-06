/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import datatype.GmlDatatype;
import com.vividsolutions.jts.geom.Geometry;
import java.util.List;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Substitute;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.pfunction.PropertyFunctionBase;
import org.apache.jena.sparql.util.IterLib;
import org.apache.jena.sparql.util.NodeFactoryExtra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class Distance extends PropertyFunctionBase {

    private static Logger LOGGER = LoggerFactory.getLogger(Distance.class);

    @Override
    public QueryIterator exec(Binding binding, PropFuncArg argSubject, Node predicate, PropFuncArg argObject, ExecutionContext execCtx) {
        LOGGER.info("Property Function called");
        argSubject = Substitute.substitute(argSubject, binding);
        argObject = Substitute.substitute(argObject, binding);
        List<Node> list = argObject.getArgList();

        RDFDatatype gmlDataType = GmlDatatype.theGmlDatatype;

        Geometry g1 = (Geometry) gmlDataType.parse(list.get(0).getLiteralLexicalForm());
        Geometry g2 = (Geometry) gmlDataType.parse(list.get(1).getLiteralLexicalForm());
        Double distance = -1d;

        distance = g1.distance(g2);

        if (distance != -1d) {
            Node dis = NodeFactoryExtra.doubleToNode(distance);
            return IterLib.oneResult(binding, Var.alloc(argSubject.getArg()), dis, execCtx);
        } else {
            return IterLib.noResults(execCtx);
        }

    }

}
