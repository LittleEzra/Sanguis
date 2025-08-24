package com.feliscape.sanguis.content.quest;

import com.feliscape.sanguis.content.quest.registry.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public record QuestPredicate(Optional<HolderSet<QuestType<?>>> types, Optional<NbtPredicate> nbt) {
    public static final Codec<QuestPredicate> CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
                            RegistryCodecs.homogeneousList(SanguisRegistries.Keys.QUEST_TYPES).optionalFieldOf("types").forGetter(QuestPredicate::types),
                            NbtPredicate.CODEC.optionalFieldOf("nbt").forGetter(QuestPredicate::nbt)
                    )
                    .apply(inst, QuestPredicate::new)
    );

    @SuppressWarnings("RedundantIfStatement")
    public boolean test(HunterQuest quest) {
        if (this.types.isEmpty() || quest.is(this.types.get())) return true;
        if (nbt.isPresent() && nbt.get().matches(HunterQuest.serialize(quest))) return true;

        return false;
    }

    public static class Builder {
        private Optional<HolderSet<QuestType<?>>> types = Optional.empty();
        private Optional<NbtPredicate> nbt = Optional.empty();

        public static QuestPredicate.Builder quest() {
            return new QuestPredicate.Builder();
        }

        public QuestPredicate.Builder of(QuestType<?>... types) {
            this.types = Optional.of(HolderSet.direct(QuestType::builtInRegistryHolder, types));
            return this;
        }

        public QuestPredicate.Builder of(Collection<QuestType<?>> blocks) {
            this.types = Optional.of(HolderSet.direct(QuestType::builtInRegistryHolder, blocks));
            return this;
        }

        public QuestPredicate.Builder of(TagKey<QuestType<?>> tag) {
            this.types = Optional.of(SanguisRegistries.QUEST_TYPES.getOrCreateTag(tag));
            return this;
        }

        public QuestPredicate.Builder hasNbt(CompoundTag nbt) {
            this.nbt = Optional.of(new NbtPredicate(nbt));
            return this;
        }
        public QuestPredicate build() {
            return new QuestPredicate(this.types, this.nbt);
        }
    }
}
