package de.rayzs.tacticalmonsters;

import de.rayzs.tacticalmonsters.api.TacticalMonsters;
import de.rayzs.tacticalmonsters.impl.TacticalMonstersImpl;
import de.rayzs.tacticalmonsters.listener.MonsterHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TacticalMonstersLoader extends JavaPlugin {

    @Override
    public void onEnable() {
        final TacticalMonstersImpl api = new TacticalMonstersImpl(this);
        TacticalMonsters.setInstance(api);


        final PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new MonsterHandler(api), api.getPlugin());
    }

    @Override
    public void onDisable() {

    }
}
