package com.feliscape.sanguis.data.datagen.tag;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.feliscape.sanguis.registry.SanguisBlocks.*;

public class SanguisBlockTagGenerator extends BlockTagsProvider {
    public SanguisBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Sanguis.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(SanguisTags.Blocks.VAMPIRE_REPELLENTS)
                .add(GARLIC.get())
                .add(WILD_GARLIC.get())
                .add(GARLIC_STRING.get())
        ;
        this.tag(SanguisTags.Blocks.COFFINS).add(
                WHITE_COFFIN.get(),
                LIGHT_GRAY_COFFIN.get(),
                GRAY_COFFIN.get(),
                BLACK_COFFIN.get(),
                BROWN_COFFIN.get(),
                RED_COFFIN.get(),
                ORANGE_COFFIN.get(),
                YELLOW_COFFIN.get(),
                LIME_COFFIN.get(),
                GREEN_COFFIN.get(),
                CYAN_COFFIN.get(),
                LIGHT_BLUE_COFFIN.get(),
                BLUE_COFFIN.get(),
                PURPLE_COFFIN.get(),
                MAGENTA_COFFIN.get(),
                PINK_COFFIN.get()
        );

        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(BLOOD_ORANGE_LEAVES.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(SanguisTags.Blocks.COFFINS)
                .add(QUEST_BOARD.get())
                .add(GARLIC.get())
                .add(WILD_GARLIC.get())
                .add(GARLIC_STRING.get())
                .add(BLOOD_ORANGE_VINE.get())
        ;

        this.tag(BlockTags.SWORD_EFFICIENT)
                .add(GARLIC.get())
                .add(WILD_GARLIC.get())
                .add(GARLIC_STRING.get())
                .add(BLOOD_ORANGE_VINE.get())
        ;

        this.tag(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)
                .add(GARLIC.get())
                .add(WILD_GARLIC.get())
                .add(GARLIC_STRING.get())
        ;

        this.tag(BlockTags.MAINTAINS_FARMLAND)
                .add(GARLIC.get())
                .add(WILD_GARLIC.get())
        ;
        this.tag(BlockTags.CROPS)
                .add(GARLIC.get())
        ;
        this.tag(BlockTags.BEE_GROWABLES)
                .add(GARLIC.get())
        ;
    }
}
