package com.feliscape.sanguis.data.ability;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record VampireAbilityHolder(ResourceLocation id, VampireAbility value) {
    public static final StreamCodec<RegistryFriendlyByteBuf, VampireAbilityHolder> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            VampireAbilityHolder::id,
            VampireAbility.STREAM_CODEC,
            VampireAbilityHolder::value,
            VampireAbilityHolder::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, List<VampireAbilityHolder>> LIST_STREAM_CODEC =
            STREAM_CODEC.apply(ByteBufCodecs.list());

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            if (other instanceof VampireAbilityHolder holder) {
                return this.id.equals(holder.id);
            }

            return false;
        }
    }
}
