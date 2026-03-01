package com.feliscape.sanguis.content.ability;

import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class VampireAbility {
    @Nullable
    public String descriptionId;
    private int cost;

    public VampireAbility(int cost) {
        this.cost = cost;
    }

    public Component getTranslation() {
        return Component.translatable(this.getTranslationId());
    }
    public Component getDescription() {
        return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GRAY);
    }

    public String getTranslationId() {
        return this.getOrCreateTranslationId();
    }
    public String getDescriptionId() {
        return this.getOrCreateTranslationId() + ".description";
    }

    protected String getOrCreateTranslationId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("vampire_ability", SanguisRegistries.VAMPIRE_ABILITIES.getKey(this));
        }

        return this.descriptionId;
    }

    public int getCost() {
        return cost;
    }
}
