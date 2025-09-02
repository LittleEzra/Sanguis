package com.feliscape.sanguis.content.menu;

import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.block.entity.QuestBoardBlockEntity;
import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisMenuTypes;
import com.feliscape.sanguis.util.HunterUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class QuestBoardMenu extends AbstractContainerMenu {
    public final QuestBoardBlockEntity blockEntity;
    private final Level level;

    public QuestBoardMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData){
        this(pContainerId, inv, getTileEntity(inv, extraData));
    }

    public QuestBoardMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(SanguisMenuTypes.QUEST_BOARD.get(), containerId);
        blockEntity = ((QuestBoardBlockEntity) entity);
        blockEntity.createQuests();
        this.level = inv.player.level();
    }

    private static QuestBoardBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof QuestBoardBlockEntity) {
            return (QuestBoardBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    public void chooseQuest(int index, Player player){
        if (!HunterUtil.isHunter(player)) return;

        HunterQuest quest = this.blockEntity.choose(index);
        if (quest != null){
            HunterData data = player.getData(HunterData.type());
            data.addQuest(quest);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, SanguisBlocks.QUEST_BOARD.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
