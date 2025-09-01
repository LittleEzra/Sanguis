package com.feliscape.sanguis.data.datagen.language;

import com.feliscape.sanguis.SanguisClientConfig;
import com.feliscape.sanguis.SanguisServerConfig;
import com.feliscape.sanguis.data.damage.SanguisDamageTypes;
import com.feliscape.sanguis.data.datagen.advancement.SanguisAdvancements;
import com.feliscape.sanguis.registry.*;
import net.minecraft.data.PackOutput;

public class SanguisEnUsProvider extends SanguisLanguageProvider{
    public SanguisEnUsProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addItem(SanguisItems.DAEMONOLOGIE, "Daemonologie");

        this.addItem(SanguisItems.BAT_WING, "Bat Wing");
        this.addItem(SanguisItems.BLOODY_FANG, "Bloody Fang");
        this.addItem(SanguisItems.ACTIVATED_VAMPIRE_BLOOD, "Activated Vampire Blood");
        this.addItemTooltip(SanguisItems.ACTIVATED_VAMPIRE_BLOOD, "[DEBUG ITEM] Inflicts vampirism, while skipping the incubation time");
        this.addItem(SanguisItems.VAMPIRE_BLOOD, "Vampire Blood");
        this.addItem(SanguisItems.BLOOD_BOTTLE, "Blood Bottle");
        this.addItem(SanguisItems.PARASOL, "Parasol");
        this.addItem(SanguisItems.BLOOD_SOAKED_COIN, "Blood-Soaked Coin");
        this.addItem(SanguisItems.BLACKENED_COIN, "Blackened Coin");

        this.addItem(SanguisItems.STEEL_BLEND, "Steel Blend");
        this.addItem(SanguisItems.STEEL_INGOT, "Steel Ingot");

        this.addItem(SanguisItems.SYRINGE, "Syringe");
        this.addItem(SanguisItems.GARLIC_INJECTION, "Garlic Injection");
        this.addItemTooltip(SanguisItems.GARLIC_INJECTION, "Cures or immunizes against vampirism");
        this.addItem(SanguisItems.ACID_INJECTION, "Acid Injection");
        this.addItemTooltip(SanguisItems.ACID_INJECTION, "Destroys the garlic within a hunter's blood");
        this.add(SanguisItems.ACID_INJECTION.get().getDescriptionId() + ".warning", "Warning: You will likely die after injecting this");

        this.addItem(SanguisItems.WOODEN_STAKE, "Wooden Stake");
        this.addItem(SanguisItems.REINFORCED_STAKE, "Reinforced Stake");

        this.addItem(SanguisItems.STEEL_CLEAVER, "Steel Cleaver");
        this.addItem(SanguisItems.DIAMOND_CLEAVER, "Diamond Cleaver");
        this.add("item.sanguis.cleaver.tooltip", "%1$s damage against Vampires");

        this.addItem(SanguisItems.GOLDEN_QUARREL, "Golden Quarrel");

        this.addItem(SanguisItems.GARLIC, "Garlic");
        this.addItem(SanguisItems.GARLIC_FLOWER, "Garlic Flower");

        this.addItem(SanguisItems.VAMPIRE_SPAWN_EGG, "Vampire Spawn Egg");
        this.addItem(SanguisItems.VAMPIRE_HUNTER_SPAWN_EGG, "Vampire Hunter Spawn Egg");

        this.addBlock(SanguisBlocks.QUEST_BOARD, "Quest Board");
        this.addBlock(SanguisBlocks.GARLIC, "Garlic");
        this.addBlock(SanguisBlocks.WILD_GARLIC, "Wild Garlic");
        this.addBlock(SanguisBlocks.GARLIC_STRING, "Garlic String");

        this.addBlock(SanguisBlocks.WHITE_COFFIN, "White Coffin");
        this.addBlock(SanguisBlocks.LIGHT_GRAY_COFFIN, "Light Gray Coffin");
        this.addBlock(SanguisBlocks.GRAY_COFFIN, "Gray Coffin");
        this.addBlock(SanguisBlocks.BLACK_COFFIN, "Black Coffin");
        this.addBlock(SanguisBlocks.BROWN_COFFIN, "Brown Coffin");
        this.addBlock(SanguisBlocks.RED_COFFIN, "Red Coffin");
        this.addBlock(SanguisBlocks.ORANGE_COFFIN, "Orange Coffin");
        this.addBlock(SanguisBlocks.YELLOW_COFFIN, "Yellow Coffin");
        this.addBlock(SanguisBlocks.LIME_COFFIN, "Lime Coffin");
        this.addBlock(SanguisBlocks.GREEN_COFFIN, "Green Coffin");
        this.addBlock(SanguisBlocks.CYAN_COFFIN, "Cyan Coffin");
        this.addBlock(SanguisBlocks.LIGHT_BLUE_COFFIN, "Light Blue Coffin");
        this.addBlock(SanguisBlocks.BLUE_COFFIN, "Blue Coffin");
        this.addBlock(SanguisBlocks.PURPLE_COFFIN, "Purple Coffin");
        this.addBlock(SanguisBlocks.MAGENTA_COFFIN, "Magenta Coffin");
        this.addBlock(SanguisBlocks.PINK_COFFIN, "Pink Coffin");

