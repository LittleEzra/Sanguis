package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, Sanguis.MOD_ID);

    public static final Supplier<SoundEvent> BAT_TRANSFORM = registerSoundEvent("entity.player_vampire.bat_transform");
    public static final Supplier<SoundEvent> VAMPIRE_TRANSFORM = registerSoundEvent("entity.player_vampire.vampire_transform");
    public static final Supplier<SoundEvent> VAMPIRE_DRINK = registerSoundEvent("entity.player_vampire.drink");
    public static final Supplier<SoundEvent> BLACKENED_COIN_USE = registerSoundEvent("item.blackened_coin.use");
    public static final Supplier<SoundEvent> INJECT = registerSoundEvent("item.inject");

    public static final Supplier<SoundEvent> COFFIN_CLOSE = registerSoundEvent("block.coffin.close");
    public static final Supplier<SoundEvent> COFFIN_OPEN = registerSoundEvent("block.coffin.open");

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }

    private static Supplier<SoundEvent> registerSoundEvent(String name)
    {
        return registerSoundEvent(name, 4f);
    }
    private static Supplier<SoundEvent> registerSoundEvent(String name, float pRange)
    {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(Sanguis.location(name), pRange));
    }
}
