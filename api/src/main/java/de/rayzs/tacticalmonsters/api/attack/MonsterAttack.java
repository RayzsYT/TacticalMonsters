package de.rayzs.tacticalmonsters.api.attack;

import de.rayzs.tacticalmonsters.api.configuration.Config;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.*;

public abstract class MonsterAttack<T extends Monster> {

    protected final TacticalMonstersAPI api;
    private final Random random;

    private final SchedulerTask scheduler;
    private final Config config;

    private boolean enabled;


    public MonsterAttack(final EntityType type, final TacticalMonstersAPI api, final Random random) {
        this.config = api.getConfigProvider().getOrCreate("attacks", this.getClass().getSimpleName());

        this.api = api;
        this.random = random;

        this.enabled = get("enabled", true);

        if (!enabled) {
            this.scheduler = null;
            return;
        }

        this.scheduler = api.getSchedulerProvider().createScheduler(new SchedulerTask() {

            @Override
            public void run() {
                final Map<Monster, Player> monsters = api.getMonsters(type);

                for (Map.Entry<Monster, Player> monsterPlayerEntry : monsters.entrySet()) {
                    final T monster = (T) monsterPlayerEntry.getKey();
                    final Player player = monsterPlayerEntry.getValue();

                    if (shouldIgnorePlayer(player)) {
                        continue;
                    }

                    attack(monster, player);
                }
            }
        }, 20, 20 * 6);
    }

    /**
     * Is this attack instance enabled.
     * @return Indicates whether this attack instance is enabled or not.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Should something be done based on the given percentage chance.
     *
     * @param percentage Chance percentage (0-100).
     * @return Returns 'true' if the action should be performed, returns 'false' otherwise.
     */
    protected boolean shouldDo(final int percentage) {

        if (percentage <= 0) {
            return false;
        }

        if (percentage >= 100) {
            return true;
        }

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
     * Determines if the player should be ignored or not.
     *
     * @param player The player to check.
     * @return Returns 'true' if the player should be ignored, returns 'false' otherwise.
     */
    protected boolean shouldIgnorePlayer(final Player player) {
        return player.getGameMode() == GameMode.CREATIVE
                || player.getGameMode() == GameMode.SPECTATOR
                || player.isDead();
    }

    /**
     * Plays a sound at the given location.
     *
     * @param location Location to play the sound at.
     * @param soundName Name of the sound to play.
     * @param volume Volume of the sound. (1.0 - 10.0)
     * @param pitch Pitch of the sound. (0.0 - 2.0)
     */
    protected void sound(
            final Location location,
            final String soundName,
            final float volume,
            final float pitch
    ) {

        final World world = location.getWorld();
        if (world == null) {
            return;
        }

        try {
            final Sound sound = Sound.valueOf(soundName);
            world.playSound(location, sound, volume, pitch);
        } catch (Exception exception) {
            api.getLogger().warning("Invalid sound name! (" + soundName + " -> " + this.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Plays a sound at the given location.
     *
     * @param player Player to hear the sound.
     * @param location Location to play the sound at.
     * @param soundName Name of the sound to play.
     * @param volume Volume of the sound. (1.0 - 10.0)
     * @param pitch Pitch of the sound. (0.0 - 2.0)
     */
    protected void sound(
            final Player player,
            final Location location,
            final String soundName,
            final float volume,
            final float pitch
    ) {

        final World world = location.getWorld();
        if (world == null) {
            return;
        }

        try {
            final Sound sound = Sound.valueOf(soundName);
            player.playSound(location, sound, volume, pitch);
        } catch (Exception exception) {
            api.getLogger().warning("Invalid sound name! (" + soundName + " -> " + this.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Plays the shield blocked sound for the given player.
     *
     * @param player Player to hear the sound.
     */
    protected void shieldBlockedSound(final Player player) {
        sound(player, player.getLocation(), "ITEM_SHIELD_BLOCK", 1.0f, 1.0f);
    }

    /**
     * Spawns particles at the given location.
     *
     * @param location Location to spawn the particles at.
     * @param particle Particle type to spawn.
     * @param amount Amount of particles to spawn.
     * @param x X distribution over time.
     * @param y Y distribution over time.
     * @param z Z distribution over time.
     * @param speed Speed of the particles.
     */
    protected void particle(
            final Location location,
            final Particle particle,
            final int amount,
            final double x,
            final double y,
            final double z,
            final double speed
    ) {

        final World world = location.getWorld();
        if (world == null) {
            return;
        }

        world.spawnParticle(
                particle,
                location,
                amount,
                x,
                y,
                z,
                speed
        );
    }

    /**
     * Spawns colored dust particles at the given location.
     *
     * @param location Location to spawn the particles at.
     * @param color Color of the dust particles.
     * @param size Size of the dust particles. (1 - 50)
     * @param amount Amount of particles to spawn.
     * @param x X distribution over time.
     * @param y Y distribution over time.
     * @param z Z distribution over time.
     * @param speed Speed of the particles.
     */
    protected void particle(
            final Location location,
            final Color color,
            final float size,
            final int amount,
            final double x,
            final double y,
            final double z,
            final double speed
    ) {

        final World world = location.getWorld();
        if (world == null) {
            return;
        }

        world.spawnParticle(
                Particle.DUST,
                location,
                amount,
                x,
                y,
                z,
                speed,
                new Particle.DustOptions(color, size)
        );
    }

    /**
     * Gets a value if it exists.
     * Returns assigned default value and sets it in the config otherwise.
     *
     * @param target Target key.
     * @param value Default value.
     * @return The existing or default value.
     */
    protected <S> S get(String target, S value) {
        return this.config.getOrSet(target, value);
    }

    /**
     * Gets a value if it exists.
     * Returns assigned default value and sets it in the config otherwise.
     *
     * @param path Path to the section.
     * @param target Target key.
     * @param value Default value.
     * @return The existing or default value.
     */
    protected <S> S get(String path, String target, S value) {
        return this.config.getOrSet(path, target, value);
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
        enabled = false;

        if (this.scheduler != null) {
            this.scheduler.stop();
        }
    }
}