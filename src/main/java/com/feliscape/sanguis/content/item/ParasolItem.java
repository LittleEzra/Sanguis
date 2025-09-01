package com.feliscape.sanguis.content.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ParasolItem extends Item {
    public ParasolItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!isSelected && !(entity instanceof Player player && player.getOffhandItem() == stack))
            return;

        if (entity instanceof LivingEntity living){
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 8, 2, false, false));
        }
    }
}
