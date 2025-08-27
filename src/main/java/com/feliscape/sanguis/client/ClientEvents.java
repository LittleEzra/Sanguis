package com.feliscape.sanguis.client;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisClient;
import com.feliscape.sanguis.SanguisServerConfig;
import com.feliscape.sanguis.client.hud.BloodLevelHudLayer;
import com.feliscape.sanguis.client.hud.DrainBarHudLayer;
import com.feliscape.sanguis.client.hud.StatusHudLayer;
import com.feliscape.sanguis.client.render.entity.GoldenQuarrelRenderer;
import com.feliscape.sanguis.client.render.entity.VampireHunterRenderer;
import com.feliscape.sanguis.client.render.entity.VampireRenderer;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.networking.payload.BatTransformationPayload;
import com.feliscape.sanguis.networking.payload.DrainBloodPayload;
import com.feliscape.sanguis.networking.payload.OpenActiveQuestsPayload;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisKeyMappings;
import com.feliscape.sanguis.util.HunterUtil;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void renderPlayer(RenderLivingEvent.Pre<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> event)
    {
        LivingEntity entity = event.getEntity();
        float partialTick = event.getPartialTick();
        if (VampireUtil.isBat(entity)){
            var bat = entity.getData(VampireData.type()).getBat();
            if (bat == null) {
                return;
            }

            event.setCanceled(true);
            float f = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
            Minecraft.getInstance().getEntityRenderDispatcher().render(bat,
                    0.0D, 0.0D, 0.0D, f, partialTick,
                    event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        }
    }
    @SubscribeEvent
    public static void renderHand(RenderHandEvent event){
        if (!VampireUtil.isBat(Minecraft.getInstance().player)) return;

        if (event.getItemStack().isEmpty()){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event)
    {
        new SanguisClient.ReloadListener(event);
    }
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(SanguisEntityTypes.VAMPIRE.get(), VampireRenderer::new);
        event.registerEntityRenderer(SanguisEntityTypes.VAMPIRE_HUNTER.get(), VampireHunterRenderer::new);

        event.registerEntityRenderer(SanguisEntityTypes.GOLDEN_QUARREL.get(), GoldenQuarrelRenderer::new);
    }
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event)
    {
        event.registerBelow(VanillaGuiLayers.EXPERIENCE_LEVEL, StatusHudLayer.LOCATION, new StatusHudLayer());
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, BloodLevelHudLayer.LOCATION, new BloodLevelHudLayer());
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, DrainBarHudLayer.LOCATION, new DrainBarHudLayer());
    }
    @SubscribeEvent
    public static void skipRenderingFoodBar(RenderGuiLayerEvent.Pre event)
    {
        if (VampireUtil.isVampire(Minecraft.getInstance().player)) {
            if (event.getName() == VanillaGuiLayers.FOOD_LEVEL){
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void beforeClientTick(ClientTickEvent.Pre event){
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        LocalPlayer player = minecraft.player;

        if (player.hasData(VampireData.type())) {
            VampireData data = player.getData(VampireData.type());
            data.clientTick();
        }
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        LocalPlayer player = minecraft.player;

        boolean triggered = false;
        while (SanguisKeyMappings.DRAIN_BLOOD.get().consumeClick()){
            if (triggered) continue; // We want to clear the entire click "stack" but only trigger the drinking once per tick

            if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.ENTITY){
                EntityHitResult hitResult = ((EntityHitResult) minecraft.hitResult);
                Entity target = hitResult.getEntity();
                if (player.distanceTo(target) <= SanguisServerConfig.CONFIG.vampireDrainDistance.getAsDouble()){
                    PacketDistributor.sendToServer(new DrainBloodPayload(target.getId()));
                    triggered = true;
                }
            }
        }
        while (SanguisKeyMappings.BAT_TRANSFORMATION.get().consumeClick()){
            PacketDistributor.sendToServer(new BatTransformationPayload());
        }

        while(SanguisKeyMappings.OPEN_ACTIVE_QUESTS.get().consumeClick()){
            if (HunterUtil.isHunter(player))
                PacketDistributor.sendToServer(new OpenActiveQuestsPayload());
        }
    }
}
