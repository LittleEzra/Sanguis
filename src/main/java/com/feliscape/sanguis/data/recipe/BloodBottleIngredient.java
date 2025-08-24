package com.feliscape.sanguis.data.recipe;

import com.feliscape.sanguis.content.item.BloodBottleItem;
import com.feliscape.sanguis.registry.SanguisDataComponents;
import com.feliscape.sanguis.registry.SanguisIngredientTypes;
import com.feliscape.sanguis.registry.SanguisItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.Objects;
import java.util.stream.Stream;

public class BloodBottleIngredient implements ICustomIngredient {
    public static final MapCodec<BloodBottleIngredient> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            Codec.INT.optionalFieldOf("required_blood", 1).forGetter(BloodBottleIngredient::getRequiredBlood))
                    .apply(builder, BloodBottleIngredient::new));
    public static final StreamCodec<ByteBuf, BloodBottleIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            BloodBottleIngredient::getRequiredBlood,
            BloodBottleIngredient::new
    );

    private int requiredBlood;

    protected int getRequiredBlood(){
        return requiredBlood;
    }

    public BloodBottleIngredient(int requiredBlood) {
        this.requiredBlood = requiredBlood;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!stack.has(SanguisDataComponents.BLOOD)) return false;
        int blood = stack.getOrDefault(SanguisDataComponents.BLOOD, 0);
        return blood >= requiredBlood;
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Stream.of(SanguisItems.BLOOD_BOTTLE.toStack());
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return SanguisIngredientTypes.BLOOD_BOTTLE_INGREDIENT.get();
    }

    public static BloodBottleIngredient of(int requiredBlood){
        return new BloodBottleIngredient(requiredBlood);
    }

    public static BloodBottleIngredient of(){
        return new BloodBottleIngredient(1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        } else{
            return obj instanceof BloodBottleIngredient ingredient && ingredient.requiredBlood == this.requiredBlood;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.requiredBlood);
    }
}
