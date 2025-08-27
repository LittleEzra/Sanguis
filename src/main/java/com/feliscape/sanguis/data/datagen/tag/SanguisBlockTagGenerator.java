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

public class SanguisBlockTagGenerator extends BlockTagsProvider {
    public SanguisBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Sanguis.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(SanguisTags.Blocks.VAMPIRE_REPELLENTS)
                .add(SanguisBlocks.GARLIC.get())
                .add(SanguisBlocks.GARLIC_STRING.get())
        ;
        this.tag(SanguisTags.Blocks.COFFINS).add(
                SanguisBlocks.WHITE_COFFIN.get(),
                SanguisBlocks.LIGHT_GRAY_COFFIN.get(),
                SanguisBlocks.GRAY_COFFIN.get(),
                SanguisBlocks.BLACK_COFFIN.get(),
                SanguisBlocks.BROWN_COFFIN.get(),
                SanguisBlocks.RED_COFFIN.get(),
                SanguisBlocks.ORANGE_COFFIN.get(),
                SanguisBlocks.YELLOW_COFFIN.get(),
                SanguisBlocks.LIME_COFFIN.get(),
                SanguisBlocks.GREEN_COFFIN.get(),
                SanguisBlocks.CYAN_COFFIN.get(),
                SanguisBlocks.LIGHT_BLUE_COFFIN.get(),
                SanguisBlocks.BLUE_COFFIN.get(),
                SanguisBlocks.PURPLE_COFFIN.get(),
                SanguisBlocks.MAGENTA_COFFIN.get(),
                SanguisBlocks.PINK_COFFIN.get()
        );

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(SanguisTags.Blocks.COFFINS)
                .add(SanguisBlocks.GARLIC.get())
                .add(SanguisBlocks.GARLIC_STRING.get())
        ;

        this.tag(BlockTags.SWORD_EFFICIENT)
                .add(SanguisBlocks.GARLIC.get())
                .add(SanguisBlocks.GARLIC_STRING.get())
        ;

        this.tag(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)
                .add(SanguisBlocks.GARLIC.get())
                .add(SanguisBlocks.GARLIC_STRING.get())
        ;

        this.tag(BlockTags.MAINTAINS_FARMLAND)
                .add(SanguisBlocks.GARLIC.get())
        ;
        this.tag(BlockTags.CROPS)
                .add(SanguisBlocks.GARLIC.get())
        ;
        this.tag(BlockTags.BEE_GROWABLES)
                .add(SanguisBlocks.GARLIC.get())
        ;
    }
}
