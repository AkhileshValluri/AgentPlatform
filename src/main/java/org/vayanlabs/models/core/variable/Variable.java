package org.vayanlabs.models.core.variable;

import java.io.Serializable;
import java.util.Objects;

public class Variable implements Serializable {

    private final String key;
    private Object value;  // must be serializable
    private final VariableBackend backend;

    public Variable(String key, Object value, VariableBackend backend) {
        this.key = Objects.requireNonNull(key, "Variable key cannot be null");
        this.value = value;
        this.backend = backend;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        // Simple variables (no backend): stored in memory only
        if (backend == null) {
            return value;
        }

        // Persisted variables: load from backend
        try {
            return backend.load(key);
        } catch (VariableStoreException e) {
            throw new RuntimeException("Failed to load variable: " + key, e);
        }
    }

    public void setValue(Object newValue) {
        this.value = newValue;

        // Persist if backend exists
        if (backend != null) {
            try {
                backend.save(key, newValue);
            } catch (VariableStoreException e) {
                throw new RuntimeException("Failed to persist variable: " + key, e);
            }
        }
    }

    public boolean hasBackend() {
        return backend != null;
    }

    public VariableBackend getBackend() {
        return backend;
    }

    public void delete() {
        if (backend != null) {
            try {
                backend.delete(key);
            } catch (VariableStoreException e) {
                throw new RuntimeException("Failed to delete variable: " + key, e);
            }
        }
    }

    @Override
    public String toString() {
        return "Variable{" +
                "key='" + key + '\'' +
                ", value=" + getValue() +
                ", backend=" + (backend != null ? backend.getClass().getSimpleName() : "ephemeral") +
                '}';
    }
}
