package phase.api;

import groovy.api.BlockGraph;
import authoringUtils.frontendUtils.Try;
import groovy.api.GroovyFactory;
import phase.NamespaceException;
import phase.PhaseGraphImpl;
import phase.PhaseImpl;
import phase.TransitionImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  PhaseDB keeps track of all the PhaseGraphs that are "out" there,
 *  and also works as a factory for graph/phase/createTransition.
 */
public class PhaseDB {
    private Set<String> namespace;
    private Set<String> phaseNamespace;
    private List<PhaseGraph> phaseGraphs;
    private GroovyFactory factory;
    private String startingPhase;
    private BlockGraph endHandler;

    public PhaseDB(GroovyFactory factory) {
        this.namespace = new HashSet<>();
        this.phaseNamespace = new HashSet<>();
        phaseGraphs = new ArrayList<>();
        this.factory = factory;
        endHandler = factory.createGraph();
    }

    public Try<PhaseGraph> createGraph(String name) {
        var trySource = createPhase(name);
        if(namespace.add(name)) {
            Try<PhaseGraph> graph = trySource.map(s -> new PhaseGraphImpl(name, s, namespace::add));
            graph.forEach(phaseGraphs::add);
            if(namespace.size() == 1) startingPhase = name; // if there's only one phase, that's the starting one
            return graph;
        } else return Try.failure(new NamespaceException(name));
    }

    public void removeGraph(PhaseGraph graph) {
        namespace.remove(graph.name());
        phaseGraphs.remove(graph);
    }

    public Try<Phase> createPhase(String name) {
        if (phaseNamespace.add(name)) {
            return Try.success(new PhaseImpl(factory.createGraph(), name));
        } else return Try.failure(new NamespaceException(name));
    }

    public Transition createTransition(Phase from, GameEvent trigger, Phase to) {
        return new TransitionImpl(from, trigger, to, factory.createGraph());
    }

    public List<PhaseGraph> phases() { return phaseGraphs; }

    public void setStartingPhase(String phaseName) { startingPhase = phaseName; }
    public String getStartingPhase() { return startingPhase; }

    public BlockGraph endHandler() { return endHandler; }
}
