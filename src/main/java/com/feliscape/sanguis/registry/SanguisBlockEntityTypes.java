package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.block.entity.QuestBoardBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Sanguis.MOD_ID);

    public static final Supplier<BlockEntityType<QuestBoardBlockEntity>> QUEST_BOARD = BLOCK_ENTITY_TYPES.register(
            "quest_board",
            () -> BlockEntityType.Builder.of(
                    QuestBoardBlockEntity::new,
                    SanguisBlocks.QUEST_BOARD.get()
            ).build(null)
    );

    public static void register(IEventBus eventBus){
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
