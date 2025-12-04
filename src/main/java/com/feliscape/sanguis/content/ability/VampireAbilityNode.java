package com.feliscape.sanguis.content.ability;

import com.feliscape.sanguis.data.ability.VampireAbility;
import com.feliscape.sanguis.data.ability.VampireAbilityHolder;
import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;

import javax.annotation.Nullable;
import java.util.Set;

public class VampireAbilityNode {
    private final VampireAbilityHolder holder;
    @Nullable
    private final VampireAbilityNode parent;
    private final Set<VampireAbilityNode> children = new ReferenceOpenHashSet<>();

    @VisibleForTesting
    public VampireAbilityNode(VampireAbilityHolder holder, @Nullable VampireAbilityNode parent) {
        this.holder = holder;
        this.parent = parent;
    }

    public VampireAbility advancement() {
        return this.holder.value();
    }

    public VampireAbilityHolder holder() {
        return this.holder;
    }

    @Nullable
    public VampireAbilityNode parent() {
        return this.parent;
    }

    public VampireAbilityNode root() {
        return getRoot(this);
    }

    public static VampireAbilityNode getRoot(VampireAbilityNode node) {
        VampireAbilityNode abilityNode = node;

        while (true) {
            VampireAbilityNode abilityNodeParent = abilityNode.parent();
            if (abilityNodeParent == null) {
                return abilityNode;
            }

            abilityNode = abilityNodeParent;
        }
    }

    public Iterable<VampireAbilityNode> children() {
        return this.children;
    }

    @VisibleForTesting
    public void addChild(VampireAbilityNode child) {
        this.children.add(child);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof VampireAbilityNode abilityNode && this.holder.equals(abilityNode.holder);
        }
    }

    @Override
    public int hashCode() {
        return this.holder.hashCode();
    }

    @Override
    public String toString() {
        return this.holder.id().toString();
    }
}
