package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.block.GarlicCropBlock;
import com.feliscape.sanguis.content.block.GarlicStringBlock;
import com.feliscape.sanguis.content.block.QuestBoardBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class SanguisBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Sanguis.MOD_ID);

    public static final DeferredBlock<QuestBoardBlock> QUEST_BOARD = registerBlockWithItem("quest_board",
            p -> new QuestBoardBlock(p
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0F)
                    .sound(SoundType.WOOD)
            ));
    public static final DeferredBlock<GarlicCropBlock> GARLIC = BLOCKS.registerBlock("garlic",
            p -> new GarlicCropBlock(p
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<GarlicStringBlock> GARLIC_STRING = registerBlockWithItem("garlic_string",
            p -> new GarlicStringBlock(p
                    .mapColor(MapColor.TERRACOTTA_MAGENTA)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY)));

    private static LeavesBlock leaves(BlockBehaviour.Properties properties, SoundType soundType) {
        return new LeavesBlock(
                properties
                        .mapColor(MapColor.PLANT)
                        .strength(0.2F)
                        .randomTicks()
                        .sound(soundType)
                        .noOcclusion()
                        .isValidSpawn(Blocks::ocelotOrParrot)
                        .isSuffocating(SanguisBlocks::never)
                        .isViewBlocking(SanguisBlocks::never)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor(SanguisBlocks::never)
        );
    }
    private static LeavesBlock leaves(BlockBehaviour.Properties properties, SoundType soundType, MapColor mapColor) {
        return new LeavesBlock(
                properties
                        .mapColor(mapColor)
                        .strength(0.2F)
                        .randomTicks()
                        .sound(soundType)
                        .noOcclusion()
                        .isValidSpawn(Blocks::ocelotOrParrot)
                        .isSuffocating(SanguisBlocks::never)
                        .isViewBlocking(SanguisBlocks::never)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor(SanguisBlocks::never)
        );
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    private static ButtonBlock woodenButton(BlockSetType type) {
        return new ButtonBlock(type, 30, BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY));
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block)
    {
        return SanguisItems.ITEMS.registerItem(name, p -> new BlockItem(block.get(), p));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
