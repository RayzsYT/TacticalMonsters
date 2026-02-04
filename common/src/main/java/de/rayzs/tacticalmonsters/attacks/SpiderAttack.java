package de.rayzs.tacticalmonsters.attacks;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;

import java.util.*;

public class SpiderAttack extends MonsterAttack<Spider> {

    public SpiderAttack(final TacticalMonstersAPI api, final Random random) {
        super(EntityType.SPIDER, api, random);
    }

    @Override
    public void attack(Spider monster, org.bukkit.entity.Player player) {
        if (webAttack(monster, player)) {
            return;
        }
    }


    private final boolean WEB_ENABLED = get("web.enabled", true);
    private final int WEB_CHANCE = get("web.chance", 30);

    private boolean webAttack(final Spider monster, final Player player) {
        if (!WEB_ENABLED || !shouldDo(WEB_CHANCE)) {
            return false;
        }

        final int times = random.nextInt(5);

        final List<Location> locationsAroundPlayer = new ArrayList<>();
        final Location originLocation = player.getLocation().clone();

        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                for (int y = 0; y < 2; y++) {
                    final Location location = originLocation.clone().add(x, y, z);
                    final Block block = location.getBlock();

                    if (!block.getType().isSolid() && !block.isLiquid()) {
                        locationsAroundPlayer.add(originLocation.clone().add(x, y, z));
                    }
                }
            }
        }


        final int maxAroundLocations = locationsAroundPlayer.size();

        for (int i = 0; i < times; i++) {
            final Location location = locationsAroundPlayer.get(random.nextInt(maxAroundLocations));

            api.getSchedulerProvider().createScheduler(new SchedulerTask() {

                @Override
                public void run() {

                    if (player.isDead() || monster.isDead()) {
                        stop();
                        return;
                    }

                    location.getBlock().setType(org.bukkit.Material.COBWEB);

                    sound(location, "ENTITY_SPIDER_AMBIENT", 0.6f, 0.1f * random.nextInt(10));
                }
            }, 2);
        }

        return true;
    }
}
