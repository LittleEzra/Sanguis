package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.ability.BatVampireAbility;
import com.feliscape.sanguis.content.ability.InvisibilityVampireAbility;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.content.ritual.BloodRitual;
import com.feliscape.sanguis.content.ritual.InvisibilityRitual;
import com.feliscape.sanguis.content.ritual.LevelUpRitual;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisRituals {
    public static final DeferredRegister<BloodRitual> RITUALS =
            DeferredRegister.create(SanguisRegistries.RITUALS, Sanguis.MOD_ID);

    public static final Supplier<InvisibilityRitual> INVISIBILITY = RITUALS.register(
            "invisibility", () -> new InvisibilityRitual());

    public static final Supplier<LevelUpRitual> LEVEL_UP_1 = RITUALS.register(
            "level_up_1", () -> new LevelUpRitual(Items.GOLD_INGOT, 0));
    public static final Supplier<LevelUpRitual> LEVEL_UP_2 = RITUALS.register(
            "level_up_2", () -> new LevelUpRitual(SanguisItems.BAT_WING.get(), 1));
    public static final Supplier<LevelUpRitual> LEVEL_UP_3 = RITUALS.register(
            "level_up_3", () -> new LevelUpRitual(SanguisItems.VAMPIRE_BLOOD.get(), 2));
    public static final Supplier<LevelUpRitual> LEVEL_UP_4 = RITUALS.register(
            "level_up_4", () -> new LevelUpRitual(SanguisItems.BLACKENED_COIN.get(), 3));

    public static void register(IEventBus eventBus){
        RITUALS.register(eventBus);
    }
}
