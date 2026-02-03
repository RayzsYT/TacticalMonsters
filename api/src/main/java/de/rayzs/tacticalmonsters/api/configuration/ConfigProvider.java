package de.rayzs.tacticalmonsters.api.configuration;

public interface ConfigProvider {
    Config getOrCreate(String filePath, String fileName);
}