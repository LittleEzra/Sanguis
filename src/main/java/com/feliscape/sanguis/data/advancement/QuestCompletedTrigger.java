package com.feliscape.sanguis.data.advancement;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.QuestPredicate;
import com.feliscape.sanguis.content.quest.registry.QuestType;
import com.feliscape.sanguis.registry.SanguisCriteriaTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.function.Predicate;

public class QuestCompletedTrigger extends SimpleCriterionTrigger<QuestCompletedTrigger.TriggerInstance>{
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, HunterQuest quest) {
        super.trigger(player, triggerInstance -> triggerInstance.matches(quest));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, QuestPredicate quest) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<QuestCompletedTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(QuestCompletedTrigger.TriggerInstance::player),
                                QuestPredicate.CODEC.fieldOf("quest").forGetter(QuestCompletedTrigger.TriggerInstance::quest)
                        )
                        .apply(inst, QuestCompletedTrigger.TriggerInstance::new)
        );

        public static Criterion<QuestCompletedTrigger.TriggerInstance> questCompleted() {
            return questCompleted(QuestPredicate.Builder.quest().build());
        }
        public static Criterion<QuestCompletedTrigger.TriggerInstance> questCompleted(QuestType<?> type) {
            return questCompleted(QuestPredicate.Builder.quest().of(type).build());
        }
        public static Criterion<QuestCompletedTrigger.TriggerInstance> questCompleted(QuestType<?>... types) {
            return questCompleted(QuestPredicate.Builder.quest().of(types).build());
        }
        public static Criterion<QuestCompletedTrigger.TriggerInstance> questCompleted(QuestPredicate questPredicate) {
            return SanguisCriteriaTriggers.QUEST_COMPLETED.get()
                    .createCriterion(
                            new QuestCompletedTrigger.TriggerInstance(Optional.empty(), questPredicate)
                    );
        }

        public boolean matches(HunterQuest quest) {
            return this.quest().test(quest);
        }
    }
}
