package com.feliscape.sanguis.content.event;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.entity.VampireEntity;
import com.feliscape.sanguis.content.entity.ai.RunAwayFromBlockGoal;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
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


    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof WanderingTrader mob && entity.isAlive()){
            mob.goalSelector.addGoal(1, new AvoidEntityGoal<>(mob, LivingEntity.class, 8.0F, 0.5, 0.5,
                    living -> VampireUtil.isVampire(living)));
        }
    }
}
