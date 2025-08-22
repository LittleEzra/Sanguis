package com.feliscape.sanguis;

import com.feliscape.sanguis.client.render.quest.ItemQuestRenderer;
import com.feliscape.sanguis.client.render.quest.KillMobsQuestRenderer;
import com.feliscape.sanguis.client.render.quest.QuestRenderDispatcher;
import com.feliscape.sanguis.client.render.quest.QuestRenderers;
import com.feliscape.sanguis.content.component.namegen.EnUsQuestNameGenerator;
import com.feliscape.sanguis.registry.SanguisQuestTypes;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Sanguis.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class SanguisClient {

    public SanguisClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        QuestRenderers.register(SanguisQuestTypes.FETCH_ITEMS.get(), ItemQuestRenderer::new);
        QuestRenderers.register(SanguisQuestTypes.KILL_MOBS.get(), KillMobsQuestRenderer::new);
    }

    private static ReloadListener reloadListeners;

    public static ReloadListener reloadListeners(){
        return reloadListeners;
    }

    public static class ReloadListener {
        private final QuestRenderDispatcher questRenderDispatcher;

        public ReloadListener(RegisterClientReloadListenersEvent event){
            reloadListeners = this;

            Minecraft minecraft = Minecraft.getInstance();
            questRenderDispatcher = new QuestRenderDispatcher(Minecraft.getInstance());
            event.registerReloadListener(questRenderDispatcher);
        }

        public QuestRenderDispatcher getQuestRenderDispatcher() {
            return questRenderDispatcher;
        }
    }
}
