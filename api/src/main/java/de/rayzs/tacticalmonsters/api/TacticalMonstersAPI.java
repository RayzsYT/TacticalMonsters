package de.rayzs.tacticalmonsters.api;

import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.api.configuration.ConfigProvider;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.*;
import java.util.*;
import java.util.logging.Logger;

public interface TacticalMonstersAPI {

    /**
     * Gets the map of monsters targeting players for the given EntityType.
     *
     * @param entityType EntityType of the monsters.
     * @return Map of monsters targeting players.
     */
    Map<Monster, Player> getMonsters(final EntityType entityType);

    /**
     * Updates the internal monster map for the given EntityType.
     *
     * @param entityType EntityType of the monsters.
     * @param monsters Bew Nap of monsters targeting players.
     */
    void updateMonsters(final EntityType entityType, Map<Monster, Player> monsters);

    /**
     * Registers a new MonsterAttack for the given EntityType.
     *
     * @param type EntityType of the monster.
     * @param attackClazz Class of the MonsterAttack to register.
     */
    void registerAttack(
            final EntityType type,
            final Class<? extends MonsterAttack<?>> attackClazz
    );

    /**
     * Unregisters a registered MonsterAttack.
     *
     * @param attackClazz Class of the MonsterAttack to unregister.
     */
    void unregisterAttack(final Class<? extends MonsterAttack<?>> attackClazz);

    /**
     * Unregisters all registered MonsterAttacks.
     */
    void unregisterAllAttacks();

    /**
     * Gets the SchedulerProvider.
     *
     * @return SchedulerProvider instance.
     */
    SchedulerProvider getSchedulerProvider();

    /**
     * Gets the ConfigurationProvider.
     *
     * @return ConfigurationProvider instance.
     */
    ConfigProvider getConfigProvider();

    /**
     * Gets the Logger.
     *
     * @return Logger instance.
     */
    Logger getLogger();

    /**
     * Gets the JavaPlugin instance.
     *
     * @return JavaPlugin instance.
     */
    JavaPlugin getPlugin();
}
