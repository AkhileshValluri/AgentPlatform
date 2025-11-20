package org.vayanlabs.models.core.variable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface VariableBackend {
    public void save(String key, Object value) throws VariableStoreException;
    public Object load(String key) throws VariableStoreException;
    public void delete(String key) throws VariableStoreException;
}