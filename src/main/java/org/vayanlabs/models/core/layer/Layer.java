package org.vayanlabs.models.core.layer;

import org.vayanlabs.models.core.variable.VariableStore;

public abstract class Layer {

    protected VariableStore memory;
    protected VariableStore workflowArtifacts;
    protected VariableStore workflowMemories;

    public String id;

    public void init(VariableStore memory, VariableStore workflowArtifacts, VariableStore workflowMemories) {
        this.memory = memory;
        this.workflowArtifacts = workflowArtifacts;
        this.workflowMemories = workflowMemories;
    }

    public abstract void execute();
}
