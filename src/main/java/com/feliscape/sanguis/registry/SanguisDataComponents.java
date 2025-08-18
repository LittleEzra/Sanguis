package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Sanguis.MOD_ID);

    public static final Supplier<DataComponentType<Integer>> BLOOD = DATA_COMPONENTS.registerComponentType(
            "blood", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );
    public static final Supplier<DataComponentType<Integer>> MAX_BLOOD = DATA_COMPONENTS.registerComponentType(
            "max_blood", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static void register(IEventBus eventBus){
        DATA_COMPONENTS.register(eventBus);
    }
}
