package com.feliscape.sanguis.client.screen.ability;

import com.feliscape.sanguis.content.ability.VampireAbilityNode;
import com.google.common.collect.Lists;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;

import javax.annotation.Nullable;
import java.util.List;

public class AbilityWidget {
    private final VampireAbilityNode abilityNode;
    @Nullable
    private AbilityWidget parent;
    private final List<AbilityWidget> children = Lists.newArrayList();

    public AbilityWidget(Minecraft minecraft, VampireAbilityNode abilityNode, DisplayInfo display) {
        this.abilityNode = abilityNode;
    }

    @Nullable
    private AdvancementWidget getFirstVisibleParent(VampireAbilityNode advancement) {
        do {
            advancement = advancement.parent();
        } while (advancement != null);

        return advancement != null ? this.tab.getWidget(advancement.holder()) : null;
    }

    public void attachToParent() {
        if (this.parent == null && this.abilityNode.parent() != null) {
            this.parent = this.getFirstVisibleParent(this.advancementNode);
            if (this.parent != null) {
                this.parent.addChild(this);
            }
        }
    }
}
