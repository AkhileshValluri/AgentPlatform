package org.vayanlabs.models.core.layer;

import org.vayanlabs.models.core.variable.Variable;

public class ValidationLayer extends Layer {

    private static final String VALIDATION_PATH = "./mock/validation/rules.txt";

    @Override
    public void execute() {
        System.out.println("[ValidationLayer] Validating node preconditions...");

         String rules ="";
        System.out.println("[ValidationLayer] Loaded Validation Rules:\n" + rules);

        // MOCK LOGIC: Read loop counter
        Variable counter = memory.read("loopCounter");
        if (counter != null) {
            int value = Integer.parseInt(counter.getValue().toString());
            System.out.println("[ValidationLayer] loopCounter = " + value);

            if (value <= 0) {
                System.out.println("[ValidationLayer] Loop ended");
            } else {
                System.out.println("[ValidationLayer] Loop continues");
            }
        }
    }
}
