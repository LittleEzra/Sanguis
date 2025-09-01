package com.feliscape.sanguis.data.datagen.tag;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class SanguisItemTagGenerator extends ItemTagsProvider {
    public SanguisItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, Sanguis.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(SanguisTags.Items.GUIDE_BOOK_MATERIALS)
                .add(SanguisItems.GARLIC.get())
                .add(SanguisItems.BLOODY_FANG.get())
        ;

        this.tag(SanguisTags.Items.HELD_SUN_PROTECTION)
                .add(SanguisItems.PARASOL.get());

        this.tag(SanguisTags.Items.STAKES)
                .add(SanguisItems.WOODEN_STAKE.get())
                .add(SanguisItems.REINFORCED_STAKE.get());

        this.tag(SanguisTags.Items.CLEAVERS)
                .add(SanguisItems.STEEL_CLEAVER.get())
                .add(SanguisItems.DIAMOND_CLEAVER.get());

        this.tag(SanguisTags.Items.QUARRELS)
                .add(SanguisItems.GOLDEN_QUARREL.get());

        this.tag(Tags.Items.TOOLS)
                .addTag(SanguisTags.Items.STAKES)
                .addTag(SanguisTags.Items.CLEAVERS);

        this.tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(SanguisTags.Items.CLEAVERS);

        this.tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(SanguisTags.Items.CLEAVERS)
                .addTag(SanguisTags.Items.STAKES)
        ;
    }
}
