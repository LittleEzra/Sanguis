package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.util.HunterUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundCancelQuestPayload(int containerId, int index) implements CustomPacketPayload{
    public static final Type<ServerboundCancelQuestPayload> TYPE =
            new Type<>(Sanguis.location("cancel_quest"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCancelQuestPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerboundCancelQuestPayload::containerId,
            ByteBufCodecs.VAR_INT,
            ServerboundCancelQuestPayload::index,
            ServerboundCancelQuestPayload::new
    );

    public static void handle(ServerboundCancelQuestPayload payload, IPayloadContext context) {
        Player player = context.player();
        if (HunterUtil.isHunter(player)){
            player.getData(HunterData.type()).getQuests().cancel(payload.index);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
