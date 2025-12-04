package com.feliscape.sanguis.data.datagen;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.datagen.ability.SanguisVampireAbilities;
import com.feliscape.sanguis.data.datagen.advancement.SanguisAdvancementProvider;
import com.feliscape.sanguis.data.datagen.language.SanguisEnUsProvider;
import com.feliscape.sanguis.data.datagen.loot.SanguisGlobalLootModifierProvider;
import com.feliscape.sanguis.data.datagen.loot.SanguisLootTableProvider;
import com.feliscape.sanguis.data.datagen.map.SanguisDataMapProvider;
import com.feliscape.sanguis.data.datagen.model.SanguisBlockModelProvider;
import com.feliscape.sanguis.data.datagen.model.SanguisItemModelProvider;
import com.feliscape.sanguis.data.datagen.recipe.SanguisRecipeProvider;
import com.feliscape.sanguis.data.datagen.tag.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        SanguisGeneratedEntries generatedEntries = new SanguisGeneratedEntries(packOutput, lookupProvider);
        lookupProvider = generatedEntries.getRegistryProvider();
        generator.addProvider(true, generatedEntries);

        generator.addProvider(true, new SanguisRecipeProvider(packOutput, lookupProvider));

        var blockTags = new SanguisBlockTagGenerator(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new SanguisItemTagGenerator(packOutput, lookupProvider, existingFileHelper, blockTags.contentsGetter()));
        generator.addProvider(true, new SanguisEntityTypeTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new SanguisBiomeTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new SanguisDamageTypeTagGenerator(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(true, new SanguisDataMapProvider(packOutput, lookupProvider));

        //generator.addProvider(event.includeServer(), new SanguisAdvancementProvider(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeServer(), new SanguisLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new SanguisGlobalLootModifierProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeServer(), new SanguisAdvancementProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new SanguisVampireAbilities(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(true, new SanguisBlockModelProvider(packOutput, existingFileHelper));
        generator.addProvider(true, new SanguisItemModelProvider(packOutput, existingFileHelper));

        generator.addProvider(true, new SanguisEnUsProvider(packOutput));
    }
}
