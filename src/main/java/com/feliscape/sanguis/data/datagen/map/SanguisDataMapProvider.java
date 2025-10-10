package com.feliscape.sanguis.data.datagen.map;

import com.feliscape.sanguis.data.map.EntityBloodContent;
import com.feliscape.sanguis.registry.SanguisDataMapTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class SanguisDataMapProvider extends DataMapProvider {
    public SanguisDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(SanguisDataMapTypes.ENTITY_BLOOD)
                .add(EntityType.VILLAGER.builtInRegistryHolder(),
                        new EntityBloodContent(6, 1.0f), false)
                .add(EntityType.WANDERING_TRADER.builtInRegistryHolder(),
                        new EntityBloodContent(6, 1.0f), false)
                .add(EntityType.PIGLIN.builtInRegistryHolder(),
                        new EntityBloodContent(5, 0.85f), false)
                .add(EntityType.PIGLIN_BRUTE.builtInRegistryHolder(),
                        new EntityBloodContent(5, 0.85f), false)
                .add(EntityType.SHEEP.builtInRegistryHolder(),
                        new EntityBloodContent(3, 0.6f), false)
                .add(EntityType.GOAT.builtInRegistryHolder(),
                        new EntityBloodContent(3, 0.7f), false)
                .add(EntityType.PIG.builtInRegistryHolder(),
                        new EntityBloodContent(3, 0.75f), false)
                .add(EntityType.COW.builtInRegistryHolder(),
                        new EntityBloodContent(4, 0.6f), false)
                .add(EntityType.MOOSHROOM.builtInRegistryHolder(),
                        new EntityBloodContent(4, 0.6f), false)
                .add(EntityType.CHICKEN.builtInRegistryHolder(),
                        new EntityBloodContent(1, 0.2f), false)
                .add(EntityType.HORSE.builtInRegistryHolder(),
                        new EntityBloodContent(4, 0.5f), false)
                .add(EntityType.DONKEY.builtInRegistryHolder(),
                        new EntityBloodContent(4, 0.5f), false)
                .add(EntityType.MULE.builtInRegistryHolder(),
                        new EntityBloodContent(4, 0.5f), false)
        ;
    }
}
