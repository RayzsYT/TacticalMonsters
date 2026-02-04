package de.rayzs.tacticalmonsters;

import de.rayzs.tacticalmonsters.attacks.WitherSkeletonAttack;
import de.rayzs.tacticalmonsters.commands.ReloadCommand;
import de.rayzs.tacticalmonsters.impl.TacticalMonstersImpl;
import de.rayzs.tacticalmonsters.api.helper.VersionHelper;
import de.rayzs.tacticalmonsters.attacks.SkeletonAttack;
import de.rayzs.tacticalmonsters.attacks.SpiderAttack;
import de.rayzs.tacticalmonsters.attacks.ZombieAttack;
import de.rayzs.tacticalmonsters.api.TacticalMonsters;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.HandlerList;

public class TacticalMonstersLoader extends JavaPlugin {

    private TacticalMonstersImpl api;

    @Override
    public void onEnable() {

        if (!VersionHelper.isSupported()) {
            getLogger().warning("This Minecraft version is not supported! (" + VersionHelper.MIN_SUPPORTED_VERSION.name() + " and above only!)");
            return;
        }

        api = new TacticalMonstersImpl(this);
        TacticalMonsters.setInstance(api);


        final PluginCommand pluginCommand = this.getCommand("tacticalmonstersreload");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(new ReloadCommand(api));
            pluginCommand.setTabCompleter(new ReloadCommand(api));
        }


        api.reload();

        api.registerAttack(WitherSkeletonAttack.class);
        api.registerAttack(SkeletonAttack.class);
        api.registerAttack(ZombieAttack.class);
        api.registerAttack(SpiderAttack.class);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        api.unregisterAllAttacks();
    }
}
