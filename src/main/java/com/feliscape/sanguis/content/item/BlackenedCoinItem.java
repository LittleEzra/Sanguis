package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlackenedCoinItem extends Item {
    public BlackenedCoinItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (VampireUtil.isVampire(player)){
            var data = player.getData(VampireData.type());
            if (!data.canUpgrade()) return super.use(level, player, usedHand);

            itemStack.consume(1, player);
            data.upgradeTier();
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return super.use(level, player, usedHand);
    }
}
