package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.advancement.VampireCureTrigger;
import com.feliscape.sanguis.data.advancement.VampireTransformationTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, Sanguis.MOD_ID);

    public static final Supplier<VampireTransformationTrigger> VAMPIRE_TRANSFORMATION = TRIGGER_TYPES.register("vampire_transformation",
            VampireTransformationTrigger::new);
    public static final Supplier<VampireCureTrigger> VAMPIRE_CURE = TRIGGER_TYPES.register("vampire_cure",
            VampireCureTrigger::new);

    public static void register(IEventBus eventBus){
        TRIGGER_TYPES.register(eventBus);
    }
}
