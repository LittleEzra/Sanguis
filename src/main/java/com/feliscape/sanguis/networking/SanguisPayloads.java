package com.feliscape.sanguis.networking;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.networking.payload.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class SanguisPayloads {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                SanguisLevelEventPayload.TYPE,
                SanguisLevelEventPayload.STREAM_CODEC,
                SanguisLevelEventPayload::handle
        );

        registrar.playToServer(
                DrainBloodPayload.TYPE,
                DrainBloodPayload.STREAM_CODEC,
                DrainBloodPayload::handle
        );
        registrar.playToServer(
                BatTransformationPayload.TYPE,
                BatTransformationPayload.STREAM_CODEC,
                BatTransformationPayload::handle
        );

        registrar.playToServer(
                OpenAbilitiesPayload.TYPE,
                OpenAbilitiesPayload.STREAM_CODEC,
                OpenAbilitiesPayload::handle
        );
        registrar.playToServer(
                OpenActiveQuestsPayload.TYPE,
                OpenActiveQuestsPayload.STREAM_CODEC,
                OpenActiveQuestsPayload::handle
        );
        registrar.playToServer(
                ServerboundCompleteQuestPayload.TYPE,
                ServerboundCompleteQuestPayload.STREAM_CODEC,
                ServerboundCompleteQuestPayload::handle
        );
        registrar.playToServer(
                ServerboundCancelQuestPayload.TYPE,
                ServerboundCancelQuestPayload.STREAM_CODEC,
                ServerboundCancelQuestPayload::handle
        );
        registrar.playToServer(
                ServerboundChooseQuestPayload.TYPE,
                ServerboundChooseQuestPayload.STREAM_CODEC,
                ServerboundChooseQuestPayload::handle
        );
    }
}
