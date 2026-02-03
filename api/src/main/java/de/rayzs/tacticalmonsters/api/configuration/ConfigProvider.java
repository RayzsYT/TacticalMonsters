package de.rayzs.tacticalmonsters.api.configuration;

public interface ConfigProvider {

    /**
     * Gets an already existing Config instance or creates a new one.
     *
     * @param filePath Path to the config file.
     * @param fileName Name of the config file.
     * @return The existing or newly created Config instance.
     */
    Config getOrCreate(String filePath, String fileName);
}