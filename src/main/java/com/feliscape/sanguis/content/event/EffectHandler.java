package com.feliscape.sanguis.content.event;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.feliscape.sanguis.registry.SanguisMobEffects;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class EffectHandler {
    private static final Supplier<AttachmentType<Unit>> WEREBAT_CURSE = SanguisDataAttachmentTypes.WEREBAT_CURSE;

    @SubscribeEvent
    public static void removed(MobEffectEvent.Remove event){
        if (!event.getEffect().is(SanguisMobEffects.WEREBAT_CURSE)) return;

        var entity = event.getEntity();
        if (entity.hasData(WEREBAT_CURSE)){
            entity.removeData(WEREBAT_CURSE);
            entity.syncData(WEREBAT_CURSE);
        }
    }
    @SubscribeEvent
    public static void expired(MobEffectEvent.Expired event){
        if (event.getEffectInstance() == null || !event.getEffectInstance().getEffect().is(SanguisMobEffects.WEREBAT_CURSE)) return;

        var entity = event.getEntity();
        if (entity.hasData(WEREBAT_CURSE)){
            entity.removeData(WEREBAT_CURSE);
            entity.syncData(WEREBAT_CURSE);
        }
    }
    @SubscribeEvent
    public static void added(MobEffectEvent.Added event){
        if (!event.getEffectInstance().getEffect().is(SanguisMobEffects.WEREBAT_CURSE)) return;

        var entity = event.getEntity();
        if (!entity.hasData(WEREBAT_CURSE)){
            entity.getData(WEREBAT_CURSE);
            entity.syncData(WEREBAT_CURSE);
        }
    }
}
