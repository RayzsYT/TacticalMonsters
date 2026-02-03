package de.rayzs.tacticalmonsters;

import de.rayzs.tacticalmonsters.api.TacticalMonsters;
import de.rayzs.tacticalmonsters.attacks.SkeletonAttack;
import de.rayzs.tacticalmonsters.attacks.WitherSkeletonAttack;
import de.rayzs.tacticalmonsters.impl.TacticalMonstersImpl;
import de.rayzs.tacticalmonsters.listener.MonsterHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TacticalMonstersLoader extends JavaPlugin {

    private TacticalMonstersImpl api;

    @Override
    public void onEnable() {
        api = new TacticalMonstersImpl(this);
        TacticalMonsters.setInstance(api);


        final PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new MonsterHandler(api), api.getPlugin());


        api.registerAttack(EntityType.WITHER_SKELETON, WitherSkeletonAttack.class);
        api.registerAttack(EntityType.SKELETON, SkeletonAttack.class);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        api.unregisterAllAttacks();
    }
}
