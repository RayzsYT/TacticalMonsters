package de.rayzs.tacticalmonsters.impl;

import de.rayzs.tacticalmonsters.impl.bukkit.BukkitSchedulerProvider;
import de.rayzs.tacticalmonsters.impl.folia.FoliaSchedulerProvider;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerProvider;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
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

    public TacticalMonstersImpl(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.random = new Random();


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
            final EntityType type,
            final Class<MonsterAttack<? extends Monster>> attackClazz
    ) {

        try {
            final Constructor<?> constructor = attackClazz.getDeclaredConstructor(
                    TacticalMonstersAPI.class, EntityType.class, Random.class
            );

            final Object monsterAttackObj = constructor.newInstance(this, type, random);
            final MonsterAttack<? extends Monster> monsterAttack = (MonsterAttack<Monster>) monsterAttackObj;

            this.attacks.add(monsterAttack);

        } catch (Exception exception) { exception.printStackTrace(); }
    }

    @Override
    public void unregisterAttack(Class<MonsterAttack<? extends Monster>> attackClazz) {
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
    public SchedulerProvider getScheduler() {
        return this.schedulerProvider;
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
