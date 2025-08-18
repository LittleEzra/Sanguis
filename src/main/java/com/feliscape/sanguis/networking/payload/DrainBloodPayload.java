package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireData;
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

public record DrainBloodPayload(int entityId) implements CustomPacketPayload {
    public static final Type<DrainBloodPayload> TYPE =
            new Type<>(Sanguis.location("drain_blood"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DrainBloodPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            DrainBloodPayload::entityId,
            DrainBloodPayload::new
    );

    public static void handle(DrainBloodPayload payload, IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        Entity entity = level.getEntity(payload.entityId);
        if (entity == null){
            Sanguis.LOGGER.error("[DrainBloodPayload] No entity with id {} found!", payload.entityId);
            return;
        }
        if (!(entity instanceof LivingEntity target)){
            return;
        }

        if (VampireUtil.isVampire(player) && VampireUtil.canDrink(player, target)){
            player.getData(VampireData.type()).drink(target);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
