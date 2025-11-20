
public class EntitlementLayer extends Layer {

    private static final String AUTHZ_POLICY_PATH = "./mock/auth/policy.txt";

    @Override
    public void execute() {
        System.out.println("[AuthorizationLayer] Executing authorization rules...");

//        String policy = FileUtil.readFile(AUTHZ_POLICY_PATH);
        String policy = "";
        System.out.println("[AuthorizationLayer] Loaded Policy:\n" + policy);

        // MOCK LOGIC: read user identity from VariableStore
        Variable user = memory.read("user");
        String username = user != null ? user.getValue().toString() : "unknown";

        System.out.println("[AuthorizationLayer] Checking authorization for user=" + username);
//        String entRules = FileUtil.readFile(ENTITLEMENT_PATH);
        String entRules = "";
        System.out.println("[EntitlementLayer] Entitlement Rules Loaded:\n" + entRules);

        Variable requestType = memory.read("requestType");
        System.out.println("[EntitlementLayer] Request Type = " +
                (requestType != null ? requestType.getValue() : "unknown"));
    }
}
