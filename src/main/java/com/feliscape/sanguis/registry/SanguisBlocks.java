package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
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
    public static final DeferredBlock<WildGarlicBlock> WILD_GARLIC = registerBlockWithItem("wild_garlic",
            p -> new WildGarlicBlock(p
                    .mapColor(MapColor.PLANT)
                    .noCollission()
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

    public static final DeferredBlock<BloodOrangeVineBlock> BLOOD_ORANGE_VINE = BLOCKS.registerBlock("blood_orange_vine",
            p -> new BloodOrangeVineBlock(p
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.CAVE_VINES)
                    .pushReaction(PushReaction.DESTROY)
            ));
    public static final DeferredBlock<BloodOrangeLeavesBlock> BLOOD_ORANGE_LEAVES = BLOCKS.registerBlock("blood_orange_leaves",
            p -> new BloodOrangeLeavesBlock(p
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .noOcclusion()
                    .strength(0.2F)
                    .randomTicks()
                    .isValidSpawn(Blocks::ocelotOrParrot)
                    .isSuffocating(SanguisBlocks::never)
                    .isViewBlocking(SanguisBlocks::never)
                    .sound(SoundType.CAVE_VINES)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(SanguisBlocks::never)
            ));


    public static final DeferredBlock<CoffinBlock> WHITE_COFFIN = registerCoffin(DyeColor.WHITE);
    public static final DeferredBlock<CoffinBlock> LIGHT_GRAY_COFFIN = registerCoffin(DyeColor.LIGHT_GRAY);
    public static final DeferredBlock<CoffinBlock> GRAY_COFFIN = registerCoffin(DyeColor.GRAY);
    public static final DeferredBlock<CoffinBlock> BLACK_COFFIN = registerCoffin(DyeColor.BLACK);
    public static final DeferredBlock<CoffinBlock> BROWN_COFFIN = registerCoffin(DyeColor.BROWN);
    public static final DeferredBlock<CoffinBlock> RED_COFFIN = registerCoffin(DyeColor.RED);
    public static final DeferredBlock<CoffinBlock> ORANGE_COFFIN = registerCoffin(DyeColor.ORANGE);
    public static final DeferredBlock<CoffinBlock> YELLOW_COFFIN = registerCoffin(DyeColor.YELLOW);
    public static final DeferredBlock<CoffinBlock> LIME_COFFIN = registerCoffin(DyeColor.LIME);
    public static final DeferredBlock<CoffinBlock> GREEN_COFFIN = registerCoffin(DyeColor.GREEN);
    public static final DeferredBlock<CoffinBlock> CYAN_COFFIN = registerCoffin(DyeColor.CYAN);
    public static final DeferredBlock<CoffinBlock> LIGHT_BLUE_COFFIN = registerCoffin(DyeColor.LIGHT_BLUE);
    public static final DeferredBlock<CoffinBlock> BLUE_COFFIN = registerCoffin(DyeColor.BLUE);
    public static final DeferredBlock<CoffinBlock> PURPLE_COFFIN = registerCoffin(DyeColor.PURPLE);
    public static final DeferredBlock<CoffinBlock> MAGENTA_COFFIN = registerCoffin(DyeColor.MAGENTA);
    public static final DeferredBlock<CoffinBlock> PINK_COFFIN = registerCoffin(DyeColor.PINK);

    private static DeferredBlock<CoffinBlock> registerCoffin(DyeColor color){
        return registerBlockWithItem(color.getName() + "_coffin", p -> coffin(p, color));
    }

    private static CoffinBlock coffin(BlockBehaviour.Properties properties, DyeColor color) {
        return new CoffinBlock(color, properties
                .mapColor(color.getMapColor())
                .sound(SoundType.WOOD)
                .strength(0.2F)
                .noOcclusion()
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
        );
    }
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
