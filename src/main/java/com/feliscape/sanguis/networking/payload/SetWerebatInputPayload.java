package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.attachment.WerebatData;
import com.feliscape.sanguis.content.event.WerebatHandler;
import com.feliscape.sanguis.util.HunterUtil;
import net.minecraft.client.player.Input;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetWerebatInputPayload(boolean jumping) implements CustomPacketPayload {
    public SetWerebatInputPayload(Input input) {
        this(input.jumping);
    }

    public static final Type<SetWerebatInputPayload> TYPE =
            new Type<>(Sanguis.location("set_werebat_input"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetWerebatInputPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    SetWerebatInputPayload::jumping,
                    SetWerebatInputPayload::new
            );

    public static void handle(SetWerebatInputPayload payload, IPayloadContext context) {
        Player player = context.player();
        if (WerebatHandler.isWerebat(player)){
            player.getData(WerebatData.TYPE).setData(payload.jumping());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
