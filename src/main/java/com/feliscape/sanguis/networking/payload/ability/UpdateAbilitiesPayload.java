package com.feliscape.sanguis.networking.payload.ability;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.content.attachment.VampireAbilityData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.networking.payload.BatTransformationPayload;
import com.feliscape.sanguis.networking.payload.ServerboundCancelQuestPayload;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record UpdateAbilitiesPayload(List<VampireAbility> newAbilities, int entityId) implements CustomPacketPayload{

    public static final CustomPacketPayload.Type<UpdateAbilitiesPayload> TYPE =
            new CustomPacketPayload.Type<>(Sanguis.location("update_abilities"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAbilitiesPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(SanguisRegistries.Keys.VAMPIRE_ABILITIES).apply(ByteBufCodecs.list()),
            UpdateAbilitiesPayload::newAbilities,
            ByteBufCodecs.INT,
            UpdateAbilitiesPayload::entityId,
            UpdateAbilitiesPayload::new
    );

    public static void handle(UpdateAbilitiesPayload payload, IPayloadContext context) {
        var entity = context.player().level().getEntity(payload.entityId);
        if (entity instanceof Player p){
            p.getData(VampireAbilityData.type()).resolveUpdatedAbilities(payload.newAbilities);
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
