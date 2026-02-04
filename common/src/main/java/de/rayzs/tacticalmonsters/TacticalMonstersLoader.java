package de.rayzs.tacticalmonsters;

import de.rayzs.tacticalmonsters.api.TacticalMonsters;
import de.rayzs.tacticalmonsters.api.helper.VersionHelper;
import de.rayzs.tacticalmonsters.attacks.SkeletonAttack;
import de.rayzs.tacticalmonsters.attacks.SpiderAttack;
import de.rayzs.tacticalmonsters.attacks.WitherSkeletonAttack;
import de.rayzs.tacticalmonsters.attacks.ZombieAttack;
import de.rayzs.tacticalmonsters.impl.TacticalMonstersImpl;
import de.rayzs.tacticalmonsters.listener.AntiWitherCheeseHandler;
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

        if (!VersionHelper.isSupported()) {
            getLogger().warning("This Minecraft version is not supported! (" + VersionHelper.MIN_SUPPORTED_VERSION.name() + " and above only!)");
            return;
        }

        final long startTime = System.currentTimeMillis();

        api = new TacticalMonstersImpl(this);
        TacticalMonsters.setInstance(api);


        final PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new MonsterHandler(api), api.getPlugin());
        manager.registerEvents(new AntiWitherCheeseHandler(api), api.getPlugin());


        api.registerAttack(EntityType.WITHER_SKELETON, WitherSkeletonAttack.class);
        api.registerAttack(EntityType.SKELETON, SkeletonAttack.class);
        api.registerAttack(EntityType.ZOMBIE, ZombieAttack.class);
        api.registerAttack(EntityType.SPIDER, SpiderAttack.class);


        getLogger().info("Successfully loaded "
                + api.getRegisteredAttacks().size()
                + " monster attacks. ("
                + (System.currentTimeMillis() - startTime) + "ms)"
        );
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        api.unregisterAllAttacks();
    }
}
