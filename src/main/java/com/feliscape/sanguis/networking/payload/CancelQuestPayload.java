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

public record CancelQuestPayload(int containerId, int index) implements CustomPacketPayload{
    public static final Type<CancelQuestPayload> TYPE =
            new Type<>(Sanguis.location("cancel_quest"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CancelQuestPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CancelQuestPayload::containerId,
            ByteBufCodecs.VAR_INT,
            CancelQuestPayload::index,
            CancelQuestPayload::new
    );

    public static void handle(CancelQuestPayload payload, IPayloadContext context) {
        Player player = context.player();
        Sanguis.LOGGER.debug("Canceled quest with index {}", payload.index);
        if (HunterUtil.isHunter(player)){
            player.getData(HunterData.type()).getQuests().cancel(payload.index);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
