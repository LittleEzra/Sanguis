package com.feliscape.sanguis.content.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class InvisibilityVampireAbility extends ActiveVampireAbility{

    public InvisibilityVampireAbility(int cost, int cooldown) {
        super(cost, cooldown);
    }

    @Override
    public void use(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 30 * 20));
    }
}
