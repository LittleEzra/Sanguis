package com.feliscape.sanguis.content.event;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.entity.VampireEntity;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class GeneralEvents {
    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event){
        event.put(SanguisEntityTypes.VAMPIRE.get(), VampireEntity.createAttributes());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event){
        event.register(SanguisEntityTypes.VAMPIRE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                VampireEntity::checkVampireSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.AND);
    }
}
