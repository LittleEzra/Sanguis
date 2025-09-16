package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.content.attachment.EntityBloodData;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SyringeItem extends Item {
    public SyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (stack.isEmpty()) return InteractionResult.PASS;

        if (EntityBloodData.canHaveBlood(interactionTarget)) {
            var data = interactionTarget.getData(EntityBloodData.type());
            int drain = data.drain();
            if (drain == 0) return InteractionResult.FAIL;

            stack.consume(1, player);
            ItemStack bloodSyringeStack = SanguisItems.BLOOD_SYRINGE.toStack();
            if (stack.getCount() == 1 && !player.hasInfiniteMaterials()){
                player.setItemInHand(usedHand, bloodSyringeStack);
            } else{
                if (!player.addItem(bloodSyringeStack)){
                    player.drop(bloodSyringeStack, true);
                }
            }
            return InteractionResult.CONSUME;
        }
        return interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
