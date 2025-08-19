package com.feliscape.sanguis.data.datagen;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.damage.SanguisDamageTypes;
import com.feliscape.sanguis.data.worldgen.registry.SanguisBiomeModifiers;
import com.feliscape.sanguis.data.worldgen.registry.SanguisStructureSets;
import com.feliscape.sanguis.data.worldgen.registry.SanguisStructures;
import com.feliscape.sanguis.data.worldgen.registry.SanguisTemplatePools;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SanguisGeneratedEntries extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, SanguisDamageTypes::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, SanguisBiomeModifiers::bootstrap)
            .add(Registries.TEMPLATE_POOL, SanguisTemplatePools::bootstrap)
            .add(Registries.STRUCTURE, SanguisStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, SanguisStructureSets::bootstrap)
            ;

    public SanguisGeneratedEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Sanguis.MOD_ID));
    }
}
