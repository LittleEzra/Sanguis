package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.map.EntityBloodContent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class SanguisDataMapTypes {
    public static final DataMapType<EntityType<?>, EntityBloodContent> ENTITY_BLOOD = AdvancedDataMapType.builder(
            Sanguis.location("entity_blood"),
            Registries.ENTITY_TYPE,
            EntityBloodContent.CODEC
    ).synced(EntityBloodContent.CODEC, true).build();

    @SubscribeEvent
    private static void registerDataMapTypes(RegisterDataMapTypesEvent event){
        event.register(ENTITY_BLOOD);
    }
}
