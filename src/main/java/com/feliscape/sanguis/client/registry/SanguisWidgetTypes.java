package com.feliscape.sanguis.client.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.book.widget.ItemDisplayWidget;
import com.feliscape.sanguis.client.book.widget.CraftingRecipeWidget;
import com.feliscape.sanguis.client.book.widget.WidgetType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class SanguisWidgetTypes {
    public static final ResourceLocation ITEM_DISPLAY = Sanguis.location("item_display");
    public static final ResourceLocation CRAFTING_RECIPE = Sanguis.location("crafting_recipe");

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event){
        WidgetTypeRegistry.register(ITEM_DISPLAY, new WidgetType<>(ITEM_DISPLAY, ItemDisplayWidget.CODEC));
        WidgetTypeRegistry.register(CRAFTING_RECIPE, new WidgetType<>(CRAFTING_RECIPE, CraftingRecipeWidget.CODEC));
    }
}
