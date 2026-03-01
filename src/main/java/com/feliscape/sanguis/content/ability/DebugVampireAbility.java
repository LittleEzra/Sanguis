package com.feliscape.sanguis.content.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class DebugVampireAbility extends ActiveVampireAbility{

    public DebugVampireAbility(int cost, int cooldown) {
        super(cost, cooldown);
    }

    @Override
    public void use(LivingEntity entity) {
        entity.sendSystemMessage(Component.translatable(this.getTranslationId()));
    }
}
