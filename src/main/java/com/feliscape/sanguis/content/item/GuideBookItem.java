package com.feliscape.sanguis.content.item;

import com.feliscape.sanguis.content.menu.GuideBookMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GuideBookItem extends Item implements MenuProvider {
    public GuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (level.isClientSide()){
            return InteractionResultHolder.success(itemStack);
        }
        player.openMenu(this);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public Component getDisplayName() {
        return this.getDescription();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new GuideBookMenu(containerId, playerInventory);
    }
}
