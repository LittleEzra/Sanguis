package com.feliscape.sanguis.content.entity.projectile;

import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class GoldenQuarrel extends AbstractArrow {
    public GoldenQuarrel(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public GoldenQuarrel(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(SanguisEntityTypes.GOLDEN_QUARREL.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public GoldenQuarrel(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(SanguisEntityTypes.GOLDEN_QUARREL.get(), owner, level, pickupItemStack, firedFromWeapon);
    }


    @Override
    protected void onHitEntity(EntityHitResult result) {
        double baseDamage = this.getBaseDamage();
        if (VampireUtil.isVampire(result.getEntity())){
            this.setBaseDamage(baseDamage * 2.5D);
        }
        super.onHitEntity(result);
        this.setBaseDamage(baseDamage);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return SanguisItems.GOLDEN_QUARREL.toStack(1);
    }
}
