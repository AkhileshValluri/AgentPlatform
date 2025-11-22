package org.vayanlabs.models.core.layer;

import org.vayanlabs.models.core.variable.Variable;
import org.vayanlabs.models.core.variable.VariableStore;

public abstract class Layer {

    protected VariableStore memory;
    protected VariableStore workflowArtifacts;
    protected VariableStore workflowMemories;
    String internalFlagVariableKey;
    Variable internalFlagVariable;

    public String id;

    public Layer(VariableStore memory, VariableStore workflowArtifacts, VariableStore workflowMemories) {
        this.memory = memory;
        this.workflowArtifacts = workflowArtifacts;
        this.workflowMemories = workflowMemories;
        this.internalFlagVariableKey = "_internal_flag_variable";
        if (!this.memory.exists(internalFlagVariableKey)) {
            internalFlagVariable = this.memory.create(internalFlagVariableKey, "True");
        }
    }

    public abstract void init();

    // needs to be overridden by subclasses
    public abstract void execute();

    public boolean preCheck() {
        // default precheck hook -- execute only if flag says to execute
        return Boolean.parseBoolean(memory.read(internalFlagVariableKey).getValue().toString());
    }

    public void run() {
        if (preCheck()) execute();
    }

    public void setInternalFlag(String val) {
        internalFlagVariable.setValue(val);
    }
}
