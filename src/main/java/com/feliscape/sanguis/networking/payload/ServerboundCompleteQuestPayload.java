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

public record ServerboundCompleteQuestPayload(int containerId, int index) implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<ServerboundCompleteQuestPayload> TYPE =
            new CustomPacketPayload.Type<>(Sanguis.location("complete_quest"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCompleteQuestPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerboundCompleteQuestPayload::containerId,
            ByteBufCodecs.VAR_INT,
            ServerboundCompleteQuestPayload::index,
            ServerboundCompleteQuestPayload::new
    );

    public static void handle(ServerboundCompleteQuestPayload payload, IPayloadContext context) {
        Player player = context.player();
        if (HunterUtil.isHunter(player)){
            player.getData(HunterData.type()).getQuests().complete(payload.index);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
