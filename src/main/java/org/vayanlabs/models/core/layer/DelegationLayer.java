package org.vayanlabs.models.core.layer;

import org.vayanlabs.models.core.variable.Variable;

public class DelegationLayer extends Layer {

    private static final String DELEGATION_MAP_PATH = "./mock/delegation/routes.txt";

    @Override
    public void execute() {
        System.out.println("[DelegationLayer] Determining next step...");

//        String routingRules = FileUtil.readFile(DELEGATION_MAP_PATH);
        String routingRules = "";
        System.out.println("[DelegationLayer] Routing Rules:\n" + routingRules);

        // MOCK: choose next node name
        String nextNodeId = "node_" + (System.currentTimeMillis() % 3);
        System.out.println("[DelegationLayer] Mock delegation selected nextNode=" + nextNodeId);

        // store delegation result in workflow memories
        workflowMemories.create("nextNode", nextNodeId);
    }
}
