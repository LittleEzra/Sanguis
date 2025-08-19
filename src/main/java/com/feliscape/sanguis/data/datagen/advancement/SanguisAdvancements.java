package com.feliscape.sanguis.data.datagen.advancement;

import com.feliscape.sanguis.data.advancement.CustomAdvancement;
import com.feliscape.sanguis.data.advancement.SimpleEventTrigger;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SanguisAdvancements implements AdvancementProvider.AdvancementGenerator {
    public static final List<CustomAdvancement> ENTRIES = new ArrayList<>();

    public static final CustomAdvancement
            ROOT = create("sanguis/root",b -> b
            .icon(SanguisItems.BLOODY_FANG)
            .type(CustomAdvancement.Type.ROOT)
            .awardedAutomatically()),

            VAMPIRE_TRANSFORMATION = create("sanguis/vampire/transformation", b -> b
                    .icon(SanguisItems.BLOODY_FANG)
                    .after(ROOT)
                    .type(CustomAdvancement.Type.GOAL)
                    .announceToChat(false)
                    .trigger(SimpleEventTrigger.TriggerInstance.vampireTransformation())),
            VAMPIRE_CURE = create("sanguis/vampire/cure", b -> b
                    .icon(SanguisItems.GARLIC_INJECTION)
                    .after(VAMPIRE_TRANSFORMATION)
                    .type(CustomAdvancement.Type.GOAL)
                    .announceToChat(false)
                    .trigger(SimpleEventTrigger.TriggerInstance.vampireCure())),
            HUNTER_INJECT = create("sanguis/hunter/inject",b -> b
                    .icon(SanguisItems.GARLIC)
                    .after(ROOT)
                    .type(CustomAdvancement.Type.GOAL)
                    .announceToChat(false)
                    .trigger(SimpleEventTrigger.TriggerInstance.hunterInject())),

    END = null;

    private static CustomAdvancement create(String id, UnaryOperator<CustomAdvancement.Builder> b) {
        return new CustomAdvancement(id, b);
    }

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        for (CustomAdvancement advancement : ENTRIES){
            advancement.save(provider, consumer, existingFileHelper);
        }
    }

    private static AdvancementHolder placeholder(String id){
        return AdvancementSubProvider.createPlaceholder(id);
    }
}
