package com.feliscape.sanguis.content.component;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import javax.annotation.Nullable;
import java.util.Optional;

public class NameGeneratorContents implements ComponentContents {
    public static final MapCodec<NameGeneratorContents> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SanguisRegistries.QUEST_TYPES.byNameCodec().fieldOf("quest_type").forGetter(contents -> contents.questType),
            Codec.LONG.fieldOf("seed").forGetter(contents -> contents.seed)
    ).apply(inst, NameGeneratorContents::new));

    public static final ComponentContents.Type<NameGeneratorContents> TYPE = new ComponentContents.Type<>(CODEC, "sanguis.name_generator");

    private QuestType<?> questType;
    private long seed;
    private String generatedString = null;
    @Nullable
    private Language decomposedWith;

    public NameGeneratorContents(QuestType<?> type, long seed){
        this.questType = type;
        this.seed = seed;
    }

    public static MutableComponent component(HunterQuest quest, long seed){
        return MutableComponent.create(new NameGeneratorContents(quest.type(), seed));
    }

    private void decompose(){
        Language language = Language.getInstance();
        if (language != this.decomposedWith){
            this.decomposedWith = language;
            generatedString = Sanguis.NAME_GENERATOR.generateName(this.questType, this.seed);
        }
    }

    @Override
    public <T> Optional<T> visit(FormattedText.ContentConsumer<T> contentConsumer) {
        this.decompose();

        return contentConsumer.accept(this.generatedString);
    }

    @Override
    public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style) {
        this.decompose();

        return styledContentConsumer.accept(style, this.generatedString);
    }

    @Override
    public Type<?> type() {
        return TYPE;
    }
}
