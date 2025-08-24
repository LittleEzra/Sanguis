package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.data.damage.SanguisDamageTypes;
import com.feliscape.sanguis.registry.SanguisDataComponents;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class BloodBottleItem extends Item {
    public BloodBottleItem(Properties properties) {
        super(properties);
    }

    public static ItemStack getWithFill(int fill) {
        ItemStack itemStack = SanguisItems.BLOOD_BOTTLE.toStack();
        itemStack.set(SanguisDataComponents.BLOOD, fill);
        return itemStack;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
        }

        if (entityLiving.hasData(VampireData.type()) && VampireUtil.isVampire(entityLiving)) {
            var data = entityLiving.getData(VampireData.type());
            data.getBloodData().drink(entityLiving, 1, 0.6F);
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (player == null || !player.hasInfiniteMaterials()) {
            int blood = stack.getOrDefault(SanguisDataComponents.BLOOD, 0);
            if (blood > 0){
                stack.set(SanguisDataComponents.BLOOD, blood - 1);
            }
            return stack;
        }

        entityLiving.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getOrDefault(SanguisDataComponents.BLOOD, 0) <= 0){
            return InteractionResultHolder.pass(itemStack);
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int blood = stack.getOrDefault(SanguisDataComponents.BLOOD, 0);
        int maxBlood = stack.getOrDefault(SanguisDataComponents.MAX_BLOOD, 0);
        tooltipComponents.add(Component.literal(blood + "/" + maxBlood).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        int blood = itemStack.getOrDefault(SanguisDataComponents.BLOOD, 0);
        if (blood <= 0 ) return ItemStack.EMPTY;

        ItemStack copy = itemStack.copy();
        copy.set(SanguisDataComponents.BLOOD, blood -1);
        return copy;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.getOrDefault(SanguisDataComponents.BLOOD, 0) > 0;
    }
}
