package com.feliscape.sanguis.data.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EntityBloodContent(int amount, float saturationModifier) {
    public static final Codec<EntityBloodContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("amount").forGetter(EntityBloodContent::amount),
            Codec.FLOAT.fieldOf("saturationMod").forGetter(EntityBloodContent::saturationModifier)
    ).apply(inst, EntityBloodContent::new));
}
