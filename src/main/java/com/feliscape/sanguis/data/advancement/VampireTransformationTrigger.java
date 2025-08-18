package com.feliscape.sanguis.data.advancement;

import com.feliscape.sanguis.registry.SanguisCriteriaTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class VampireTransformationTrigger extends SimpleCriterionTrigger<VampireTransformationTrigger.TriggerInstance>{

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> triggerInstance.matches());
    }

    public static record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<VampireTransformationTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(VampireTransformationTrigger.TriggerInstance::player)
                        )
                        .apply(inst, VampireTransformationTrigger.TriggerInstance::new)
        );

        public static Criterion<VampireTransformationTrigger.TriggerInstance> any() {
            return SanguisCriteriaTriggers.VAMPIRE_TRANSFORMATION.get()
                    .createCriterion(
                            new VampireTransformationTrigger.TriggerInstance(Optional.empty())
                    );
        }

        public boolean matches() {
            return true;
        }
    }
}
