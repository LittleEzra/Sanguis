package com.feliscape.sanguis.data.datagen.tag;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SanguisEntityTypeTagGenerator extends EntityTypeTagsProvider {

    public SanguisEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Sanguis.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(SanguisTags.EntityTypes.VAMPIRIC)
                .add(SanguisEntityTypes.VAMPIRE.get());

        this.tag(SanguisTags.EntityTypes.BLOOD_DRINKABLE)
                .add(
                        EntityType.VILLAGER,
                        EntityType.SHEEP,
                        EntityType.COW,
                        EntityType.CHICKEN
                );

        this.tag(SanguisTags.EntityTypes.INFECTABLE)
                .add(EntityType.PLAYER);
    }
}
