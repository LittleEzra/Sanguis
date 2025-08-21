package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.HunterUtil;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CompleteQuestPayload(int containerId, int index) implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<CompleteQuestPayload> TYPE =
            new CustomPacketPayload.Type<>(Sanguis.location("complete_quest"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CompleteQuestPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CompleteQuestPayload::containerId,
            ByteBufCodecs.VAR_INT,
            CompleteQuestPayload::index,
            CompleteQuestPayload::new
    );

    public static void handle(CompleteQuestPayload payload, IPayloadContext context) {
        Player player = context.player();
        Sanguis.LOGGER.debug("Completed quest with index {}", payload.index);
        if (HunterUtil.isHunter(player)){
            player.getData(HunterData.type()).getQuests().complete(payload.index);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
