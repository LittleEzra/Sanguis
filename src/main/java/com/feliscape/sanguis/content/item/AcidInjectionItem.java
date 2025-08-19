package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.util.HunterUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class AcidInjectionItem extends Item {
    public AcidInjectionItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown()){
            if (HunterUtil.isHunter(player)) {
                player.getData(HunterData.type()).removeGarlic();
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 2));
                ItemStack returnStack = SanguisItems.SYRINGE.toStack();
                return InteractionResultHolder.sidedSuccess(player.hasInfiniteMaterials() ? itemStack : returnStack, level.isClientSide());
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable(this.getDescriptionId(stack) + ".tooltip").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable(this.getDescriptionId(stack) + ".warning").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));
    }
}