        this.addEntityType(SanguisEntityTypes.VAMPIRE, "Vampire");
        this.addEntityType(SanguisEntityTypes.VAMPIRE_HUNTER, "Vampire Hunter");

        this.addSubtitle(SanguisSoundEvents.BAT_TRANSFORM, "Player transforms into bat");
        this.addSubtitle(SanguisSoundEvents.VAMPIRE_TRANSFORM, "Player transforms into vampire");
        this.addSubtitle(SanguisSoundEvents.VAMPIRE_DRINK, "Vampire drinks");
        this.addSubtitle(SanguisSoundEvents.INJECT, "Injection is used");
        this.addSubtitle(SanguisSoundEvents.BLACKENED_COIN_USE, "Blackened Coin is used");

        //noinspection SpellCheckingInspection
        this.addAdvancement(SanguisAdvancements.ROOT,
                "Sanguis",
                "Sanguis turpis et dentes cruenti"); // Vile blood and blood-stained teeth

        this.addAdvancement(SanguisAdvancements.VAMPIRE_TRANSFORMATION,
                "Bloodsucker",
                "Complete your transformation into a vampire");
        this.addAdvancement(SanguisAdvancements.BAT_TRANSFORM,
                "Look! I'm a bat!",
                "Turn into bat. Adorable!");
        this.addAdvancement(SanguisAdvancements.VAMPIRE_CURE,
                "Back to Mortal Blood",
                "Revert your transformation by drinking harmful Garlic Solution");

        this.addAdvancement(SanguisAdvancements.HUNTER_INJECT,
                "Vaccinated",
                "Become a Vampire Hunter by immunizing yourself against vampirism");
        this.addAdvancement(SanguisAdvancements.QUEST_COMPLETE,
                "Toss A Coin To Your Witcher",
                "Complete a quest from a Hunter Camp");
        this.addAdvancement(SanguisAdvancements.BIBLE_HIT,
                "Naughty Vampire!",
                "Hit a Vampire with a copy of Daemonologie");

        this.addDeathMessage(SanguisDamageTypes.DRAINING, "%1$s was drained");
        this.addDeathMessagePlayer(SanguisDamageTypes.DRAINING, "%1$s was drained by %2$s");

        this.add("quest.sanguis.fetch_items.name", "Fetch Items");
        this.add("quest.sanguis.fetch_items.title", "Collect %1$s");
        this.add("quest.sanguis.kill_mobs.name", "Kill Mobs");
        this.add("quest.sanguis.kill_mobs.title", "Defeat %1$s");

        this.add("itemGroup.sanguis.base", "Sanguis");

        this.add("sanguis.cant_eat_message", "You cannot eat this");

        this.addKeyMapping(SanguisKeyMappings.DRAIN_BLOOD, "Drain Blood");
        this.addKeyMapping(SanguisKeyMappings.BAT_TRANSFORMATION, "Bat Transformation");
        this.addKeyMapping(SanguisKeyMappings.OPEN_ACTIVE_QUESTS, "Open Active Quests");

        this.add("container.sanguis.active_quests", "Active Quests");
        this.add("container.sanguis.active_quests.cancel", "Cancel Quest");
        this.add("container.sanguis.active_quests.complete", "Complete Quest");

        this.add("commands.sanguis.quest.addrandom.failure.invalid_type", "Quest type \"%1$s\" doesn't exist");
        this.add("commands.sanguis.quest.addrandom.success.single", "Added quest of type \"%1$s\" to %2$s");
        this.add("commands.sanguis.quest.addrandom.success.multiple", "Added quest of type \"%1$s\" to %2$s players");

        this.addConfigSection("vampire_form", "Vampire Form");
        this.addConfigValue("client", SanguisClientConfig.CONFIG.vampireNightVisionBrightness, "Night Vision Brightness");

        // Vampire Form
        this.addConfigValue("server", SanguisServerConfig.CONFIG.vampireDrainDistance, "Drain Distance");
        this.addConfigValue("server", SanguisServerConfig.CONFIG.vampireInfectChance, "Vampire Infect Chance");
    }
}
