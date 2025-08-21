package com.feliscape.sanguis.content.block;

import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.registry.SanguisQuestTypes;
import com.feliscape.sanguis.util.HunterUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class QuestBoardBlock extends Block {
    public QuestBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel && HunterUtil.isHunter(player)){
            player.getData(HunterData.type()).addQuest(SanguisQuestTypes.FETCH_ITEMS.get().factory().create(serverLevel));
        }
        return InteractionResult.PASS;
    }
}
