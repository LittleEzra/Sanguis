package com.feliscape.sanguis.data.datagen.language;

import com.feliscape.sanguis.SanguisClientConfig;
import com.feliscape.sanguis.SanguisServerConfig;
import com.feliscape.sanguis.data.damage.SanguisDamageTypes;
import com.feliscape.sanguis.data.datagen.advancement.SanguisAdvancements;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.registry.SanguisKeyMappings;
import net.minecraft.data.PackOutput;

public class SanguisEnUsProvider extends SanguisLanguageProvider{
    public SanguisEnUsProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addItem(SanguisItems.BLOODY_FANG, "Bloody Fang");
        this.addItem(SanguisItems.ACTIVATED_VAMPIRE_BLOOD, "Activated Vampire Blood");
        this.addItem(SanguisItems.VAMPIRE_BLOOD, "Vampire Blood");
        this.addItem(SanguisItems.BLOOD_BOTTLE, "Blood Bottle");

        this.addItem(SanguisItems.GARLIC_SOLUTION, "Garlic Solution");
        this.addItem(SanguisItems.WOODEN_STAKE, "Wooden Stake");

        this.addItem(SanguisItems.GARLIC, "Garlic");
        this.addItem(SanguisItems.GARLIC_FLOWER, "Garlic Flower");

        this.addItem(SanguisItems.VAMPIRE_SPAWN_EGG, "Vampire Spawn Egg");

        this.addBlock(SanguisBlocks.GARLIC, "Garlic");
        this.addBlock(SanguisBlocks.GARLIC_STRING, "Garlic String");

        this.addEntityType(SanguisEntityTypes.VAMPIRE, "Vampire");

        //noinspection SpellCheckingInspection
        this.addAdvancement(SanguisAdvancements.ROOT,
                "Sanguis",
                "Sanguis turpis et dentes cruenti"); // Vile blood and blood-stained teeth

        this.addAdvancement(SanguisAdvancements.TURN_TO_VAMPIRE,
                "Bloodsucker",
                "Complete your transformation into a vampire");
        this.addAdvancement(SanguisAdvancements.CURE_VAMPIRE,
                "Back to Mortal Blood",
                "Revert your transformation by drinking harmful Garlic Solution");

        this.addDeathMessage(SanguisDamageTypes.DRAINING, "%1$s was drained");
        this.addDeathMessagePlayer(SanguisDamageTypes.DRAINING, "%1$s was drained by %2$s");

        this.add("itemGroup.sanguis.base", "Sanguis");

        this.add("sanguis.cant_eat_message", "You cannot eat this");

        this.addKeyMapping(SanguisKeyMappings.DRAIN_BLOOD, "Drain Blood");

        this.addConfigSection("vampire_form", "Vampire Form");
        this.addConfigValue("client", SanguisClientConfig.CONFIG.vampireNightVisionBrightness, "Night Vision Brightness");

        // Vampire Form
        this.addConfigValue("server", SanguisServerConfig.CONFIG.vampireDrainDistance, "Drain Distance");
    }
}
