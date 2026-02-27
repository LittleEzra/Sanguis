package com.feliscape.sanguis.content.ability;

import net.minecraft.world.entity.LivingEntity;

public abstract class ActiveVampireAbility extends VampireAbility{
    protected final int cooldown;
    public ActiveVampireAbility(int cost, int cooldown) {
        super(cost);
        this.cooldown = cooldown;
    }

    public abstract void use(LivingEntity entity);
}
