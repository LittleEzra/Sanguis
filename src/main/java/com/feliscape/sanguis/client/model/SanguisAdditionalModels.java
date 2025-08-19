package com.feliscape.sanguis.client.model;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class SanguisAdditionalModels {
    public static final ModelResourceLocation CROSSBOW_GOLDEN_QUARREL =
            ModelResourceLocation.standalone(Sanguis.location("item/crossbow_golden_quarrel"));

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event)
    {
        event.register(SanguisAdditionalModels.CROSSBOW_GOLDEN_QUARREL);
    }
}
