package org.vayanlabs.models.core;

import java.util.List;
import java.util.Map;
import org.vayanlabs.models.core.variable.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Workflow manages:
 *  - interNodeVariableStore  (short-term, in-memory)
 *  - interNodeLongTermVariableStore (long-term, artifact/filesystem)
 *  - intraNodeVariableStore  (per-node VariableStore, keyed by Node)
 *  - list of nodes belonging to the workflow
 */
public class Workflow {

    // Each context manages multiple variable stores (STM/LTM, node-local etc.)
    public VariableStore interNodeVariableStore;
    public VariableStore interNodeLongTermVariableStore;
    public Map<String, VariableStore> intraNodeVariableStore;
    private List<Node> nodes;

    /**
     * Construct a Workflow for the provided nodes.
     * For each node a new in-memory VariableStore is constructed and stored in intraNodeVariableStore.
     *
     * @param nodes list of nodes in the workflow
     */
    public Workflow(List<Node> nodes) {
        this.nodes = Objects.requireNonNull(nodes, "nodes cannot be null");
        this.intraNodeVariableStore = new HashMap<String, VariableStore>();
        this.interNodeVariableStore = new VariableStore( new InMemoryBackend());

        try {
            ArtifactBackend artifactBackend = new ArtifactBackend("./workflow-artifacts");
            this.interNodeLongTermVariableStore = new VariableStore(artifactBackend);
        } catch (VariableStoreException e) {
            throw new RuntimeException(e);
        }

        // build per-node (intra-node) variable stores using in-memory backends
        for (Node node : nodes) {
            String nodeKey = safeNodeId(node);
            VariableStore vs = new VariableStore(new InMemoryBackend());
            this.intraNodeVariableStore.put(nodeKey, vs);
        }
    }

    private String safeNodeId(Node node) {
        if (node == null) return "null-node";
        try {
            String id = node.id;
            if (id != null) return id;
        } catch (NoSuchMethodError | AbstractMethodError | Exception ignored) {
            // fall through to toString()
        }
        return node.toString();
    }

    public void run() {
        for (Node node: nodes) {
            node.execute();
        }
    }
}
