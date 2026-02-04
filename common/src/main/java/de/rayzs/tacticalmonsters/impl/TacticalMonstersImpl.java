package de.rayzs.tacticalmonsters.impl;

import de.rayzs.tacticalmonsters.impl.scheduler.bukkit.BukkitSchedulerProvider;
import de.rayzs.tacticalmonsters.impl.scheduler.folia.FoliaSchedulerProvider;
import de.rayzs.tacticalmonsters.impl.configuration.ConfigProviderImpl;
import de.rayzs.tacticalmonsters.api.configuration.ConfigProvider;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerProvider;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.listener.AntiWitherCheeseHandler;
import de.rayzs.tacticalmonsters.listener.MonsterHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;
import org.bukkit.entity.*;
import java.util.*;

public class TacticalMonstersImpl implements TacticalMonstersAPI {

    private final Map<EntityType, Map<Monster, Player>> monsters = new HashMap<>();
    private final Set<MonsterAttack<? extends Monster>> attacks = new HashSet<>();

    private final JavaPlugin plugin;
    private final Random random;

    private final SchedulerProvider schedulerProvider;
    private final ConfigProvider configProvider;

    public TacticalMonstersImpl(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.random = new Random();

        this.configProvider = new ConfigProviderImpl();


        boolean folia = false;

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException ignored) { }


        this.schedulerProvider = folia
                ? new FoliaSchedulerProvider(this)
                : new BukkitSchedulerProvider(this);
    }

    public Map<Monster, Player> getMonsters(final EntityType entityType) {
        return this.monsters.getOrDefault(entityType, new HashMap<>());
    }

    public void updateMonsters(final EntityType entityType, Map<Monster, Player> monsters) {
        this.monsters.put(entityType, monsters);
    }

    @Override
    public void registerAttack(
            final Class<? extends MonsterAttack<?>> attackClazz
    ) {

        try {
            final Constructor<?> constructor = attackClazz.getDeclaredConstructor(
                    TacticalMonstersAPI.class, Random.class
            );

            final Object monsterAttackObj = constructor.newInstance(this, random);
            final MonsterAttack<? extends Monster> monsterAttack = (MonsterAttack<Monster>) monsterAttackObj;

            this.attacks.add(monsterAttack);


            if (monsterAttack.isEnabled()) {

                final String entityName = monsterAttack.getEntityType().name()
                        .replace("_", " ")
                        .toLowerCase();

                getLogger().info("Registered "
                        + attackClazz.getSimpleName()
                        + " for " + entityName);
            }

        } catch (Exception exception) { exception.printStackTrace(); }
    }

    @Override
    public Set<MonsterAttack<? extends Monster>> getRegisteredAttacks() {
        return Collections.unmodifiableSet(this.attacks);
    }

    @Override
    public void unregisterAttack(Class<? extends MonsterAttack<?>> attackClazz) {
        this.attacks.removeIf(attack -> {
            if (attack.getClass().equals(attackClazz)) {
                attack.eliminate();
                return true;
            }

            return false;
        });
    }

    @Override
    public void unregisterAllAttacks() {
        this.attacks.removeIf(attack -> {
            attack.eliminate();
            return true;
        });
    }

    @Override
    public void reload() {
        HandlerList.unregisterAll(plugin);

        final PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new MonsterHandler(this), plugin);
        manager.registerEvents(new AntiWitherCheeseHandler(this), plugin);


        if (getRegisteredAttacks().isEmpty()) {
            return;
        }


        final HashSet<Class<MonsterAttack<? extends Monster>>> attackClasses = new HashSet<>();

        for (MonsterAttack<? extends Monster> attack : getRegisteredAttacks()) {
            final Class<MonsterAttack<? extends Monster>> attackClass = (Class<MonsterAttack<? extends Monster>>) attack.getClass();
            attackClasses.add(attackClass);
        }

        unregisterAllAttacks();

        attackClasses.forEach(this::registerAttack);
    }

    @Override
    public SchedulerProvider getSchedulerProvider() {
        return this.schedulerProvider;
    }

    @Override
    public ConfigProvider getConfigProvider() {
        return this.configProvider;
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
}
