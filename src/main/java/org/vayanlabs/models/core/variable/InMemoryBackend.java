package org.vayanlabs.models.core.variable;

public class InMemoryBackend implements VariableBackend {

    @Override
    public void save(String key, Object value) throws VariableStoreException {

    }

    @Override
    public Object load(String key) throws VariableStoreException {
        return null;
    }

    @Override
    public void delete(String key) throws VariableStoreException {

    }
}

