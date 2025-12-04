package com.feliscape.sanguis.content.ability;

import com.feliscape.sanguis.data.ability.VampireAbilityHolder;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class VampireAbilityTree {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<ResourceLocation, VampireAbilityNode> nodes = new Object2ObjectOpenHashMap<>();

    private void remove(VampireAbilityNode node) {
        for (VampireAbilityNode vampireAbilityNode : node.children()) {
            this.remove(vampireAbilityNode);
        }

        //LOGGER.info("Forgot about advancement {}", node.holder());
        this.nodes.remove(node.holder().id());
    }

    public void remove(Set<ResourceLocation> advancements) {
        for (ResourceLocation resourcelocation : advancements) {
            VampireAbilityNode vampireAbilityNode = this.nodes.get(resourcelocation);
            if (vampireAbilityNode == null) {
                LOGGER.warn("Told to remove ability {} but I don't know what that is", resourcelocation);
            } else {
                this.remove(vampireAbilityNode);
            }
        }
    }

    public void addAll(Collection<VampireAbilityHolder> holders) {
        List<VampireAbilityHolder> list = new ArrayList<>(holders);

        while (!list.isEmpty()) {
            if (!list.removeIf(this::tryInsert)) {
                LOGGER.error("Couldn't load abilities: {}", list);
                break;
            }
        }

        LOGGER.info("Loaded {} abilities", this.nodes.size());
    }

    private boolean tryInsert(VampireAbilityHolder holder) {
        Optional<ResourceLocation> optional = holder.value().parent();
        VampireAbilityNode vampireAbilityNode = optional.map(this.nodes::get).orElse(null);
        if (vampireAbilityNode == null && optional.isPresent()) {
            return false;
        } else {
            VampireAbilityNode node = new VampireAbilityNode(holder, vampireAbilityNode);
            if (vampireAbilityNode != null) {
                vampireAbilityNode.addChild(node);
            }

            this.nodes.put(holder.id(), node);

            return true;
        }
    }

    public void clear() {
        this.nodes.clear();
    }

    @Nullable
    public VampireAbilityNode get(ResourceLocation id) {
        return this.nodes.get(id);
    }

    @Nullable
    public VampireAbilityNode get(VampireAbilityHolder advancement) {
        return this.nodes.get(advancement.id());
    }
}
