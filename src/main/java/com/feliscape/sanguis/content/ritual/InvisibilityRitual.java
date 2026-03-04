package com.feliscape.sanguis.content.ritual;

import com.feliscape.sanguis.registry.SanguisBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class InvisibilityRitual extends ReagentListRitual{
    public InvisibilityRitual() {
        super(List.of(
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.GOLDEN_CARROT)
        ), new ItemStack(Items.ENDER_PEARL));
    }

    @Override
    protected BlockPattern createPattern() {
        return LayeredBlockPatternBuilder.start()
                .layer(
                        "#   #",
                        "     ",
                        "  A  ",
                        "     ",
                        "#   #"
                )
                .layer(
                        "#   #",
                        "     ",
                        "     ",
                        "     ",
                        "#   #"
                )
                .layer(
                        "o   o",
                        "     ",
                        "     ",
                        "     ",
                        "o   o"
                )
                .where('A', SanguisBlocks.BLOOD_ALTAR)
                .where('#', Tags.Blocks.GLASS_BLOCKS_COLORLESS)
                .where('o', Blocks.IRON_BLOCK)
                .build();
    }

    @Override
    public Result activate(Level level, BlockPos pos, List<Player> nearbyPlayers, Player activatingPlayer) {
        for (Player player : nearbyPlayers){
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 20));
        }
        return Result.SUCCESS_CONSUME;
    }
}
