package com.feliscape.sanguis.data.datagen.ability;

import com.feliscape.sanguis.data.ability.VampireAbility;
import com.feliscape.sanguis.data.ability.VampireAbilityHolder;
import com.feliscape.sanguis.data.registry.SanguisDatapackRegistries;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class VampireAbilityProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final ExistingFileHelper existingFileHelper;

    public VampireAbilityProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        this.pathProvider = output.createRegistryElementsPathProvider(SanguisDatapackRegistries.VAMPIRE_ABILITY);
        this.registries = registries;
        this.existingFileHelper = existingFileHelper;
    }

    protected abstract void generateAbilities(Consumer<VampireAbilityHolder> consumer, HolderLookup.Provider lookupProvider, ExistingFileHelper existingFileHelper);

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose(provider -> {
            Set<ResourceLocation> locations = new HashSet<>();
            List<CompletableFuture<?>> saved = new ArrayList<>();
            generateAbilities((holder) -> {
                if (!locations.add(holder.id())) {
                    throw new IllegalStateException("Duplicate vampire ability " + holder.id());
                } else {
                    Path path = this.pathProvider.json(holder.id());
                    saved.add(DataProvider.saveStable(output, provider, VampireAbility.CODEC, holder.value(), path));
                }
            }, provider, existingFileHelper);
            return CompletableFuture.allOf(saved.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "Vampire Abilities";
    }
}
