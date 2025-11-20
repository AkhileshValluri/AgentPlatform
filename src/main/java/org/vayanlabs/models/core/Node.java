package org.vayanlabs.models.core;

import org.vayanlabs.models.core.variable.VariableStore;
import org.vayanlabs.models.core.layer.Layer;

import java.util.List;

public class Node {
    private VariableStore memory;
    private VariableStore workflowArtifacts;
    private VariableStore workflowMemories;

    public String id;
    private List<Layer> layers;
    private Node nextNode;
    private Node previousNode;

    public Node(String id, List<Layer> layers, List<String> nextNodeIds) {
        this.id = id;
        this.layers = layers;
    }

    public void init(VariableStore memory, VariableStore workflowArtifacts, VariableStore workflowMemories) {
        for (Layer layer : layers) {
            layer.init(memory, workflowArtifacts, workflowMemories);
        }
    }

    public void execute() {
        for (Layer layer : layers) {
            layer.execute();
        }
    }
}
