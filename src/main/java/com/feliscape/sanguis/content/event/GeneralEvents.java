package com.feliscape.sanguis.content.event;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.entity.living.VampireEntity;
import com.feliscape.sanguis.content.entity.living.VampireHunter;
import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.KillMobsQuest;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.util.HunterUtil;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class GeneralEvents {
    @SubscribeEvent
    public static void entityKilled(LivingDeathEvent event){
        var cause = event.getSource().getEntity();

        if (cause instanceof Player player && HunterUtil.isHunter(player)){
            var data = player.getData(HunterData.type()).getQuests();
            for (HunterQuest quest : data.getActiveQuests()){
                if (quest instanceof KillMobsQuest killMobsQuest){
                    killMobsQuest.recordKill(event.getEntity().getType());
                }
            }
        }
    }

    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event){
        event.put(SanguisEntityTypes.VAMPIRE.get(), VampireEntity.createAttributes());
        event.put(SanguisEntityTypes.VAMPIRE_HUNTER.get(), VampireHunter.createAttributes().build());
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
        if (!entity.isAlive()) return;

        if (entity instanceof WanderingTrader mob){
            mob.goalSelector.addGoal(1, new AvoidEntityGoal<>(mob, LivingEntity.class, 8.0F, 0.5, 0.5,
                    VampireUtil::isVampire));
        }
        else if (entity instanceof IronGolem mob){
            mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, LivingEntity.class, 5, false, false, VampireUtil::isVampire));
        }
    }
}
