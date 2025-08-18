package com.feliscape.sanguis.data.datagen.advancement;

import com.feliscape.sanguis.data.advancement.CustomAdvancement;
import com.feliscape.sanguis.data.advancement.VampireCureTrigger;
import com.feliscape.sanguis.data.advancement.VampireTransformationTrigger;
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

            TURN_TO_VAMPIRE = create("sanguis/turn_to_vampire",b -> b
                    .icon(SanguisItems.BLOODY_FANG)
                    .after(ROOT)
                    .type(CustomAdvancement.Type.CHALLENGE)
                    .trigger(VampireTransformationTrigger.TriggerInstance.any())),
            CURE_VAMPIRE = create("sanguis/cure_vampire",b -> b
                    .icon(SanguisItems.GARLIC_SOLUTION)
                    .after(TURN_TO_VAMPIRE)
                    .type(CustomAdvancement.Type.CHALLENGE)
                    .trigger(VampireCureTrigger.TriggerInstance.any())),

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
