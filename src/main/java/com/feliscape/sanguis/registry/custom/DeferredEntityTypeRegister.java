package com.feliscape.sanguis.registry.custom;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DeferredEntityTypeRegister extends DeferredRegister<EntityType<?>> {
    public static DeferredEntityTypeRegister create(String modid){
        return new DeferredEntityTypeRegister(modid);
    }

    protected DeferredEntityTypeRegister(String namespace) {
        super(Registries.ENTITY_TYPE, namespace);
    }

    /**
     * Convenience method that constructs a builder. Use this to avoid inference issues.
     *
     * @param name     The name for this entity type. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param factory  The factory used to typically construct the entity when using an existing helper from the type.
     * @param category The category of the entity, typically {@link MobCategory#MISC} for non-living entities, or one of the others for living entities.
     * @return A {@link DeferredHolder} which reflects the data that will be registered.
     * @param <E> the type of the entity
     */
    public <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> registerEntityType(String name, EntityType.EntityFactory<E> factory, MobCategory category) {
        return this.registerEntityType(name, factory, category, UnaryOperator.identity());
    }

    /**
     * Convenience method that constructs a builder for use in the operator. Use this to avoid inference issues.
     *
     * @param name     The name for this entity type. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param factory  The factory used to typically construct the entity when using an existing helper from the type.
     * @param category The category of the entity, typically {@link MobCategory#MISC} for non-living entities, or one of the others for living entities.
     * @param builder  The unary operator, which is passed a new builder for user operators, then builds it upon registration.
     * @return A {@link DeferredHolder} which reflects the data that will be registered.
     * @param <E> the type of the entity
     */
    public <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> registerEntityType(String name, EntityType.EntityFactory<E> factory, MobCategory category, UnaryOperator<EntityType.Builder<E>> builder) {
        return this.register(name, key -> builder.apply(EntityType.Builder.of(factory, category)).build(ResourceKey.create(Registries.ENTITY_TYPE, key).location().toString()));
    }
}
