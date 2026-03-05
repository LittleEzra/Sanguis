package com.feliscape.sanguis.content.ritual;

import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.registry.SanguisMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class WerebatRitual extends ReagentListRitual{
    public WerebatRitual() {
        super(List.of(
                SanguisItems.BAT_WING.toStack(),
                SanguisItems.BAT_WING.toStack(),
                SanguisItems.VAMPIRE_BLOOD.toStack()
        ), new ItemStack(Items.AMETHYST_SHARD));
    }

    @Override
    protected BlockPattern createPattern() {
        return LayeredBlockPatternBuilder.start()
                .layer(
                        "#C C#",
                        "C   C",
                        "  A  ",
                        "C   C",
                        "#C C#"
                )
                .layer(
                        "o   o",
                        "     ",
                        "     ",
                        "     ",
                        "o   o"
                )
                .where('A', SanguisBlocks.BLOOD_ALTAR)
                .where('#', Blocks.BLACKSTONE)
                .where('o', Blocks.GOLD_BLOCK)
                .where('C', blockInWorld -> blockInWorld.getState().is(Blocks.BLACK_CANDLE) &&
                        blockInWorld.getState().getValue(CandleBlock.LIT))
                .build();
    }

    @Override
    public Result activate(Level level, BlockPos pos, List<Player> nearbyPlayers, Player activatingPlayer) {
        for (Player player : nearbyPlayers){
            player.addEffect(new MobEffectInstance(SanguisMobEffects.WEREBAT_CURSE, 10 * 60 * 20));
        }
        return Result.SUCCESS_CONSUME;
    }
}
