package com.feliscape.sanguis.content.ability;

import com.feliscape.sanguis.content.attachment.VampireData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class BatVampireAbility extends ActiveVampireAbility{

    public BatVampireAbility(int cost, int cooldown) {
        super(cost, cooldown);
    }

    @Override
    public void use(LivingEntity entity) {
        VampireData vampirism = entity.getData(VampireData.type());
        vampirism.toggleBatForm();
    }
}
