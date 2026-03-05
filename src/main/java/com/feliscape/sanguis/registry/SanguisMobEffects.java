package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.effect.NoTickMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SanguisMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(
            Registries.MOB_EFFECT, Sanguis.MOD_ID
    );

    public static Holder<MobEffect> WEREBAT_CURSE = MOB_EFFECTS.register("werebat_curse",
            () -> new NoTickMobEffect(MobEffectCategory.NEUTRAL, 0x26211f));

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
