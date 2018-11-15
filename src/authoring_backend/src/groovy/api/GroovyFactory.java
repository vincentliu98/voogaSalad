package groovy.api;

import groovy.graph.BlockEdgeImpl;
import groovy.graph.BlockGraphImpl;
import groovy.graph.blocks.core.*;
import groovy.graph.blocks.small_factory.LiteralFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  A Factory that can generate some pieces of groovy code
 *  TODO: Make a utility function that helps the author when writing things out, using the gameData
 */
public class GroovyFactory {
    /**
     *  Makes an empty BlockGraph with one source node
     */
    public static BlockGraph emptyGraph() { return new BlockGraphImpl(); }

    /**
     *  Makes a default BlockGraph for guards, passing everything in.
     */
    public static BlockGraph emptyGuard() {
        try {
            var graph = new BlockGraphImpl();
            var pass = assignBlock();
            var guardRet = $guardRet();
            var t = booleanBlock("true").get();
            graph.addNode(pass);
            graph.addNode(guardRet);
            graph.addNode(t);
            graph.addEdge(makeEdge(pass, Ports.ASSIGN_LHS, guardRet));
            graph.addEdge(makeEdge(pass, Ports.ASSIGN_RHS, t));
            graph.addEdge(makeEdge(graph.source(), Ports.FLOW_OUT, pass));
            return graph;
        } catch(Throwable ignored) { return emptyGraph(); } // but it's not gonna fail
    }

    /**
     *  Makes an edge
     */
    public static BlockEdge makeEdge(GroovyBlock from, Ports fromPort, GroovyBlock to) {
        return new BlockEdgeImpl(from, fromPort, to);
    }

    /**
     *  Core Blocks
     */
    public static IfBlock ifBlock() { return new IfBlock(false); }
    public static IfBlock ifElseBlock() { return new IfBlock(true); }
    public static ElseBlock elseBlock() { return new ElseBlock(); }
    public static ForEachBlock forEachBlock(String loopvar) { return new ForEachBlock(loopvar); }

    public static AssignBlock assignBlock() { return new AssignBlock(); }

    public static Try<LiteralBlock> booleanBlock(String value) { return LiteralFactory.booleanBlock(value); }
    public static Try<LiteralBlock> integerBlock(String value) { return LiteralFactory.integerBlock(value); }
    public static Try<LiteralBlock> doubleBlock(String value) { return LiteralFactory.doubleBlock(value); }
    public static Try<LiteralBlock> listBlock(String value) { return LiteralFactory.listBlock(value); }
    public static Try<LiteralBlock> mapBlock(String value) { return LiteralFactory.mapBlock(value); }
    public static LiteralBlock stringBlock(String value) { return LiteralFactory.stringBlock(value); }
    public static Try<LiteralBlock> refBlock(String value) {
        return LiteralFactory.refBlock(value);
    }

    public static UnaryBlock unaryBlock(String op) { return new UnaryBlock(op); }

    public static InfixBinaryBlock binaryBlock(String op) { return new InfixBinaryBlock(op); }

    // We'll eventually remove this
    public static RawGroovyBlock rawBlock(String code) { return new RawGroovyBlock(code); }


    /**
     * Convenience Blocks
     */
    public static InfixBinaryBlock add() { return new InfixBinaryBlock("+"); }
    public static InfixBinaryBlock minus() { return new InfixBinaryBlock("-"); }
    public static InfixBinaryBlock multiply() { return new InfixBinaryBlock("*"); }
    public static InfixBinaryBlock divide() { return new InfixBinaryBlock("/"); }
    public static InfixBinaryBlock eq() { return new InfixBinaryBlock("=="); }
    public static InfixBinaryBlock neq() { return new InfixBinaryBlock("!="); }
    public static InfixBinaryBlock leq() { return new InfixBinaryBlock("<="); }
    public static InfixBinaryBlock lt() { return new InfixBinaryBlock("<"); }
    public static InfixBinaryBlock geq() { return new InfixBinaryBlock(">="); }
    public static InfixBinaryBlock gt() { return new InfixBinaryBlock(">"); }
    public static InfixBinaryBlock and() { return new InfixBinaryBlock("&&"); }
    public static InfixBinaryBlock or() { return new InfixBinaryBlock("||"); }
    public static Try<LiteralBlock> range(int from, int to) {
        return GroovyFactory.listBlock(
            Stream.iterate(from, v -> v < to, v -> v + 1)
                  .collect(Collectors.toList()).toString()
        );
    }
    public static RawGroovyBlock $clicked() { return GroovyFactory.rawBlock("$clicked"); }
    public static RawGroovyBlock $imageData() { return GroovyFactory.rawBlock("$imageData"); }
    public static RawGroovyBlock $guardRet() { return GroovyFactory.rawBlock("$guardRet"); }

    public static UnaryBlock $goto() { return GroovyFactory.unaryBlock("$goto"); }
    public static UnaryBlock $remove() { return GroovyFactory.unaryBlock("$entities.$instances.remove"); }

}

