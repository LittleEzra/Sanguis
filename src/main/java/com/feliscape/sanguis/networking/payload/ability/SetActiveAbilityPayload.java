package com.feliscape.sanguis.networking.payload.ability;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.content.attachment.VampireAbilityData;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SetActiveAbilityPayload(VampireAbility pickedAbility, int entityId) implements CustomPacketPayload{

    public static final Type<SetActiveAbilityPayload> TYPE =
            new Type<>(Sanguis.location("set_active_ability"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetActiveAbilityPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(SanguisRegistries.Keys.VAMPIRE_ABILITIES),
            SetActiveAbilityPayload::pickedAbility,
            ByteBufCodecs.INT,
            SetActiveAbilityPayload::entityId,
            SetActiveAbilityPayload::new
    );

    public static void handle(SetActiveAbilityPayload payload, IPayloadContext context) {
        var entity = context.player().level().getEntity(payload.entityId);
        if (entity instanceof Player p){
            p.getData(VampireAbilityData.type()).setActiveAbility(payload.pickedAbility);
            p.syncData(VampireAbilityData.type());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
