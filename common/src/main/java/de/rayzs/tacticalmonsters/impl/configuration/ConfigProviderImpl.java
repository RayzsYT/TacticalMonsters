package de.rayzs.tacticalmonsters.impl.configuration;

import de.rayzs.tacticalmonsters.api.configuration.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigProviderImpl implements ConfigProvider {

    private final Map<String, Config> configs = new HashMap<>();

    @Override
    public Config getOrCreate(String filePath, String fileName) {
        final String id = ((filePath != null) ? (filePath + "/") : "") + fileName;

        Config config = configs.get(id);
        if (config != null) {
            return config;
        }

        config = new ConfigImpl(filePath, fileName);

        configs.put(id, config);
        return config;
    }
}
