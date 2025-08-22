package com.feliscape.sanguis.content.block;

import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.block.entity.QuestBoardBlockEntity;
import com.feliscape.sanguis.content.menu.QuestBoardMenu;
import com.feliscape.sanguis.registry.SanguisBlockEntityTypes;
import com.feliscape.sanguis.registry.SanguisQuestTypes;
import com.feliscape.sanguis.util.HunterUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class QuestBoardBlock extends BaseEntityBlock {
    private static final MapCodec<QuestBoardBlock> CODEC = simpleCodec(QuestBoardBlock::new);

    public QuestBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends QuestBoardBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof QuestBoardBlockEntity) {
                player.openMenu((QuestBoardBlockEntity)blockentity, pos);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new QuestBoardBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;
        return createTickerHelper(blockEntityType, SanguisBlockEntityTypes.QUEST_BOARD.get(), QuestBoardBlockEntity::tick);
    }
}
