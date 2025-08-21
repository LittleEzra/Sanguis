package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.content.menu.ActiveQuestsMenu;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenActiveQuestsPayload() implements CustomPacketPayload {
    private static final OpenActiveQuestsPayload INSTANCE = new OpenActiveQuestsPayload();

    public static final Type<OpenActiveQuestsPayload> TYPE =
            new Type<>(Sanguis.location("open_active_quests"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenActiveQuestsPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private static final Component CONTAINER_TITLE = Component.translatable("container.sanguis.active_quests");

    public static void handle(OpenActiveQuestsPayload payload, IPayloadContext context) {
        context.player().openMenu(new SimpleMenuProvider(((containerId, playerInventory, player) ->
                new ActiveQuestsMenu(containerId, playerInventory, player)), CONTAINER_TITLE));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
