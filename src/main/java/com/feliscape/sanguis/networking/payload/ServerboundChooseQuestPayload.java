package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.menu.QuestBoardMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundChooseQuestPayload(int containerId, int index) implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<ServerboundChooseQuestPayload> TYPE =
            new CustomPacketPayload.Type<>(Sanguis.location("choose_quest"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundChooseQuestPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerboundChooseQuestPayload::containerId,
            ByteBufCodecs.VAR_INT,
            ServerboundChooseQuestPayload::index,
            ServerboundChooseQuestPayload::new
    );

    public static void handle(ServerboundChooseQuestPayload payload, IPayloadContext context) {
        Player player = context.player();
        if (player.containerMenu.containerId == payload.containerId && player.containerMenu instanceof QuestBoardMenu menu){
            menu.chooseQuest(payload.index, player);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
