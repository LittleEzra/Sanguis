package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BloodSoakedCoinItem extends Item {
    public BloodSoakedCoinItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        if (itemEntity.level() instanceof ServerLevel serverLevel) {
            ItemStack itemStack = itemEntity.getItem();
            ItemStack transformed = new ItemStack(SanguisItems.BLACKENED_COIN.get(), itemStack.getCount());
            ItemEntity transformedEntity = new ItemEntity(itemEntity.level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), transformed);
            double theta = itemEntity.level().random.nextDouble() * Math.TAU;
            double x = Math.cos(theta);
            double y = itemEntity.level().random.nextDouble() * 0.2D + 0.1D;
            double z = Math.sin(theta);
            transformedEntity.setDeltaMovement(x, y, z);
            serverLevel.addFreshEntity(transformedEntity);
        }
    }
}
