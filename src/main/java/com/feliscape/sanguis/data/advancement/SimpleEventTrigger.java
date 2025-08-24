package com.feliscape.sanguis.data.advancement;

import com.feliscape.sanguis.registry.SanguisCriteriaTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class SimpleEventTrigger extends SimpleCriterionTrigger<SimpleEventTrigger.TriggerInstance> {

    @Override
    public Codec<SimpleEventTrigger.TriggerInstance> codec() {
        return SimpleEventTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> triggerInstance.matches());
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<SimpleEventTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(SimpleEventTrigger.TriggerInstance::player)
                        )
                        .apply(inst, SimpleEventTrigger.TriggerInstance::new)
        );

        public static Criterion<SimpleEventTrigger.TriggerInstance> vampireCure() {
            return SanguisCriteriaTriggers.VAMPIRE_CURE.get()
                    .createCriterion(
                            new SimpleEventTrigger.TriggerInstance(Optional.empty())
                    );
        }
        public static Criterion<SimpleEventTrigger.TriggerInstance> vampireTransformation() {
            return SanguisCriteriaTriggers.VAMPIRE_TRANSFORMATION.get()
                    .createCriterion(
                            new SimpleEventTrigger.TriggerInstance(Optional.empty())
                    );
        }
        public static Criterion<SimpleEventTrigger.TriggerInstance> hunterInject() {
            return SanguisCriteriaTriggers.HUNTER_INJECT.get()
                    .createCriterion(
                            new SimpleEventTrigger.TriggerInstance(Optional.empty())
                    );
        }
        public static Criterion<SimpleEventTrigger.TriggerInstance> batTransform() {
            return SanguisCriteriaTriggers.TRANSFORM_TO_BAT.get()
                    .createCriterion(
                            new SimpleEventTrigger.TriggerInstance(Optional.empty())
                    );
        }

        public boolean matches() {
            return true;
        }
    }
}
