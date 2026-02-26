package com.feliscape.sanguis.networking.payload;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.menu.ActiveQuestsMenu;
import com.feliscape.sanguis.content.menu.VampireAbilitiesMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenAbilitiesPayload() implements CustomPacketPayload {
    private static final OpenAbilitiesPayload INSTANCE = new OpenAbilitiesPayload();

    public static final Type<OpenAbilitiesPayload> TYPE =
            new Type<>(Sanguis.location("open_abilities"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenAbilitiesPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private static final Component CONTAINER_TITLE = Component.translatable("container.sanguis.vampire_abilities");

    public static void handle(OpenAbilitiesPayload payload, IPayloadContext context) {
        context.player().openMenu(new SimpleMenuProvider(((containerId, playerInventory, player) ->
                new VampireAbilitiesMenu(containerId, playerInventory, player)), CONTAINER_TITLE));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
