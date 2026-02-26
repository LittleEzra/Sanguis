package com.feliscape.sanguis.client.atlas;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

public class VampireAbilityTextureManager extends TextureAtlasHolder {
    public static final ResourceLocation EMPTY = Sanguis.location("empty");

    public VampireAbilityTextureManager(TextureManager textureManager) {
        super(textureManager, Sanguis.location("textures/atlas/abilities.png"), Sanguis.location("abilities"));
    }

    public TextureAtlasSprite get(VampireAbility ability) {
        ResourceLocation location = SanguisRegistries.VAMPIRE_ABILITIES.getKey(ability);
        return this.getSprite(location == null ? EMPTY : location);
    }
}
