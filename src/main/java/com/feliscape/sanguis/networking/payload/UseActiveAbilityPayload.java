package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireAbilityData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UseActiveAbilityPayload() implements CustomPacketPayload {
    private static final UseActiveAbilityPayload INSTANCE = new UseActiveAbilityPayload();

    public static final Type<UseActiveAbilityPayload> TYPE =
            new Type<>(Sanguis.location("use_active_ability"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UseActiveAbilityPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static void handle(UseActiveAbilityPayload payload, IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();

        if (VampireUtil.isVampire(player)){
            player.getData(VampireAbilityData.type()).useActiveAbility();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
