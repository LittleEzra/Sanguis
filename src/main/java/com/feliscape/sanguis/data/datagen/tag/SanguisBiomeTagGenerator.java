package com.feliscape.sanguis.data.datagen.tag;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.damage.SanguisDamageTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SanguisBiomeTagGenerator extends BiomeTagsProvider {

    public SanguisBiomeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Sanguis.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(SanguisTags.Biomes.SPAWNS_VAMPIRES)
                .addTags(BiomeTags.IS_FOREST,
                        Tags.Biomes.IS_DESERT,
                        BiomeTags.IS_TAIGA,
                        BiomeTags.IS_BADLANDS,
                        BiomeTags.IS_MOUNTAIN,
                        BiomeTags.IS_JUNGLE,
                        BiomeTags.IS_SAVANNA
                        )
                .add(
                    Biomes.PLAINS,
                    Biomes.SUNFLOWER_PLAINS,
                    Biomes.SNOWY_BEACH,
                    Biomes.SNOWY_PLAINS,
                    Biomes.SNOWY_TAIGA
                );
        this.tag(SanguisTags.Biomes.HAS_HUNTER_CAMPS)
                .addTags(BiomeTags.IS_FOREST,
                        BiomeTags.IS_TAIGA,
                        Tags.Biomes.IS_DESERT,
                        BiomeTags.IS_MOUNTAIN,
                        BiomeTags.IS_SAVANNA
                        )
                .add(
                        Biomes.WINDSWEPT_FOREST,
                        Biomes.WINDSWEPT_HILLS,
                        Biomes.WINDSWEPT_GRAVELLY_HILLS,
                        Biomes.PLAINS,
                        Biomes.SUNFLOWER_PLAINS,
                        Biomes.SNOWY_PLAINS,
                        Biomes.SNOWY_TAIGA
                );
        this.tag(SanguisTags.Biomes.HAS_WILD_GARLIC)
                .add(
                    Biomes.WINDSWEPT_FOREST,
                    Biomes.WINDSWEPT_HILLS,
                    Biomes.WINDSWEPT_GRAVELLY_HILLS,
                    Biomes.PLAINS,
                    Biomes.MEADOW,
                    Biomes.SUNFLOWER_PLAINS
                );
    }
}
