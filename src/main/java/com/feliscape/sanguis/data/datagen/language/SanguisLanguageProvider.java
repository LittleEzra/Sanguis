package com.feliscape.sanguis.data.datagen.language;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.advancement.CustomAdvancement;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.List;
import java.util.function.Supplier;

public abstract class SanguisLanguageProvider extends LanguageProvider {
    public SanguisLanguageProvider(PackOutput output, String locale) {
        super(output, Sanguis.MOD_ID, locale);
    }

    protected void addBlockAndItem(Supplier<? extends Block> key, String name) {
        this.addBlock(key, name);
        this.addItem(key.get()::asItem, name);
    }

    protected void addItemTooltip(Supplier<? extends Item> key, String name) {
        add(key.get().getDescriptionId() + ".tooltip", name);
    }
    protected void addDeathMessage(ResourceKey<DamageType> key, String message) {
        add("death.attack.%s".formatted(key.location().toString()), message);
    }
    protected void addDeathMessageItem(ResourceKey<DamageType> key, String message) {
        add("death.attack.%s.item".formatted(key.location().toString()), message);
    }
    protected void addDeathMessagePlayer(ResourceKey<DamageType> key, String message) {
        add("death.attack.%s.player".formatted(key.location().toString()), message);
    }
    protected void addMobEffect(Holder<? extends MobEffect> key, String name) {
        add(key.value().getDescriptionId(), name);
    }
    protected void addSubtitle(Supplier<SoundEvent> key, String name) {
        add("subtitle.%s.%s".formatted(Sanguis.MOD_ID, key.get().getLocation().getPath()), name);
    }
    protected void addAdvancement(String id, String title, String description) {
        add("advancements.%s.%s.title".formatted(Sanguis.MOD_ID, id), title);
        add("advancements.%s.%s.description".formatted(Sanguis.MOD_ID, id), description);
    }
    protected void addAdvancement(CustomAdvancement advancement, String title, String description) {
        if (advancement.getTitle().getContents() instanceof TranslatableContents translatable)
            add(translatable.getKey(), title);
        if (advancement.getDescription().getContents() instanceof TranslatableContents translatable)
            add(translatable.getKey(), description);
    }
    protected void addEnchantment(ResourceKey<Enchantment> key, String name) {
        add(Util.makeDescriptionId("enchantment", key.location()), name);
    }
    protected void addBiome(ResourceKey<Biome> key, String name) {
        add(Util.makeDescriptionId("biome", key.location()), name);
    }
    protected void addTab(Supplier<CreativeModeTab> key, String name) {
        add(Util.makeDescriptionId("itemGroup", BuiltInRegistries.CREATIVE_MODE_TAB.getKey(key.get())), name);
    }
    protected void addKeyMapping(KeyMapping mapping, String name) {
        add(mapping.getName(), name);
    }
    protected void addKeyMapping(Lazy<KeyMapping> mapping, String name) {
        add(mapping.get().getName(), name);
    }

    protected void addConfigSection(String section, String name){
        add(Sanguis.MOD_ID + ".configuration." + section, name);
    }
    protected void addConfigValue(String id, String name){
        add(Sanguis.MOD_ID + ".configuration." + id, name);
    }
    protected void addConfigValue(String environment, ModConfigSpec.ConfigValue<?> value, String name){
        add("%s.configuration.%s.%s".formatted(Sanguis.MOD_ID, environment, combineConfigValuePath(value.getPath())), name);
    }

    protected String combineConfigValuePath(List<String> path){
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < path.size(); i++){
            string.append(path.get(i));
            if (i < path.size() - 1){
                string.append(".");
            }
        }
        return string.toString();
    }
}
