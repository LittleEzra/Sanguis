package com.feliscape.sanguis.data.datagen.tag;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.damage.SanguisDamageTypes;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SanguisDamageTypeTagGenerator extends DamageTypeTagsProvider {

    public SanguisDamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Sanguis.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.NO_IMPACT)
                .add(SanguisDamageTypes.DRAINING);
        this.tag(DamageTypeTags.NO_KNOCKBACK)
                .add(SanguisDamageTypes.DRAINING);
    }
}
