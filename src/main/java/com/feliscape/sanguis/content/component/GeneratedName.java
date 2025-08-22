package com.feliscape.sanguis.content.component;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.locale.Language;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.Optional;

public class GeneratedName {
    public static final Codec<GeneratedName> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            SanguisRegistries.QUEST_TYPES.byNameCodec().fieldOf("quest_type").forGetter(n -> n.questType),
            Codec.STRING.optionalFieldOf("generated_string").forGetter(n -> n.generatedString)
    ).apply(inst, GeneratedName::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GeneratedName> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(SanguisRegistries.Keys.QUEST_TYPES),
            n -> n.questType,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            n -> n.generatedString,
            GeneratedName::new
    );

    private QuestType<?> questType;
    private Optional<String> generatedString = Optional.empty();
    public boolean hasBeenGenerated;
    @Nullable
    private Language decomposedWith;

    public GeneratedName(QuestType<?> questType){
        this.questType = questType;
    }
    public GeneratedName(QuestType<?> questType, Optional<String> generatedString){
        this.questType = questType;
        this.generatedString = generatedString;
        this.hasBeenGenerated = true;
    }

    private void decompose(){
        Language language = Language.getInstance();
        if (language != this.decomposedWith){
            this.decomposedWith = language;
            if (hasBeenGenerated) {
                hasBeenGenerated = false;
                return;
            }
            generatedString = Optional.of(Sanguis.NAME_GENERATOR.generateName(this.questType, RandomSource.create().nextLong()));
        }
    }

    public String getString(){
        this.decompose();
        return generatedString.isPresent() ? generatedString.get() : "";
    }
}
