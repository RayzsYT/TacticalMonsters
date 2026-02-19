package de.rayzs.tacticalmonsters.impl.configuration;

import de.rayzs.tacticalmonsters.api.configuration.*;

import java.io.File;
import java.util.*;

public class ConfigProviderImpl implements ConfigProvider {

    private final String defaultFolderPath = "plugins/TacticalMonsters";
    private final Map<String, Config> configs = new HashMap<>();


    // Create default files if the folder does not exist yet.
    public ConfigProviderImpl() {
        final File folder = new File(defaultFolderPath);

        if (folder.isDirectory()) {
            return;
        }

        if (!folder.mkdirs()) {
            throw new RuntimeException("Failed to create TacticalMonsters folder! (" + defaultFolderPath + ")");
        }

        // TO-DO:
        // Create implementation to load all files from inside project
        // into the plugins/TacticalMonsters folder.
    }

    @Override
    public Config getOrCreate(final String fileName) {
        return getOrCreate(null, fileName);
    }

    @Override
    public Config getOrCreate(final String filePath, final String fileName) {
        final String path = defaultFolderPath + (filePath != null ? ("/" + filePath) : "");
        final String id = path + "/" + fileName;

        Config config = configs.get(id);
        if (config != null) {
            return config;
        }

        config = new ConfigImpl(path, fileName);

        configs.put(id, config);
        return config;
    }
}
