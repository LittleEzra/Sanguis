package com.feliscape.sanguis.client;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisServerConfig;
import com.feliscape.sanguis.client.hud.BloodLevelHudLayer;
import com.feliscape.sanguis.client.hud.DrainBarHudLayer;
import com.feliscape.sanguis.client.hud.VampireStatusHudLayer;
import com.feliscape.sanguis.client.render.entity.VampireRenderer;
import com.feliscape.sanguis.networking.payload.DrainBloodPayload;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisKeyMappings;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(SanguisEntityTypes.VAMPIRE.get(), VampireRenderer::new);
    }
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event)
    {
        event.registerBelow(VanillaGuiLayers.EXPERIENCE_LEVEL, VampireStatusHudLayer.LOCATION, new VampireStatusHudLayer());
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
    public static void clientTick(ClientTickEvent.Post event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        LocalPlayer player = minecraft.player;
        if (SanguisKeyMappings.DRAIN_BLOOD.get().consumeClick()){
            if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.ENTITY){
                EntityHitResult hitResult = ((EntityHitResult) minecraft.hitResult);
                Entity target = hitResult.getEntity();
                if (player.distanceTo(target) <= SanguisServerConfig.CONFIG.vampireDrainDistance.getAsDouble()){
                    PacketDistributor.sendToServer(new DrainBloodPayload(target.getId()));
                }
            }
        }
    }
}
