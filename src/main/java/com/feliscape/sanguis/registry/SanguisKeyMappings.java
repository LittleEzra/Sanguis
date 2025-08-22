package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class SanguisKeyMappings {
    public static final Lazy<KeyMapping> DRAIN_BLOOD = Lazy.of(() -> new KeyMapping(
            "key.sanguis.drain_blood",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.gameplay"
    ));
    public static final Lazy<KeyMapping> BAT_TRANSFORMATION = Lazy.of(() -> new KeyMapping(
            "key.sanguis.bat_transformation",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            "key.categories.gameplay"
    ));
    public static final Lazy<KeyMapping> OPEN_ACTIVE_QUESTS = Lazy.of(() -> new KeyMapping(
            "key.sanguis.open_active_quests",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "key.categories.inventory"
    ));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(DRAIN_BLOOD.get());
        event.register(BAT_TRANSFORMATION.get());
        event.register(OPEN_ACTIVE_QUESTS.get());
    }
}
