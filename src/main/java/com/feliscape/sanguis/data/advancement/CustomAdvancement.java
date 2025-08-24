package com.feliscape.sanguis.data.advancement;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.datagen.advancement.SanguisAdvancements;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class CustomAdvancement {
    private final Advancement.Builder advancementBuilder;
    private final Builder builder;
    private final String id;

    AdvancementHolder datagenResult;

    private final Component title;
    private final Component description;

    public CustomAdvancement(String path, UnaryOperator<Builder> operator) {
        this.id = path;


        builder = Builder.of();
        operator.apply(builder);

        this.title = titleComponent(id, builder.type.titleColor);
        this.description = descriptionComponent(id);

        this.advancementBuilder = builder.builder;
        this.advancementBuilder.display(builder.displayIcon,
                this.title,
                this.description,
                builder.type.background,
                builder.type.border,
                builder.type.showToast,
                builder.announceToChatOverride.orElse(builder.type.announceToChat),
                builder.hiddenOverride.orElse(builder.type.hidden)
        );

        SanguisAdvancements.ENTRIES.add(this);
    }

    public String getId(){
        return id;
    }

    public Component getTitle() {
        return title;
    }

    public Component getDescription() {
        return description;
    }

    public void save(HolderLookup.Provider lookupProvider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        if (builder.parent != null){
            builder.parent
                    .ifLeft(a -> advancementBuilder.parent(a.datagenResult))
                    .ifRight(a -> advancementBuilder.parent(a));
        }
        builder.resolveRegistryTriggers(lookupProvider);
        datagenResult = this.advancementBuilder.save(consumer, Sanguis.location(id), existingFileHelper);
    }

    private Component titleComponent(String name){
        return Component.translatable("advancements.%s.%s.title".formatted(Sanguis.MOD_ID, name.replace('/', '.')));
    }
    private Component titleComponent(String name, @Nullable ChatFormatting color){
        MutableComponent mutable = Component.translatable("advancements.%s.%s.title".formatted(Sanguis.MOD_ID, name.replace('/', '.')));
        if (color != null){
            mutable.withStyle(color);
        }
        return mutable;
    }
    private Component descriptionComponent(String name){
        return Component.translatable("advancements.%s.%s.description".formatted(Sanguis.MOD_ID, name.replace('/', '.')));
    }

    public enum Type{
        ROOT(AdvancementType.TASK, true, false, false, Sanguis.location("textures/gui/advancements.png")),
        STANDARD(AdvancementType.TASK, false, true, false),
        GOAL(AdvancementType.GOAL, false, true, true),
        CHALLENGE(AdvancementType.CHALLENGE, false, true, true),
        HIDDEN(AdvancementType.TASK, true, true, false)
        ;

        public final AdvancementType border;
        public final boolean hidden;
        public final boolean showToast;
        public final boolean announceToChat;
        public final ResourceLocation background;
        @Nullable
        public final ChatFormatting titleColor;

        Type(AdvancementType border, boolean hidden, boolean showToast, boolean announceToChat, ResourceLocation background) {
            this.border = border;
            this.hidden = hidden;
            this.showToast = showToast;
            this.announceToChat = announceToChat;
            this.background = background;
            this.titleColor = null;
        }
        Type(AdvancementType border, boolean hidden, boolean showToast, boolean announceToChat) {
            this.border = border;
            this.hidden = hidden;
            this.showToast = showToast;
            this.announceToChat = announceToChat;
            this.background = null;
            this.titleColor = null;
        }
        Type(AdvancementType border, boolean hidden, boolean showToast, boolean announceToChat, ChatFormatting titleColor) {
            this.border = border;
            this.hidden = hidden;
            this.showToast = showToast;
            this.announceToChat = announceToChat;
            this.background = null;
            this.titleColor = titleColor;
        }
    }

    public static class Builder{
        Either<CustomAdvancement, AdvancementHolder> parent;
        ItemStack displayIcon = ItemStack.EMPTY;
        Type type = Type.STANDARD;
        Optional<Boolean> announceToChatOverride = Optional.empty();
        Optional<Boolean> hiddenOverride = Optional.empty();
        Advancement.Builder builder;
        List<Function<HolderLookup.Provider, Criterion<?>>> registryTriggers = new ArrayList<>();
        private int keyIndex;

        public Builder() {
            this.builder = new Advancement.Builder();
        }

        public static Builder of(){
            return new Builder();
        }

        public Builder after(CustomAdvancement advancement){
            this.parent = Either.left(advancement);
            return this;
        }
        public Builder after(AdvancementHolder holder){
            this.parent = Either.right(holder);
            return this;
        }

        public Builder type(Type type){
            this.type = type;
            return this;
        }

        public Builder icon(ItemLike item){
            return this.icon(new ItemStack(item));
        }
        public Builder icon(ItemStack itemStack){
            this.displayIcon = itemStack;
            return this;
        }
        public Builder announceToChat(boolean announceToChat){
            this.announceToChatOverride = Optional.of(announceToChat);
            return this;
        }
        public Builder hidden(boolean hidden){
            this.hiddenOverride = Optional.of(hidden);
            return this;
        }

        public Builder awardedAutomatically() {
            //return trigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{}));
            return trigger(PlayerTrigger.TriggerInstance.tick());
        }

        public Builder hasItem(ItemLike item) {
            return trigger(InventoryChangeTrigger.TriggerInstance.hasItems(item));
        }
        public Builder hasItem(ItemPredicate predicate) {
            return trigger(InventoryChangeTrigger.TriggerInstance.hasItems(predicate));
        }
        public Builder hasItem(ItemPredicate... predicates) {
            return trigger(InventoryChangeTrigger.TriggerInstance.hasItems(predicates));
        }
        public Builder hasItem(ItemLike... items) {
            return trigger(InventoryChangeTrigger.TriggerInstance.hasItems(items));
        }
        public Builder consumeItem(ItemLike item) {
            return trigger(ConsumeItemTrigger.TriggerInstance.usedItem(item.asItem()));
        }

        public Builder registryTrigger(Function<HolderLookup.Provider, Criterion<?>> trigger) {
            this.registryTriggers.add(trigger);
            return this;
        }

        public Builder trigger(Criterion<?> trigger) {
            builder.addCriterion(String.valueOf(keyIndex), trigger);
            keyIndex++;
            return this;
        }

        public void resolveRegistryTriggers(HolderLookup.Provider registries) {
            for (var t : registryTriggers){
                this.trigger(t.apply(registries));
            }
        }
    }
}
