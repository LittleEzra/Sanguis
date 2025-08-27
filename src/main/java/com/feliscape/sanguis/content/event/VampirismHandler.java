package com.feliscape.sanguis.content.event;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.EntityBloodData;
import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.content.block.CoffinBlock;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class VampirismHandler {
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event){
        if (event.getEntity() instanceof LivingEntity living){
            if (living.hasData(VampireData.type())) {
                VampireData data = living.getData(VampireData.type());
                data.tick();
            }
            if (living.hasData(EntityBloodData.type())) {
                EntityBloodData data = living.getData(EntityBloodData.type());
                data.tick();
            }
            if (living.hasData(HunterData.type())) {
                HunterData data = living.getData(HunterData.type());
                data.tick();
            }
        }
    }
    @SubscribeEvent
    public static void entityUseItem(LivingEntityUseItemEvent.Start event){
        LivingEntity entity = event.getEntity();
        if (VampireUtil.isVampire(entity)){
            if (event.getItem().getFoodProperties(entity) != null){
                event.setCanceled(true);
                if (entity instanceof Player player){
                    player.displayClientMessage(Component.translatable("sanguis.cant_eat_message"), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingBreathe(LivingBreatheEvent event){
        if (!VampireUtil.isVampire(event.getEntity())) return;

        event.setCanBreathe(true);
    }
    @SubscribeEvent
    public static void canContinueSleeping(CanContinueSleepingEvent event){
        Entity entity = event.getEntity();
        if (!VampireUtil.isVampire(entity)) return;

        boolean canSleepAsVampire = VampireUtil.isDay(entity.level());

        if (event.mayContinueSleeping() && !canSleepAsVampire){
            event.setContinueSleeping(false);
        } else if (!event.mayContinueSleeping() && canSleepAsVampire && event.getProblem() == Player.BedSleepingProblem.NOT_POSSIBLE_NOW){
            event.setContinueSleeping(true);
        }
    }
    @SubscribeEvent
    public static void canPlayerSleep(CanPlayerSleepEvent event){
        Player player = event.getEntity();
        if (!VampireUtil.isVampire(player)) return;
        var problem = event.getVanillaProblem();

        boolean canSleepAsVampire = VampireUtil.isDay(player.level());

        if (canSleepAsVampire && problem == Player.BedSleepingProblem.NOT_POSSIBLE_NOW && event.getProblem() == problem){
            event.setProblem(null);
        } else if (!canSleepAsVampire){
            event.setProblem(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
        }
    }
    @SubscribeEvent
    public static void overrideSleepTime(SleepFinishedTimeEvent event){
        LevelAccessor level = event.getLevel();

        if(level instanceof ServerLevel serverLevel){
            List<ServerPlayer> players = serverLevel.players();

            boolean daySleeping = false;
            for (Player player : players){
                var sleepingPos = player.getSleepingPos();
                if (sleepingPos.isPresent() && player.isSleepingLongEnough()){
                    Block block = level.getBlockState(sleepingPos.get()).getBlock();
                    if (block instanceof CoffinBlock){
                        daySleeping = true;
                    }
                }
                if (daySleeping){
                    break;
                }
            }

            if (daySleeping){
                long i = serverLevel.getDayTime() + 24000L;
                long result = (i - i % 24000L) - 11001L;
                event.setTimeAddition(result);
            }
        }
    }
}
