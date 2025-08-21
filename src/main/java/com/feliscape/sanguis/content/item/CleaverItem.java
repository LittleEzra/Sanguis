package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;

import java.util.List;

public class CleaverItem extends SwordItem {
    private final float bonusDamage;

    public CleaverItem(Tier tier, float bonusDamage, Properties properties) {
        super(tier, properties);
        this.bonusDamage = bonusDamage;
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        if (VampireUtil.isVampire(target)) {
            return damage * bonusDamage;
        }
        return 0.0F;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.sanguis.cleaver.tooltip", String.format("+%.0f", bonusDamage * 100.0F) + "%")
                .withStyle(ChatFormatting.BLUE));
    }
}
