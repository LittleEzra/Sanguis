package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VampireFangItem extends Item {
    public VampireFangItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown()){
            if (VampireUtil.canInfect(player)) {
                player.getData(VampireData.type()).infect(5 * 60 * 20); // 5 Minutes
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
            }
        }
        return super.use(level, player, usedHand);
    }
}
