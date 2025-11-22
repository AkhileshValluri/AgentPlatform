package org.vayanlabs.models.core.layer;

import org.vayanlabs.models.core.variable.Variable;

public class ValidationLayer extends Layer {

    public enum ValidationType {
        IF,
        LOOP
    }

    private ValidationType validationType;
    Variable externalFlagVariable;
    String externalFlagVariableKey;

    public void init(ValidationType validationType, String externalFlagVariableKey) {
        // external flag variable key has to be passed on by delegation layer from previous node
        this.validationType = validationType;
        this.externalFlagVariableKey = externalFlagVariableKey;
        this.externalFlagVariable = this.memory.read(this.externalFlagVariableKey);
    }

    public void init(ValidationType validationType, Variable externalFlagVariable) {
        this.validationType = validationType;
        this.externalFlagVariable = externalFlagVariable;
        this.externalFlagVariableKey = externalFlagVariable.getKey();
    }

    @Override
    public void execute() {
        // deterministic blocks don't do anything
    }

    public boolean preCheck() {
        boolean otherLayersShouldExec = Boolean.parseBoolean(memory.read(externalFlagVariableKey).getValue().toString());
        this.internalFlagVariable.setValue(otherLayersShouldExec);
        return otherLayersShouldExec;
    }
}
