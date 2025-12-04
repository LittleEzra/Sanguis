package com.feliscape.sanguis.data.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Optional;
import java.util.function.Consumer;

public record VampireAbility(Optional<ResourceLocation> parent, Component name, Component description, int cost) {
    public static final Codec<VampireAbility> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.optionalFieldOf("parent").forGetter(VampireAbility::parent),
            ComponentSerialization.CODEC.fieldOf("name").forGetter(VampireAbility::name),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(VampireAbility::description),
            Codec.INT.fieldOf("cost").forGetter(VampireAbility::cost)
    ).apply(inst, VampireAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireAbility> STREAM_CODEC =
            StreamCodec.ofMember(VampireAbility::write, VampireAbility::read);

    private void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeOptional(this.parent, FriendlyByteBuf::writeResourceLocation);
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buffer, this.name);
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buffer, this.description);
        buffer.writeVarInt(cost);
    }

    private static VampireAbility read(RegistryFriendlyByteBuf buffer) {
        return new VampireAbility(
                buffer.readOptional(FriendlyByteBuf::readResourceLocation),
                ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buffer),
                ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buffer),
                buffer.readVarInt()
        );
    }

    public static class Builder {
        private Optional<ResourceLocation> parent = Optional.empty();
        private Component name;
        private Component description;
        private int cost;

        public static VampireAbility.Builder ability() {
            return new VampireAbility.Builder();
        }

        public VampireAbility.Builder parent(ResourceLocation parentId) {
            this.parent = Optional.of(parentId);
            return this;
        }
        public VampireAbility.Builder parent(VampireAbilityHolder holder) {
            this.parent = Optional.of(holder.id());
            return this;
        }
        public VampireAbility.Builder name(Component name) {
            this.name = name;
            return this;
        }
        public VampireAbility.Builder description(Component description) {
            this.description = description;
            return this;
        }
        public VampireAbility.Builder cost(int cost) {
            this.cost = cost;
            return this;
        }

        public VampireAbilityHolder build(ResourceLocation id) {
            return new VampireAbilityHolder(id, new VampireAbility(this.parent, this.name, this.description, this.cost));
        }

        public VampireAbilityHolder save(Consumer<VampireAbilityHolder> output, String id) {
            VampireAbilityHolder holder = this.build(ResourceLocation.parse(id));
            output.accept(holder);
            return holder;
        }

        public VampireAbilityHolder save(Consumer<VampireAbilityHolder> output, ResourceLocation id) {
            VampireAbilityHolder holder = this.build(id);
            output.accept(holder);
            return holder;
        }

        public VampireAbilityHolder save(Consumer<VampireAbilityHolder> saver, ResourceLocation id, ExistingFileHelper fileHelper) {
            VampireAbilityHolder abilityHolder = this.build(id);
            Optional<ResourceLocation> parent = abilityHolder.value().parent();
            if (parent.isPresent() && !fileHelper.exists(parent.get(), PackType.SERVER_DATA, ".json", "sanguis/vampire_ability")) {
                throw new IllegalStateException("The parent: '%s' of ability '%s', has not been saved yet!".formatted(parent.orElseThrow(), id));
            } else {
                saver.accept(abilityHolder);
                fileHelper.trackGenerated(id, PackType.SERVER_DATA, ".json", "sanguis/vampire_ability");
                return abilityHolder;
            }
        }
    }
}
