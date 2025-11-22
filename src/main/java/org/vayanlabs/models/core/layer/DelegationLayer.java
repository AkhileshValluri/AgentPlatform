package org.vayanlabs.models.core.layer;

import org.vayanlabs.models.core.Node;
import org.vayanlabs.models.core.variable.Variable;
import org.vayanlabs.models.core.variable.VariableStore;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class Condition {

    private final Object left;
    private final Object right;

    public Condition(Object left, Object right) {
        this.left = left;
        this.right = right;
    }

    public boolean greaterThan() {
        return asDouble(left) > asDouble(right);
    }

    public boolean lessThan() {
        return asDouble(left) < asDouble(right);
    }

    public boolean equalsTo() {
        return left.equals(right);
    }

    public boolean notEquals() {
        return !equalsTo();
    }

    public boolean greaterThanOrEqual() {
        return asDouble(left) >= asDouble(right);
    }

    public boolean lessThanOrEqual() {
        return asDouble(left) <= asDouble(right);
    }

    private double asDouble(Object o) {
        return Double.parseDouble(o.toString());
    }
}

public class DelegationLayer extends Layer {

    public enum DelegationType {
        IF,
        LOOP
    }

    private DelegationType delegationType;
    private Condition condition;

    // Flag variable where the result of the delegation will be saved.
    private String workflowFlagKey;

    // Nodes to call blindly in parallel
    private List<Node> nextNodes;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public void init(DelegationType delegationType,
                           Condition condition,
                           List<Node> nextNodes) {
        this.delegationType = delegationType;
        this.condition = condition;
        this.nextNodes = nextNodes;
    }

    public DelegationLayer(VariableStore memory,
                     VariableStore workflowArtifacts,
                     VariableStore workflowMemories) {

        super(memory, workflowArtifacts, workflowMemories);

        // Create unique flag name for flow control
        this.workflowFlagKey = "_flowflag_" + UUID.randomUUID().toString().substring(0, 8);

        // Pre-create the variable
        workflowMemories.create(workflowFlagKey, false);
    }

    @Override
    public void init() {}

    @Override
    public void execute() {

        boolean result = switch (delegationType) {
            case IF -> condition.equalsTo();
            case LOOP -> condition.greaterThan(); // example: loop until > condition
        };

        // Store result in workflow memories
        Variable v = workflowMemories.read(workflowFlagKey);
        v.setValue(result);

        // Spawn all next nodes blindly in parallel
        for (Node node : nextNodes) {
            executor.submit(node::execute);
        }
    }

    public String getWorkflowFlagKey() {
        return workflowFlagKey;
    }
}
