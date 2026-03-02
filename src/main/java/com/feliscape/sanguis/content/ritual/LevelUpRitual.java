package com.feliscape.sanguis.content.ritual;

import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

import java.util.List;
import java.util.function.Supplier;

public class LevelUpRitual extends BloodRitual{
    private final Item reagent;
    private final int tierToUpgrade;

    public LevelUpRitual(Item reagent, int tierToUpgrade) {
        this.reagent = reagent;
        this.tierToUpgrade = tierToUpgrade;
    }

    @Override
    protected boolean verifyItem(Player player, ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.is(reagent) && VampireUtil.isVampire(player) &&
                player.getData(VampireData.type()).getTier() >= tierToUpgrade;
    }

    @Override
    protected BlockPattern createPattern() {
        return LayeredBlockPatternBuilder.start()
                .layer(
                        "#C#",
                        "C C",
                        "#C#"
                )
                .layer(
                        "o o",
                        "   ",
                        "o o"
                )
                .where('#', Blocks.STONE_BRICKS)
                .where('C', blockInWorld -> blockInWorld.getState().is(BlockTags.CANDLES) &&
                        blockInWorld.getState().getValue(CandleBlock.LIT))
                .where('o', Blocks.GOLD_BLOCK)
                .build();
    }

    @Override
    public Result activate(Level level, BlockPos pos, List<Player> nearbyPlayers, Player activatingPlayer) {
        boolean couldActivate = false;

        for (int i = 0; i < nearbyPlayers.size(); i++){
            Player player = nearbyPlayers.get(i);
            if (VampireUtil.isVampire(player)){
                var vampirism = player.getData(VampireData.type());
                if (vampirism.getTier() == tierToUpgrade){
                    couldActivate = true;
                    player.getData(VampireData.type()).setTier(tierToUpgrade + 1);
                }
            }
        }

        return couldActivate ? Result.SUCCESS_CONSUME : Result.FAIL;
    }
}
