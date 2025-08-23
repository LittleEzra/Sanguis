package com.feliscape.sanguis.content.menu;

import com.feliscape.sanguis.content.block.entity.QuestBoardBlockEntity;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.registry.SanguisMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class GuideBookMenu extends AbstractContainerMenu {
    public GuideBookMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData){
        this(pContainerId, inv);
    }

    public GuideBookMenu(int containerId, Inventory inv) {
        super(SanguisMenuTypes.GUIDE_BOOK.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(SanguisItems.DAEMONOLOGIE) || player.getOffhandItem().is(SanguisItems.DAEMONOLOGIE);
    }
}
