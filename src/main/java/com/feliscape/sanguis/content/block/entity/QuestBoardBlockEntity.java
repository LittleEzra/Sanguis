package com.feliscape.sanguis.content.block.entity;

import com.feliscape.sanguis.content.menu.QuestBoardMenu;
import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.registry.SanguisBlockEntityTypes;
import com.feliscape.sanguis.util.QuestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class QuestBoardBlockEntity extends BlockEntity implements MenuProvider {
    private final ArrayList<HunterQuest> quests = new ArrayList<>();

    private int lastUpdate = -1;
    private boolean chosen = false;

    public QuestBoardBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    public QuestBoardBlockEntity(BlockPos pos, BlockState blockState) {
        super(SanguisBlockEntityTypes.QUEST_BOARD.get(), pos, blockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag listTag = new ListTag();
        for (HunterQuest quest : quests){
            listTag.add(HunterQuest.TYPED_CODEC.encodeStart(NbtOps.INSTANCE, quest).getOrThrow());
        }
        tag.put("quests", listTag);
        tag.putInt("lastUpdate", lastUpdate);
        tag.putBoolean("hasChosen", chosen);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        quests.clear();
        if (tag.contains("quests")) {
            ListTag list = tag.getList("quests", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                quests.add(HunterQuest.TYPED_CODEC.decode(NbtOps.INSTANCE, list.get(i)).getOrThrow().getFirst());
            }
        }
        this.lastUpdate = tag.getInt("lastUpdate");
        this.chosen = tag.getBoolean("hasChosen");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, QuestBoardBlockEntity questBoard){
        if (questBoard.quests.isEmpty()){
            questBoard.update();
            return;
        }

        var day = Mth.floor((double) level.getDayTime() / 24000.0D);
        if (day > questBoard.lastUpdate){
            questBoard.lastUpdate = day;
            questBoard.update();
        }
    }

    private void update() {
        if (!(this.level instanceof ServerLevel serverLevel)) return;
        quests.clear();
        for (int i = 0; i < 3; i++){
            quests.add(QuestUtil.createRandom(serverLevel));
        }
        this.chosen = false;
        this.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public ArrayList<HunterQuest> getQuests() {
        return quests;
    }

    public boolean hasChosen() {
        return chosen;
    }

    @Nullable
    public HunterQuest choose(int index) {
        var quest = getQuest(index);
        if (quest == null) return null;

        this.chosen = true;
        this.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        return quest;
    }
    @Nullable
    public HunterQuest getQuest(int index) {
        if (quests.isEmpty()) createQuests();

        if (index < 0 || index >= quests.size())
            return null;
        return quests.get(index);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.sanguis.quest_board");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new QuestBoardMenu(containerId, playerInventory, this);
    }

    public void createQuests() {
        if (quests.isEmpty()){
            update();
        }
    }
}
