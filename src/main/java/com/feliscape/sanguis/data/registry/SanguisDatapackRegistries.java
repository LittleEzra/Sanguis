package com.feliscape.sanguis.data.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.ability.VampireAbility;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class SanguisDatapackRegistries {
    public static final ResourceKey<Registry<VampireAbility>> VAMPIRE_ABILITY = ResourceKey.createRegistryKey(Sanguis.location("vampire_ability"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event){
        event.dataPackRegistry(VAMPIRE_ABILITY, VampireAbility.CODEC, VampireAbility.CODEC);
    }
}
