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

public class VampireCureTrigger extends SimpleCriterionTrigger<VampireCureTrigger.TriggerInstance>{

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> triggerInstance.matches());
    }

    public static record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<VampireCureTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(VampireCureTrigger.TriggerInstance::player)
                        )
                        .apply(inst, VampireCureTrigger.TriggerInstance::new)
        );

        public static Criterion<VampireCureTrigger.TriggerInstance> any() {
            return SanguisCriteriaTriggers.VAMPIRE_CURE.get()
                    .createCriterion(
                            new VampireCureTrigger.TriggerInstance(Optional.empty())
                    );
        }

        public boolean matches() {
            return true;
        }
    }
}
