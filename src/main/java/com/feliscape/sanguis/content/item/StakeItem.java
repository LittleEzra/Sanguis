package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.registry.SanguisTags;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class StakeItem extends SwordItem {
    public StakeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        if (target instanceof LivingEntity livingTarget &&
                VampireUtil.isVampire(livingTarget) && !target.getType().is(SanguisTags.EntityTypes.STAKE_IMMUNE) &&
                livingTarget.getHealth() / livingTarget.getMaxHealth() <= 0.25F){
            return livingTarget.getMaxHealth() * 10.0F;
        }
        return super.getAttackDamageBonus(target, damage, damageSource);
    }
}
