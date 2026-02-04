package de.rayzs.tacticalmonsters.listener;

import de.rayzs.tacticalmonsters.api.configuration.Config;
import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import org.bukkit.event.entity.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.*;

public class AntiWitherCheeseHandler implements Listener {

    private final Config config;

    private final boolean disableIronGolemsInNether;
    private final boolean witherOnlyInNether;
    private final boolean witherBreakObsidian;
    private final int maxWitherBuiltHeight;

    public AntiWitherCheeseHandler(final TacticalMonstersAPI api) {
        this.config = api.getConfigProvider().getOrCreate("anti-wither-cheese.yml");

        this.disableIronGolemsInNether = config.getOrSet("disable-iron-golems-in-nether", true);
        this.witherOnlyInNether = config.getOrSet("wither-only-in-nether", true);
        this.witherBreakObsidian = config.getOrSet("wither-break-obsidian", true);
        this.maxWitherBuiltHeight = config.getOrSet("max-wither-spawn-height", 160);
    }

    @EventHandler
    public void onWitherSpawn(final CreatureSpawnEvent event) {
        final Entity entity = event.getEntity();

        if (!witherOnlyInNether) {
            return;
        }

        if (entity.getType() != EntityType.WITHER) {
            return;
        }

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.BUILD_WITHER) {
            return;
        }

        final Location location = entity.getLocation();
        final World world = location.getWorld();

        if (world == null) {
            return;
        }

        if (world.getEnvironment() != World.Environment.NETHER
                || location.getBlockY() >= maxWitherBuiltHeight) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onIronGolemSpawn(final CreatureSpawnEvent event) {
        final Entity entity = event.getEntity();

        if (!disableIronGolemsInNether) {
            return;
        }

        if (entity.getType() != EntityType.IRON_GOLEM) {
            return;
        }

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM) {
            return;
        }

        final Location location = entity.getLocation();
        final World world = location.getWorld();

        if (world == null) {
            return;
        }

        if (world.getEnvironment() == World.Environment.NETHER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onIronGolemTeleport(final EntityTeleportEvent event) {
        final Entity entity = event.getEntity();

        if (!disableIronGolemsInNether) {
            return;
        }

        if (entity.getType() != EntityType.IRON_GOLEM) {
            return;
        }

        final World world = entity.getLocation().getWorld();
        if (world == null) {
            return;
        }

        if (world.getEnvironment() == World.Environment.NETHER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWitherTeleport(final EntityTeleportEvent event) {
        final Entity entity = event.getEntity();

        if (!witherOnlyInNether) {
            return;
        }

        if (entity.getType() != EntityType.WITHER) {
            return;
        }

        final World world = entity.getLocation().getWorld();
        if (world == null) {
            return;
        }

        if (world.getEnvironment() == World.Environment.NETHER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWitherGrief(final EntityChangeBlockEvent event) {
        final Entity entity = event.getEntity();
        final Block block = event.getBlock();

        if (!witherBreakObsidian) {
            return;
        }

        if (entity.getType() != EntityType.WITHER) {
            return;
        }

        if (block.getType() == Material.BEDROCK) {
            return;
        }

        if (block.getType().name().contains("OBSIDIAN")) {
            block.getWorld().playSound(entity, Sound.BLOCK_STONE_BREAK, 1f, 1f);
        }

        block.getWorld().playSound(entity, Sound.ENTITY_WITHER_BREAK_BLOCK, 1f, 1f);
        block.breakNaturally();
    }

    @EventHandler
    public void onWitherExplode(final EntityExplodeEvent event) {
        final Entity entity = event.getEntity();

        if (!witherBreakObsidian) {
            return;
        }

        if (entity.getType() != EntityType.WITHER_SKULL) {
            return;
        }

        event.blockList().removeIf(block -> {
            if (block.getType().name().contains("OBSIDIAN")) {
                block.breakNaturally();
                return true;
            }

            return false;
        });
    }
}
