package com.feliscape.sanguis.client;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisDataComponents;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class SanguisItemProperties {
    public static final ResourceLocation BLOOD = Sanguis.location("blood");
    public static final ResourceLocation GOLDEN_QUARREL = Sanguis.location("golden_quarrel");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            ItemProperties.register(SanguisItems.BLOOD_BOTTLE.asItem(), BLOOD,
                    ((stack, level, entity, seed) ->
                            (float) stack.getOrDefault(SanguisDataComponents.BLOOD, 0) / 6.0F));
            /*ItemProperties.register(Items.CROSSBOW, GOLDEN_QUARREL,
                    ((stack, level, entity, seed) -> {
                        ChargedProjectiles projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
                        return projectiles != null && projectiles.contains(SanguisItems.GOLDEN_QUARREL.get()) ? 1.0F : 0.0F;
                    }));*/
        });
    }
}
