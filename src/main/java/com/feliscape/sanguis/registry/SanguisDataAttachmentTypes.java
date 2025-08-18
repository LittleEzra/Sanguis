package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.EntityBloodData;
import com.feliscape.sanguis.content.attachment.VampireData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SanguisDataAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Sanguis.MOD_ID);

    public static final Supplier<AttachmentType<VampireData>> VAMPIRISM = ATTACHMENT_TYPES.register("vampirism",
            () -> AttachmentType.serializable(VampireData::getInstance)
                    .sync(new VampireData.SyncHandler())
                    .copyOnDeath()
                    .copyHandler(VampireData::copyPersistent).build());
    public static final Supplier<AttachmentType<EntityBloodData>> ENTITY_BLOOD = ATTACHMENT_TYPES.register("entity_blood",
            () -> AttachmentType.serializable(EntityBloodData::getInstance).sync(new EntityBloodData.SyncHandler()).build());

    public static void register(IEventBus eventBus){
        ATTACHMENT_TYPES.register(eventBus);
    }
}
