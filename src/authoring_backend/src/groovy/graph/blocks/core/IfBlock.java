package groovy.graph.blocks.core;

import groovy.api.BlockGraph;
import graph.SimpleNode;
import groovy.api.Try;
import groovy.api.Ports;

import java.util.Set;

import static groovy.api.Ports.*;

public class IfBlock extends SimpleNode implements GroovyBlock<IfBlock> {
    private boolean elseIf;
    public IfBlock(boolean elseIf) {
        super();
        this.elseIf = elseIf;
    }

    @Override
    public Set<Ports> ports() { return Set.of(FLOW_OUT, IF_PREDICATE, IF_BODY); }

    @Override
    public Try<String> toGroovy(BlockGraph graph) {
        var tryPredicate = graph.findTarget(this, IF_PREDICATE).flatMap(b -> b.toGroovy(graph));
        var tryBody = graph.findTarget(this, IF_BODY, true).flatMap(b -> b.toGroovy(graph));
        var tryOut = graph.findTarget(this, FLOW_OUT).flatMap(b -> b.toGroovy(graph));
        return tryPredicate.flatMap( predicate ->
                tryBody.flatMap( body ->
                    tryOut.map( out ->
                        String.format("%sif(%s) {\n%s\n}\n%s", elseIf ? "else " : "", predicate, body, out)
                    )
                )
        );
    }

    @Override
    public IfBlock replicate() { return new IfBlock(elseIf); }

    @Override
    public String name() { return elseIf ? "Else If" : "If"; }
}
