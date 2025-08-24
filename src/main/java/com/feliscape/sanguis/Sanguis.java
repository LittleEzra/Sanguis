package com.feliscape.sanguis;

import com.feliscape.sanguis.content.component.namegen.EnUsQuestNameGenerator;
import com.feliscape.sanguis.registry.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Sanguis.MOD_ID)
public class Sanguis {
    public static final String MOD_ID = "sanguis";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public static final EnUsQuestNameGenerator NAME_GENERATOR = new EnUsQuestNameGenerator();

    public Sanguis(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        SanguisItems.register(modEventBus);
        SanguisCreativeModeTabs.register(modEventBus);
        SanguisDataComponents.register(modEventBus);

        SanguisBlocks.register(modEventBus);
        SanguisBlockEntityTypes.register(modEventBus);

        SanguisEntityTypes.register(modEventBus);
        SanguisDataAttachmentTypes.register(modEventBus);

        SanguisParticles.register(modEventBus);

        SanguisCriteriaTriggers.register(modEventBus);
        SanguisQuestTypes.register(modEventBus);

        SanguisMenuTypes.register(modEventBus);

        SanguisSoundEvents.register(modEventBus);
        SanguisIngredientTypes.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.CLIENT, SanguisClientConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, SanguisServerConfig.SPEC);
    }

    public static ResourceLocation location(String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
    public static String stringLocation(String path){
        return MOD_ID + ":" + path;
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
