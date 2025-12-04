package com.feliscape.sanguis.data.ability;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Helper function used to have an instance of the ability available
 */
public class VampireAbilityWrapper {
    private final VampireAbility.Builder abilityBuilder;
    private final VampireAbilityWrapper.Builder builder;
    private final ResourceLocation id;

    VampireAbilityHolder datagenResult;
    private final Component name;
    private final Component description;

    public VampireAbilityWrapper(ResourceLocation id, UnaryOperator<VampireAbilityWrapper.Builder> operator) {
        this.id = id;


        builder = VampireAbilityWrapper.Builder.of();
        operator.apply(builder);

        this.name = nameComponent(id);
        this.description = descriptionComponent(id);

        this.abilityBuilder = builder.abilityBuilder;
        this.abilityBuilder
                .name(this.name)
                .description(this.description)
                .cost(builder.cost);
    }

    public static Component nameComponent(ResourceLocation id){
        return Component.translatable("advancements.%s.%s.title".formatted(id.getNamespace(),
                id.getPath().replace('/', '.')));
    }
    public static Component nameComponent(ResourceLocation id, @Nullable ChatFormatting color){
        MutableComponent mutable = Component.translatable("advancements.%s.%s.title".formatted(id.getNamespace(),
                id.getPath().replace('/', '.')));
        if (color != null){
            mutable.withStyle(color);
        }
        return mutable;
    }
    public static Component descriptionComponent(ResourceLocation id){
        return Component.translatable("advancements.%s.%s.description".formatted(id.getNamespace(),
                id.getPath().replace('/', '.')));
    }

    public void save(HolderLookup.Provider lookupProvider, Consumer<VampireAbilityHolder> consumer, ExistingFileHelper existingFileHelper) {
        if (builder.parent != null){
            builder.parent
                    .ifLeft(a -> abilityBuilder.parent(a.datagenResult))
                    .ifRight(a -> abilityBuilder.parent(a));
        }
        datagenResult = this.abilityBuilder.save(consumer, id, existingFileHelper);
    }

    public ResourceLocation getId(){
        return id;
    }

    public Component getName() {
        return name;
    }

    public Component getDescription() {
        return description;
    }

    public VampireAbility getAbility() {
        return datagenResult.value();
    }

    public static class Builder{
        Either<VampireAbilityWrapper, VampireAbilityHolder> parent;
        VampireAbility.Builder abilityBuilder;
        int cost;
        public Builder() {
            this.abilityBuilder = new VampireAbility.Builder();
        }

        public static VampireAbilityWrapper.Builder of(){
            return new VampireAbilityWrapper.Builder();
        }

        public VampireAbilityWrapper.Builder after(VampireAbilityWrapper advancement){
            this.parent = Either.left(advancement);
            return this;
        }
        public VampireAbilityWrapper.Builder after(VampireAbilityHolder holder){
            this.parent = Either.right(holder);
            return this;
        }
        public VampireAbilityWrapper.Builder costs(int cost){
            this.cost = cost;
            return this;
        }
    }
}
