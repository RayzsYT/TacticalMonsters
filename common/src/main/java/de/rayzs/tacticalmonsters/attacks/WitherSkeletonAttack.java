package de.rayzs.tacticalmonsters.attacks;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Random;

public class WitherSkeletonAttack extends MonsterAttack<WitherSkeleton> {

    public WitherSkeletonAttack(final TacticalMonstersAPI api, final Random random) {
        super(EntityType.WITHER_SKELETON, api, random);
    }

    @Override
    public void attack(WitherSkeleton monster, Player player) {

        if (pushAttack(monster, player)) {
            return;
        }

        if (shadowAttack(monster, player)) {
            return;
        }

    }


    private final boolean SHADOW_ENABLED = get("shadow.enabled", true);
    private final int SHADOW_CHANCE = get("shadow.chance", 30);

    private boolean shadowAttack(final WitherSkeleton monster, final Player player) {
        if (!SHADOW_ENABLED || !shouldDo(SHADOW_CHANCE)) {
            return false;
        }

        final Location behind = player.getLocation().clone();
        final Vector dir = behind.getDirection().normalize().multiply(-2);

        behind.add(dir);
        behind.setYaw(player.getLocation().getYaw() + 180);
        behind.setPitch(0);

        if (!behind.getBlock().isPassable()) {
            return false;
        }

        particle(monster.getLocation(), Color.BLACK,
                3, 50,
                0.3, 1, 0.3, 0.01
        );

        particle(monster.getLocation().clone().add(0, 1, 0),
                Particle.SOUL_FIRE_FLAME,50,
                0.3, 0.5, 0.3, 0.01
        );


        player.hideEntity(api.getPlugin(), monster);
        monster.teleport(behind);



        api.getSchedulerProvider().createScheduler(new SchedulerTask() {
            @Override
            public void run() {
                player.showEntity(api.getPlugin(), monster);
            }
        }, 1);

        particle(behind, Color.BLACK,
                3, 50,
                0.3, 1, 0.3, 0.01
        );

        monster.getWorld().playSound(
                monster.getLocation(),
                "ENTITY_ENDERMAN_TELEPORT",
                0.8f,
                0.6f
        );

        monster.getWorld().playSound(
                monster.getLocation(),
                "ENTITY_GENERIC_EXTINGUISH_FIRE",
                0.8f,
                0.6f
        );

        return true;
    }



    private final boolean PUSH_ENABLED = get("push.enabled", true);
    private final int PUSH_CHANCE = get("push.chance", 15);

    private final double PUSH_RADIUS = get("push.radius", 4);
    private final double PUSH_DAMAGE = get("push.damage", 0.4);

    private boolean pushAttack(final WitherSkeleton monster, final Player player) {
        if (!PUSH_ENABLED || !shouldDo(PUSH_CHANCE) || monster.isInsideVehicle()) {
            return false;
        }

        sound(monster.getLocation(),
                "BLOCK_BEACON_ACTIVATE",
                2.0f,
                0.5f
        );

        monster.setGlowing(true);

        particle(monster.getLocation().add(0, 1, 0), Color.AQUA,
                3, 50,
                0.3, 1, 0.3, 0.01
        );

        api.getSchedulerProvider().createScheduler(new SchedulerTask() {
            @Override
            public void run() {
                monster.setGlowing(false);

                final Location startLocation = monster.getLocation().clone().add(0, 1, 0);

                particle(startLocation, Particle.EXPLOSION_NORMAL, 1,
                        0.4, 0.2, 0.4, 0.02
                );

                particle(startLocation, Color.RED,
                        3, 50,
                        2, 0.5, 2, 0.5
                );

                particle(startLocation, Color.BLACK, 3, 50,
                        2, 0.5, 2, 0.5
                );

                sound(
                        monster.getLocation(),
                        "ENTITY_WITHER_BREAK_BLOCK",
                        1.0f,
                        1.2f
                );

                monster.getNearbyEntities(PUSH_RADIUS, PUSH_RADIUS, PUSH_RADIUS).forEach(entity -> {
                    if (! (entity instanceof Player target)) {
                        return;
                    }

                    if (shouldIgnorePlayer(target)) {
                        return;
                    }

                    if (!target.isBlocking()) {
                        pushBack(target, monster.getLocation(), 1.8, 0.4);
                        hurt(player, monster, 0.2f, PUSH_DAMAGE);
                    } else {
                        shieldBlockedSound(target);

                        pushBack(target, monster.getLocation(), 0.6, 0);
                        hurt(player, monster, 0.2f, 0);
                    }
                });
            }
        }, 10);

        return true;
    }
}
