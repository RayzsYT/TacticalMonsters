package de.rayzs.tacticalmonsters.attacks;

import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;

public class PillagerAttack extends MonsterAttack<Pillager> implements ManualRegistration {

    public PillagerAttack(final TacticalMonstersAPI api, final Random random) {
        super(EntityType.PILLAGER, api, random);
    }

    @Override
    public void attack(Pillager monster, org.bukkit.entity.Player player) {
        if (explosiveProjectileAttack(monster, player)) {
            return;
        }
    }


    private final boolean EXPLOSIVE_PROJECTILE_ENABLED = get("explosive_projectile.enabled", true);
    private final int EXPLOSIVE_PROJECTILE_CHANCE = get("explosive_projectile.chance", 20);
    private final double EXPLOSIVE_PROJECTILE_DAMAGE = get("explosive_projectile.damage", 6.0);

    private boolean explosiveProjectileAttack(final Pillager monster, final Player player) {
        if (!EXPLOSIVE_PROJECTILE_ENABLED || !shouldDo(EXPLOSIVE_PROJECTILE_CHANCE)) {
            return false;
        }

        if (monster.getActiveItem().getType() != Material.CROSSBOW) {
            return false;
        }

        monster.setGlowing(true);
        monster.setAI(false);

        sound(monster.getLocation(), "ENTITY_CREEPER_PRIMED", 1, 0.5f);
        sound(monster.getLocation(), "ITEM_FLINTANDSTEEL_USE", 1, 0.5f);

        monster.lookAt(player);

        api.getSchedulerProvider().createScheduler(new SchedulerTask() {
            @Override
            public void run() {
                if (monster.isDead()) {
                    stop();
                    return;
                }

                sound(monster.getLocation(), "ENTITY_GENERIC_EXPLODE", 0.3f, 2);
                sound(monster.getLocation(), "ITEM_FIRECHARGE_USE", 1, 0.6f);

                monster.setAI(true);


                final Item rocketItem = monster.getWorld().dropItem(
                        monster.getEyeLocation(),
                        new ItemStack(Material.FIREWORK_ROCKET, 1)
                );

                final List<Player> nearbyPlayers = rocketItem.getNearbyEntities(100, 100, 100)
                        .stream().filter(entity -> entity instanceof Player)
                        .map(entity -> (Player) entity).toList();

                pushTowards(rocketItem, player.getLocation().clone(), 1, 0);

                api.getSchedulerProvider().createScheduler(new SchedulerTask() {

                    int tick = 0;

                    @Override
                    public void run() {
                        if (tick > 100 || rocketItem.isDead() || rocketItem.isOnGround()) {
                            rocketItem.remove();
                            stop();

                            return;
                        }

                        for (final Player nearbyPlayer : nearbyPlayers) {
                            if (shouldIgnorePlayer(nearbyPlayer)) {
                                continue;
                            }

                            if (rocketItem.getLocation().distance(nearbyPlayer.getLocation()) < 1.5) {
                                sound(nearbyPlayer, nearbyPlayer.getLocation(), "ENTITY_DRAGON_FIREBALL_EXPLODE", 1, 0.5f);
                                sound(nearbyPlayer, nearbyPlayer.getLocation(), "ENTITY_FIREWORK_ROCKET_BLAST", 1, 0.5f);

                                particle(nearbyPlayer.getLocation(), Particle.EXPLOSION_LARGE, 1,
                                        1, 1, 1, 0.02
                                );

                                particle(nearbyPlayer.getLocation(), Particle.FLAME, 10,
                                        1, 1, 1, 0.1
                                );

                                particle(nearbyPlayer.getLocation(), Particle.SMOKE_LARGE, 10,
                                        1, 1, 1, 0.1
                                );

                                if (nearbyPlayer.isBlocking()) {
                                    shieldBlockedSound(nearbyPlayer);
                                    hurt(nearbyPlayer, monster, 0.2f, 0);
                                    pushBack(nearbyPlayer, rocketItem.getLocation(), 0.3, 0);
                                } else {
                                    pushBack(nearbyPlayer, rocketItem.getLocation(), 0.6, 0.25);
                                    hurt(nearbyPlayer, monster, 0.5f, EXPLOSIVE_PROJECTILE_DAMAGE);
                                }

                                rocketItem.remove();
                                stop();

                                return;
                            }

                        }

                        tick++;
                    }
                }, 1, 1);

            }
        }, 10);

        return true;
    }
}
