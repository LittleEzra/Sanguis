package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class GarlicItem extends ItemNameBlockItem {
    public GarlicItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        FoodProperties foodproperties = stack.getFoodProperties(livingEntity);
        if (foodproperties != null && VampireUtil.isVampire(livingEntity)){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 400, 2));
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
