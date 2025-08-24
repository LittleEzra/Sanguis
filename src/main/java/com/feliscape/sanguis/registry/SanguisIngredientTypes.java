package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.recipe.BloodBottleIngredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SanguisIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, Sanguis.MOD_ID);

    public static final Supplier<IngredientType<BloodBottleIngredient>> BLOOD_BOTTLE_INGREDIENT = INGREDIENT_TYPES.register(
            "blood_bottle", () -> new IngredientType<>(BloodBottleIngredient.CODEC, BloodBottleIngredient.STREAM_CODEC)
    );

    public static void register(IEventBus eventBus){
        INGREDIENT_TYPES.register(eventBus);
    }
}
