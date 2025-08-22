package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BatTransformationPayload() implements CustomPacketPayload {
    private static final BatTransformationPayload INSTANCE = new BatTransformationPayload();

    public static final Type<BatTransformationPayload> TYPE =
            new Type<>(Sanguis.location("bat_transformation"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BatTransformationPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static void handle(BatTransformationPayload payload, IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();

        if (VampireUtil.isVampire(player)){
            player.getData(VampireData.type()).toggleBatForm();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
