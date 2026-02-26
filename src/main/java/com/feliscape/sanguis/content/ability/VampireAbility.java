package com.feliscape.sanguis.content.ability;

import com.feliscape.sanguis.registry.SanguisVampireAbilities;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class VampireAbility {
    @Nullable
    public String descriptionId;
    private int cost;

    public VampireAbility(int cost) {
        this.cost = cost;
    }

    public Component getDescription() {
        return Component.translatable(this.getDescriptionId());
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("vampire_ability", SanguisRegistries.VAMPIRE_ABILITIES.getKey(this));
        }

        return this.descriptionId;
    }

    public int getCost() {
        return cost;
    }
}
