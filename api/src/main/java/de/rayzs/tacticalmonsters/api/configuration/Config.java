package de.rayzs.tacticalmonsters.api.configuration;

import java.util.Collection;

public interface Config {

    void reload();
    void save();

    Config set(String path, String target, Object object);
    Config set(String target, Object object);

    Config setAndSave(String path, String target, Object object);
    Config setAndSave(String target, Object object);

    <T> T getOrSet(String path, String target, T t);
    <T> T getOrSet(String target, T t);

    Object get(String target);
    Object get(String path, String target);

    Collection<String> getKeys(boolean deep);
    Collection<String> getKeys(String section, boolean deep);
}