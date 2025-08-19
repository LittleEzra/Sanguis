package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.entity.living.VampireEntity;
import com.feliscape.sanguis.content.entity.projectile.GoldenQuarrel;
import com.feliscape.sanguis.registry.util.DeferredEntityTypeRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisEntityTypes {
    public static final DeferredEntityTypeRegister ENTITY_TYPES =
            DeferredEntityTypeRegister.create(Sanguis.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<VampireEntity>> VAMPIRE = ENTITY_TYPES.registerEntityType("vampire",
            VampireEntity::new, MobCategory.MONSTER, b -> b
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .passengerAttachments(2.0125F)
                    .ridingOffset(-0.7F)
                    .clientTrackingRange(8));

    public static final Supplier<EntityType<GoldenQuarrel>> GOLDEN_QUARREL = ENTITY_TYPES.registerEntityType("golden_quarrel",
            GoldenQuarrel::new, MobCategory.MISC, b -> b
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
