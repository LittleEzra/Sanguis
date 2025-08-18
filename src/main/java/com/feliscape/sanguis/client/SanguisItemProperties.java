package com.feliscape.sanguis.client;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisDataComponents;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class SanguisItemProperties {
    public static final ResourceLocation BLOOD = Sanguis.location("blood");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ItemProperties.register(SanguisItems.BLOOD_BOTTLE.asItem(), BLOOD,
                ((stack, level, entity, seed) ->
                        (float) stack.getOrDefault(SanguisDataComponents.BLOOD, 0) / 6.0F));
    }
}
