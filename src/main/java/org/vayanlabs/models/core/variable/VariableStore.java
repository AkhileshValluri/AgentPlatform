package org.vayanlabs.models.core.variable;

import java.util.*;

import java.util.*;

public class VariableStore {
    private VariableBackend backend;

    // Single in-memory key → Variable map
    private Map<String, Variable> memory = new HashMap<>();


    public VariableStore(VariableBackend backend) {
        this.backend = backend == null ? new InMemoryBackend() : backend;
    }

    /* Utility: Generate non-colliding key */
    private String generateUniqueKey(String base) {
        String candidate = base;
        while (memory.containsKey(candidate)) {
            candidate = base + "_" + UUID.randomUUID().toString().substring(0, 8);
        }
        return candidate;
    }

    /* -------------------------------------------------------------
     * CRUD OPERATIONS (BY KEY)
     * ------------------------------------------------------------- */

    public Variable create(String key, Object value) {
        String uniqueKey = generateUniqueKey(key);
        Variable v = new Variable(uniqueKey, value, backend);
        memory.put(uniqueKey, v);
        return v;
    }

    public Variable read(String key) {
        return memory.get(key);
    }

    public void update(String key, Object newValue) {
        Variable v = memory.get(key);
        if (v != null) {
            v.setValue(newValue);
        }
    }

    public void delete(String key) {
        Variable v = memory.remove(key);
        if (v != null) {
            v.delete();
        }
    }

    public boolean exists(String key) {
        return memory.containsKey(key);
    }

    /* -------------------------------------------------------------
     * CRUD OPERATIONS (BY VARIABLE REFERENCE)
     * ------------------------------------------------------------- */

    public void update(Variable v, Object newValue) {
        if (v != null) v.setValue(newValue);
    }

    public void delete(Variable v) {
        if (v != null) {
            memory.remove(v.getKey());
            v.delete();
        }
    }
}
