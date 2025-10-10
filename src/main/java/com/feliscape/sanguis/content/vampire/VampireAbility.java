package com.feliscape.sanguis.content.vampire;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public record VampireAbility(Optional<ResourceLocation> parent, Component name) {
    public static final Codec<VampireAbility> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.optionalFieldOf("parent").forGetter(VampireAbility::parent),
            ComponentSerialization.CODEC.fieldOf("name").forGetter(VampireAbility::name)
    ).apply(inst, VampireAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireAbility> STREAM_CODEC =
            StreamCodec.ofMember(VampireAbility::write, VampireAbility::read);

    private void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeOptional(this.parent, FriendlyByteBuf::writeResourceLocation);
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buffer, this.name);
    }

    private static VampireAbility read(RegistryFriendlyByteBuf buffer) {
        return new VampireAbility(
                buffer.readOptional(FriendlyByteBuf::readResourceLocation),
                ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buffer)
        );
    }

    public static class Builder {
        private Optional<ResourceLocation> parent = Optional.empty();
        private Component name;

        public static VampireAbility.Builder ability() {
            return new VampireAbility.Builder();
        }

        public VampireAbility.Builder parent(ResourceLocation parentId) {
            this.parent = Optional.of(parentId);
            return this;
        }
        public VampireAbility.Builder name(Component name) {
            this.name = name;
            return this;
        }

        public VampireAbilityHolder build(ResourceLocation id) {
            return new VampireAbilityHolder(id, new VampireAbility(this.parent, this.name));
        }

        public VampireAbilityHolder save(Consumer<VampireAbilityHolder> output, String id) {
            VampireAbilityHolder holder = this.build(ResourceLocation.parse(id));
            output.accept(holder);
            return holder;
        }
    }
}
