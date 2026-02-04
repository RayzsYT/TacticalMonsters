package de.rayzs.tacticalmonsters.attacks;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class SkeletonAttack extends MonsterAttack<Skeleton> {

    public SkeletonAttack(final TacticalMonstersAPI api, final Random random) {
        super(EntityType.SKELETON, api, random);
    }

    @Override
    public void attack(Skeleton monster, org.bukkit.entity.Player player) {

        if (throwBoneAttack(monster, player)) {
            return;
        }

        if (swordAttack(monster, player)) {
            return;
        }
    }


    private final boolean SWORD_ENABLED = get("sword.enabled", true);
    private final int SWORD_CHANCE = get("sword.chance", 40);

    private boolean swordAttack(final Skeleton monster, final Player player) {
        if (!SWORD_ENABLED || !shouldDo(SWORD_CHANCE)) {
            return false;
        }

        monster.getEquipment().setItemInMainHand(
                new ItemStack(Material.WOODEN_SWORD, 1)
        );

        sound(monster.getLocation(), "ENTITY_SKELETON_AMBIENT", 1, 2);
        sound(monster.getLocation(), "ITEM_ARMOR_EQUIP_LEATHER", 1, 2);

        final AttributeInstance attribute = monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        attribute.setBaseValue(attribute.getBaseValue() + 0.4);
        monster.setGlowing(true);

        api.getSchedulerProvider().createScheduler(new SchedulerTask() {

            @Override
            public void run() {
                attribute.setBaseValue(attribute.getDefaultValue());
                monster.setGlowing(false);
            }
        }, 20 * 3);

        return true;
    }


    private final boolean BONE_THROW_ENABLED = get("bone_throw.enabled", true);
    private final int BONE_THROW_CHANCE = get("bone_throw.chance", 30);
    private final int BONE_THROW_DAMAGE = get("bone_throw.damage", 2);

    private boolean throwBoneAttack(final Skeleton monster, final Player player) {
        if (!BONE_THROW_ENABLED || !shouldDo(BONE_THROW_CHANCE)) {
            return false;
        }

        final Item boneItem = monster.getWorld().dropItem(monster.getEyeLocation(),
                new ItemStack(org.bukkit.Material.BONE, 1)
        );

        final Location direction = player.getLocation().clone();
        final List<Player> nearbyPlayers = boneItem.getNearbyEntities(100, 100, 100)
                .stream().filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity).toList();

        pushTowards(boneItem, direction, 1, 0);

        api.getSchedulerProvider().createScheduler(new SchedulerTask() {

            int tick = 0;

            @Override
            public void run() {
                if (tick > 100 || boneItem.isDead() || boneItem.isOnGround()) {
                    stop();
                    return;
                }

                for (final Player nearbyPlayer : nearbyPlayers) {
                    if (shouldIgnorePlayer(nearbyPlayer)) {
                        continue;
                    }

                    if (boneItem.getLocation().distance(nearbyPlayer.getLocation()) < 1.5) {

                        sound(nearbyPlayer, nearbyPlayer.getLocation(), "ENTITY_SKELETON_HURT", 1, 0.5f);

                        if (nearbyPlayer.isBlocking()) {
                            shieldBlockedSound(nearbyPlayer);
                            hurt(nearbyPlayer, monster, 0.2f, 0);
                            pushBack(nearbyPlayer, boneItem.getLocation(), 0.3, 0);
                        } else {
                            pushBack(nearbyPlayer, boneItem.getLocation(), 0.6, 0.25);
                            hurt(nearbyPlayer, monster, 0.5f, BONE_THROW_DAMAGE);
                        }

                        boneItem.remove();
                        stop();

                        return;
                    }

                }

                tick++;
            }
        }, 1, 1);

        return true;
    }
}
