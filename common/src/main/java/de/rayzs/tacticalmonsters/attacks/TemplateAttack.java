package de.rayzs.tacticalmonsters.attacks;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.attack.MonsterAttack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

import java.util.Random;

public class TemplateAttack extends MonsterAttack<Zombie> {

    public TemplateAttack(final TacticalMonstersAPI api, final Random random) {
        super(EntityType.ZOMBIE, api, random);
    }

    @Override
    public void attack(Zombie monster, org.bukkit.entity.Player player) {

    }



}
