package de.rayzs.tacticalmonsters;

import de.rayzs.tacticalmonsters.attacks.ManualRegistration;
import de.rayzs.tacticalmonsters.impl.TacticalMonstersImpl;
import de.rayzs.tacticalmonsters.api.helper.VersionHelper;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.commands.ReloadCommand;
import de.rayzs.tacticalmonsters.api.TacticalMonsters;
import com.google.common.reflect.ClassPath;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.entity.Monster;

public class TacticalMonstersLoader extends JavaPlugin {

    private TacticalMonstersImpl api;

    @Override
    public void onEnable() {

        // Check if software version is supported.
        if (!VersionHelper.isSupported()) {
            getLogger().warning("This Minecraft version is not supported! (" + VersionHelper.MIN_SUPPORTED_VERSION.name() + " and above only!)");
            return;
        }

        // Registers the API and its instance.
        api = new TacticalMonstersImpl(this);
        TacticalMonsters.setInstance(api);


        // Registers the command(s).
        final PluginCommand pluginCommand = this.getCommand("tacticalmonstersreload");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(new ReloadCommand(api));
            pluginCommand.setTabCompleter(new ReloadCommand(api));
        }

        // Registers the event listeners.
        api.reload();

        // Registers all monster attacks.
        registerAllClasses("de.rayzs.tacticalmonsters.attacks");
    }

    @Override
    public void onDisable() {
        // Unload all listeners.
        HandlerList.unregisterAll(this);

        // Unload all attacks.
        api.unregisterAllAttacks();
    }

    // This method here registers all classes inside a certain package associated with the MonsterAttack class
    // fully automatically, except for those inheriting from the ManualRegistration interface.
    private void registerAllClasses(final String packagePath) {
        try {
            final ClassPath classPath = ClassPath.from(TacticalMonstersLoader.class.getClassLoader());
            for (final ClassPath.ClassInfo topLevelClass : classPath.getTopLevelClasses(packagePath)) {
                final Class<?> clazz = Class.forName(topLevelClass.getName());

                if (!MonsterAttack.class.isAssignableFrom(clazz)) {
                    continue;
                }

                final Class<MonsterAttack <? extends Monster>> monsterAttackClazz = (Class<MonsterAttack <? extends Monster>>) Class.forName(topLevelClass.getName());

                boolean requiresManualRegistration = false;
                for (final Class<?> interfaceClazz : monsterAttackClazz.getInterfaces()) {
                    if (interfaceClazz == ManualRegistration.class) {
                        requiresManualRegistration = true;
                        break;
                    }
                }

                if (!requiresManualRegistration) {
                    api.registerAttack(monsterAttackClazz);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
