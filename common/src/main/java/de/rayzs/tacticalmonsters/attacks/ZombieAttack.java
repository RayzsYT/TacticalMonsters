package de.rayzs.tacticalmonsters.attacks;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import java.util.Random;

public class ZombieAttack extends MonsterAttack<Zombie> {

    public ZombieAttack(final TacticalMonstersAPI api, final Random random) {
        super(EntityType.ZOMBIE, api, random);
    }

    @Override
    public void attack(Zombie monster, org.bukkit.entity.Player player) {

    }


    private final boolean STOMP_ENABLED = get("attack.enabled", true);
    private final int STOMP_CHANCE = get("attack.chance", 10);
    private final double STOMP_RADIUS = get("attack.radius", 4.0);
    private final double STOMP_DAMAGE = get("attack.damage", 4.0);

    public boolean stompAttack(final Zombie monster, final Player player) {
        if (!STOMP_ENABLED || !shouldDo(STOMP_CHANCE)) {
            return false;
        }


        sound(monster.getLocation(),
                "ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR",
                1.0f,
                0.8f
        );

        sound(monster.getLocation(),
                "ENTITY_BREEZE_WIND_BURST",
                1.0f,
                0.8f
        );

        particle(monster.getLocation(),
                Particle.CLOUD,
                10,
                0.5, 0.5, 0.5,
                0.1
        );


        final double originY = monster.getLocation().getY();
        final double goalY = originY + 4;

        pushTowards(monster, player.getLocation(), 1.0, 2);

        api.getSchedulerProvider().createScheduler(new SchedulerTask() {

            private boolean reachedPoint = false;
            private double lastY = originY;

            @Override
            public void run() {

                if (monster.isDead() || monster.isSwimming()) {
                    stop();
                    return;
                }

                if (!reachedPoint) {

                    if (monster.getY() >= goalY) {
                        reachedPoint = true;
                    } else {
                        final double currentY = monster.getY();

                        // Entity is falling without reaching goal y.
                        if (currentY < lastY) {
                            stop();
                            return;
                        }

                        lastY = currentY;
                    }

                    return;
                }

                if (!monster.isOnGround()) {
                    return;
                }


                final Location center = monster.getLocation().clone();

                for (int x = -3; x <= 3; x++) {
                    for (int z = -3; z <= 3; z++) {
                        final Location location = center.clone().add(x, 0, z);
                        location.setY(center.getY() + 0.25);

                        particle(location, Particle.CLOUD, 1,
                                0.5, 0.1, 0.5, 0.02
                        );

                        sound(location,
                                "BLOCK_SAND_STEP",
                                1.0f,
                                1.0f
                        );

                        sound(location,
                                "BLOCK_GRASS_STEP",
                                1.0f,
                                1.0f
                        );

                        sound(location,
                                "BLOCK_STONE_STEP",
                                1.0f,
                                1.0f
                        );

                        sound(location,
                                "BLOCK_ANVIL_STEP",
                                1.0f,
                                0.6f
                        );
                    }
                }

                monster.getNearbyEntities(STOMP_RADIUS, STOMP_RADIUS, STOMP_RADIUS).forEach(entity -> {
                    if (! (entity instanceof LivingEntity livingEntity) || !entity.isOnGround()) {
                        return;
                    }

                    if (entity instanceof Player nearbyPlayer) {
                        if (shouldIgnorePlayer(nearbyPlayer)) {
                            return;
                        }

                        sound(nearbyPlayer, livingEntity.getLocation(),
                                "ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR",
                                1.0f,
                                0.8f
                        );
                    }

                    pushBack(livingEntity, monster.getLocation(), 0.8, 0.3);
                    hurt(livingEntity, monster, 0.7f, STOMP_DAMAGE);
                });

                stop();
            }
        }, 1, 1);

        return true;
    }
}
