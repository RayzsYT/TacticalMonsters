package de.rayzs.tacticalmonsters.attacks;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;

import java.util.Random;

public class WitherSkeletonAttack extends MonsterAttack<WitherSkeleton> {

    public WitherSkeletonAttack(TacticalMonstersAPI api, Random random) {
        super(EntityType.WITHER_SKELETON, api, random);

        System.out.println("WitherSkeletonAttack initialized");
    }

    @Override
    public void attack(WitherSkeleton monster, Player player) {

    }
}
