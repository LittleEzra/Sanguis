package com.feliscape.sanguis.client.render.quest;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import javax.annotation.Nullable;
import java.util.Map;

public class QuestRenderDispatcher implements ResourceManagerReloadListener {
    private Map<QuestType<?>, QuestRenderer<?>> renderers = ImmutableMap.of();

    private final Minecraft minecraft;

    public QuestRenderDispatcher(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends HunterQuest> QuestRenderer<? super T> getRenderer(T requirement) {
        return (QuestRenderer<? super T>) this.renderers.get(requirement.type());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        renderers = QuestRenderers.createRenderers(this.minecraft);
    }

    public <Q extends HunterQuest> void render(
            Q quest,
            int x,
            int y, int mouseX, int mouseY, boolean canRenderTooltip,
            GuiGraphics guiGraphics
    ){
        QuestRenderer<? super Q> renderer = getRenderer(quest);

        if (renderer == null) return;
        renderer.renderActive(quest, guiGraphics, x, y, mouseX, mouseY, canRenderTooltip);
    }
    public <Q extends HunterQuest> void renderChoice(
            Q quest,
            int x,
            int y, int mouseX, int mouseY, boolean canRenderTooltip,
            GuiGraphics guiGraphics
    ){
        QuestRenderer<? super Q> renderer = getRenderer(quest);

        if (renderer == null) return;
        renderer.renderChoice(quest, guiGraphics, x, y, mouseX, mouseY, canRenderTooltip);
    }
}
