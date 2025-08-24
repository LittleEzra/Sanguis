package com.feliscape.sanguis.data.datagen.advancement;

import com.feliscape.sanguis.data.advancement.CustomAdvancement;
import com.feliscape.sanguis.data.advancement.QuestCompletedTrigger;
import com.feliscape.sanguis.data.advancement.SimpleEventTrigger;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SanguisAdvancements implements AdvancementProvider.AdvancementGenerator {
    public static final List<CustomAdvancement> ENTRIES = new ArrayList<>();

    public static final CustomAdvancement
            ROOT = create("sanguis/root",b -> b
            .icon(SanguisItems.DAEMONOLOGIE)
            .type(CustomAdvancement.Type.ROOT)
            .awardedAutomatically()),

            VAMPIRE_TRANSFORMATION = create("sanguis/vampire/transformation", b -> b
                    .icon(SanguisItems.BLOODY_FANG)
                    .after(ROOT)
                    .type(CustomAdvancement.Type.STANDARD)
                    .trigger(SimpleEventTrigger.TriggerInstance.vampireTransformation())),
            BAT_TRANSFORM = create("sanguis/vampire/bat_transform", b -> b
                    .icon(SanguisItems.BAT_WING)
                    .after(VAMPIRE_TRANSFORMATION)
                    .type(CustomAdvancement.Type.STANDARD)
                    .trigger(SimpleEventTrigger.TriggerInstance.batTransform())),
            VAMPIRE_CURE = create("sanguis/vampire/cure", b -> b
                    .icon(SanguisItems.GARLIC_INJECTION)
                    .after(VAMPIRE_TRANSFORMATION)
                    .type(CustomAdvancement.Type.STANDARD)
                    .trigger(SimpleEventTrigger.TriggerInstance.vampireCure())),

            HUNTER_INJECT = create("sanguis/hunter/inject",b -> b
                    .icon(SanguisItems.GARLIC)
                    .after(ROOT)
                    .type(CustomAdvancement.Type.STANDARD)
                    .trigger(SimpleEventTrigger.TriggerInstance.hunterInject())),
            QUEST_COMPLETE = create("quest_complete",b -> b
                    .icon(SanguisBlocks.QUEST_BOARD)
                    .after(HUNTER_INJECT)
                    .type(CustomAdvancement.Type.STANDARD)
                    .trigger(QuestCompletedTrigger.TriggerInstance.questCompleted())),

            BIBLE_HIT = create("bible_hit",b -> b
                    .icon(SanguisItems.DAEMONOLOGIE)
                    .after(ROOT)
                    .type(CustomAdvancement.Type.STANDARD)
                    .hidden(true)
                    .trigger(hitWith(SanguisItems.DAEMONOLOGIE, EntityPredicate.Builder.entity().of(SanguisTags.EntityTypes.VAMPIRIC).build()))),

    END = null;

    protected static Criterion<?> hitWith(ItemLike itemLike, EntityPredicate entity){
        return PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(
                DamagePredicate.Builder.damageInstance().sourceEntity(
                        EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(itemLike)))
                                .build()),
                Optional.of(
                        entity
                )

        );
    }

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
