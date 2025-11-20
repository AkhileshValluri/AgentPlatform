package org.vayanlabs.models.core.variable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArtifactBackend implements VariableBackend {

    private final Path root;

    public ArtifactBackend(String storageDir) throws VariableStoreException {
        this.root = Paths.get(storageDir);
        initStorage();
    }

    private void initStorage() throws VariableStoreException {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new VariableStoreException("Could not create artifact storage directory", e);
        }
    }

    @Override
    public void save(String key, Object value) throws VariableStoreException {
        if (!(value instanceof byte[] data)) {
            throw new VariableStoreException("ArtifactBackend only supports byte[] values.");
        }

        Path path = resolveSafe(key);

        if (Files.exists(path)) {
            throw new VariableStoreException("Artifact already exists: " + key);
        }

        try {
            Files.write(path, value.toString().getBytes());
        } catch (IOException e) {
            throw new VariableStoreException("Failed to save artifact: " + key, e);
        }
    }

    @Override
    public Object load(String key) throws VariableStoreException {
        Path path = resolveSafe(key);

        if (!Files.exists(path)) {
            throw new VariableStoreException(key);
        }

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new VariableStoreException("Failed to read artifact: " + key, e);
        }
    }

    @Override
    public void delete(String key) throws VariableStoreException {
        Path path = resolveSafe(key);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new VariableStoreException("Failed to delete artifact: " + key, e);
        }
    }

    private Path resolveSafe(String key) {
        // sanitize key to avoid path traversal
        String safeKey = key.replaceAll("[^a-zA-Z0-9._-]", "_");
        return root.resolve(safeKey).normalize();
    }
}
