package de.rayzs.tacticalmonsters.listener;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import java.util.Map;

public class MonsterHandler implements Listener {

    private final TacticalMonstersAPI api;

    public MonsterHandler(final TacticalMonstersAPI api) {
        this.api = api;
    }

    @EventHandler
    public void onMonsterTarget(final EntityTargetEvent event) {
        final Entity entity = event.getEntity();
        final Entity target = event.getTarget();

        if (! (entity instanceof Monster monster)) {
            return;
        }

        final EntityType entityType = entity.getType();
        final Map<Monster, Player> monsters = api.getMonsters(entityType);

        if (target instanceof Player player) {
            monsters.put(monster, player);
        } else {
            monsters.remove(monster);
        }

        api.updateMonsters(entityType, monsters);
    }

    @EventHandler
    public void onMonsterDeath(final EntityDeathEvent event) {
        final Entity entity = event.getEntity();

        if (! (entity instanceof Monster monster)) {
            return;
        }

        final EntityType entityType = entity.getType();
        final Map<Monster, Player> monsters = api.getMonsters(entityType);

        monsters.remove(monster);
        api.updateMonsters(entityType, monsters);
    }
}
