package de.rayzs.tacticalmonsters.api.attack;

import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.GameMode;
import org.bukkit.util.Vector;

import java.util.*;

public abstract class MonsterAttack<T extends Monster> {

    private final Random random;
    private final SchedulerTask scheduler;


    public MonsterAttack(final EntityType type, final TacticalMonstersAPI api, final Random random) {
        this.random = random;
        this.scheduler = api.getSchedulerProvider().createScheduler(new SchedulerTask() {

            @Override
            public void run() {
                final Map<Monster, Player> monsters = api.getMonsters(type);

                for (Map.Entry<Monster, Player> monsterPlayerEntry : monsters.entrySet()) {
                    final T monster = (T) monsterPlayerEntry.getKey();
                    final Player player = monsterPlayerEntry.getValue();

                    if (player.getGameMode() == GameMode.CREATIVE
                            || player.getGameMode() == GameMode.SPECTATOR
                            || player.isDead()) {
                        continue;
                    }

                    attack(monster, player);
                }
            }
        }, 20, 20 * 6);
    }

    protected boolean shouldDo(final int percentage) {
        return this.random.nextInt(100) < percentage;
    }

    /**
     * Artificially hurts the player to simulate damage.
     *
     * <pre>
     *     {@code
     *     // The entity being hurt
     *     LivingEntity victim = ...;
     *
     *     // The monster causing the damage
     *     Monster monster = ...;
     *
     *     // Apply 4 damage with a hurt animation strength of 0.5
     *     hurt(victim, monster, 0.5f, 4.0f);
     *     }
     * </pre>
     *
     * @param victim The entity being hurt.
     * @param monster The monster causing the damage.
     * @param hurtAnimation The strength of the hurt animation.
     * @param damage The amount of damage to apply.
     */
    protected void hurt(
            final LivingEntity victim,
            final Monster monster,
            final float hurtAnimation,
            final float damage
    ) {

        if (victim.isDead()) {
            return;
        }

        victim.damage(damage, monster);
        victim.setHealth(Math.max(0, victim.getHealth() - damage));

        if (victim instanceof Player player)
            player.sendHurtAnimation(hurtAnimation);
    }

    /**
     * Pushes the victim away from the pusher with the specified strength and vertical velocity.
     *
     * <pre>
     *     {@code
     *     // The entity being pushed
     *     LivingEntity victim = ...;
     *
     *     // The location from which the push originates
     *     Location pushingFrom = monster.getLocation();
     *
     *     // Push the victim away with a strength of 1.0 and an upward velocity of 0.5
     *     pushBack(victim, pushingFrom, 1.0, 0.5);
     *     }
     * </pre>
     *
     * @param victim The entity being pushed.
     * @param pushingFrom The location from which the push originates.
     * @param strength The strength of the push.
     * @param y The value the player gets pushed upwards.
     */
    protected void pushBack(
            final LivingEntity victim,
            final Location pushingFrom,
            final double strength,
            final double y
    ) {

        final Vector direction = victim.getLocation().toVector()
                .subtract(pushingFrom.toVector())
                .normalize()
                .multiply(strength);

        direction.setY(y);

        victim.setVelocity(direction);
    }

    /**
     * Code implementation on how the monster attacks the player.
     *
     * @param monster The monster attacking the player.
     * @param player The player being attacked.
     */
    public abstract void attack(final T monster, final Player player);

    /**
     * Stops the attack scheduler.
     */
    public void eliminate() {
        this.scheduler.stop();
    }
}