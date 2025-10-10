package com.feliscape.sanguis.compat;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.hud.BloodLevelHudLayer;
import com.feliscape.sanguis.client.hud.DrainBarHudLayer;
import com.feliscape.sanguis.client.hud.StatusHudLayer;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public class AppleSkinHandler {
    private static final ResourceLocation SATURATION_OVERLAY =
            ResourceLocation.fromNamespaceAndPath("appleskin", "saturation_level");
    private static final ResourceLocation HUNGER_OVERLAY =
            ResourceLocation.fromNamespaceAndPath("appleskin", "hunger_restored");
    private static final ResourceLocation EXHAUSTION_OVERLAY =
            ResourceLocation.fromNamespaceAndPath("appleskin", "exhaustion_level");

    @EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
    public static class Client{

        @SubscribeEvent
        public static void skipRenderingAppleSkinOverlays(RenderGuiLayerEvent.Pre event)
        {
            if (VampireUtil.isVampire(Minecraft.getInstance().player)) {
                if (event.getName().equals(SATURATION_OVERLAY) ||
                        event.getName().equals(HUNGER_OVERLAY) ||
                        event.getName().equals(EXHAUSTION_OVERLAY)){
                    event.setCanceled(true);
                }
            }
        }
    }
}
