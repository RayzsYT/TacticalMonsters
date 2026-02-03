package de.rayzs.tacticalmonsters.impl.configuration;

import de.rayzs.tacticalmonsters.api.configuration.Config;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.Collection;
import java.io.File;

public class ConfigImpl implements Config {

    private final String filePath, fileName;

    private File file;
    private YamlConfiguration configuration;

    public ConfigImpl(final String fileName) {
        this(null, fileName);
    }

    public ConfigImpl(final String filePath, final String fileName) {
        this.fileName = fileName;

        final String defaultPath = "plugins/TacticalMonsters";
        this.filePath = defaultPath + (filePath == null ? "" : "/" + filePath);
    }

    private void load() {
        this.file = new File(filePath, fileName + ".yml");
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Config set(final String path, final String target, final Object object) {
        this.configuration.set(
                ((path != null) ? (path + ".") : "") + target,
                object instanceof String ? ((String) object).replace("§", "&") : object
        );

        return this;
    }

    @Override
    public ConfigImpl set(final String target, final Object object) {
        set(null, target, object);
        return this;
    }

    @Override
    public ConfigImpl setAndSave(final String path, final String target, final Object object) {
        set(path, target, object);
        save();

        return this;
    }

    @Override
    public ConfigImpl setAndSave(final String target, final Object object) {
        set(target, object);
        save();

        return this;
    }

    @Override
    public <T> T getOrSet(final String path, final String target, final T t) {
        T result = (T) get(path, target);

        if (result != null) {
            return result;
        }

        set(path, target, t);
        save();

        return (T) get(path, target);
    }

    @Override
    public <T> T getOrSet(final String target, final T t) {
        T result = (T) get(target);

        if (result != null) {
            return result;
        }

        set(target, t);
        save();

        return (T) get(target);
    }

    @Override
    public Object get(final String target) {
        return get(null, target);
    }

    @Override
    public Object get(final String path, final String target) {
        Object object = this.configuration.get(((path != null) ? (path + ".") : "") + target);

        if (object instanceof String str) {
            return str.replace("&", "§");
        }

        return object;
    }

    @Override
    public Collection<String> getKeys(final boolean deep) {
        return this.configuration.getKeys(deep);
    }

    @Override
    public Collection<String> getKeys(final String section, final boolean deep) {
        return this.configuration.getConfigurationSection(section).getKeys(deep);
    }
}