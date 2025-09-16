package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.content.attachment.VampireBloodData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BloodOrangeItem extends Item {
    public BloodOrangeItem(Properties properties) {
        super(properties);
    }
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        FoodProperties foodproperties = stack.getFoodProperties(livingEntity);
        if (foodproperties != null && VampireUtil.isVampire(livingEntity) && livingEntity.hasData(VampireData.type())){
            VampireBloodData data = livingEntity.getData(VampireData.type()).getBloodData();
            if (data.getBlood() < data.maxBlood()){
                data.drink(livingEntity, 2, 0.5F);
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
