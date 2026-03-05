package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.EntityBloodData;
import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.attachment.VampireAbilityData;
import com.feliscape.sanguis.content.attachment.VampireData;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SanguisDataAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Sanguis.MOD_ID);

    public static final Supplier<AttachmentType<Unit>> WEREBAT_CURSE = ATTACHMENT_TYPES.register("werebat_curse",
            () -> AttachmentType.builder(() -> Unit.INSTANCE)
                    .serialize(Unit.CODEC)
                    .sync(StreamCodec.unit(Unit.INSTANCE))
                    .build());

    public static final Supplier<AttachmentType<VampireData>> VAMPIRISM = ATTACHMENT_TYPES.register("vampirism",
            () -> AttachmentType.builder(VampireData::getInstance)
                    .serialize(new VampireData.Serializer())
                    .sync(new VampireData.SyncHandler())
                    .copyOnDeath()
                    .copyHandler(VampireData::copyDeathPersistent).build());
    public static final Supplier<AttachmentType<HunterData>> HUNTER = ATTACHMENT_TYPES.register("hunter",
            () -> AttachmentType.serializable(HunterData::getInstance)
                    .sync(new HunterData.SyncHandler())
                    .copyOnDeath()
                    .copyHandler(HunterData::copyDeathPersistent).build());
    public static final Supplier<AttachmentType<VampireAbilityData>> VAMPIRE_ABILITIES = ATTACHMENT_TYPES.register("vampire_abilities",
            () -> AttachmentType.builder(VampireAbilityData::getInstance)
                    .serialize(new VampireAbilityData.Serializer())
                    .sync(new VampireAbilityData.SyncHandler())
                    .copyOnDeath().build());
    public static final Supplier<AttachmentType<EntityBloodData>> ENTITY_BLOOD = ATTACHMENT_TYPES.register("entity_blood",
            () -> AttachmentType.serializable(EntityBloodData::getInstance).sync(new EntityBloodData.SyncHandler()).build());

    public static void register(IEventBus eventBus){
        ATTACHMENT_TYPES.register(eventBus);
    }
}
