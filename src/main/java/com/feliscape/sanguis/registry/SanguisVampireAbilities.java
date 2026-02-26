package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisVampireAbilities {
    public static final DeferredRegister<VampireAbility> VAMPIRE_ABILITIES =
            DeferredRegister.create(SanguisRegistries.VAMPIRE_ABILITIES, Sanguis.MOD_ID);

    public static final Supplier<VampireAbility> BAT_TRANSFORMATION = VAMPIRE_ABILITIES.register(
            "bat_transformation", () -> new VampireAbility(2));

    public static void register(IEventBus eventBus){
        VAMPIRE_ABILITIES.register(eventBus);
    }
}
