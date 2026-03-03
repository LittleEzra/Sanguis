package com.feliscape.sanguis.content.ritual;

import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisParticles;
import com.feliscape.sanguis.registry.SanguisSoundEvents;
import com.feliscape.sanguis.util.VampireUtil;
import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
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
                        "#   #",
                        " C C ",
                        "  A  ",
                        " C C ",
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

        if (couldActivate){
            level.playSound(activatingPlayer, pos, SanguisSoundEvents.BAT_TRANSFORM.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

            if (level.isClientSide()){
                for (int i = 0; i < 22; i++){
                    double theta = Math.TAU * level.random.nextDouble();
                    level.addParticle(
                            SanguisParticles.BLOOD_DROPLET.get(),
                            pos.getX() + 0.5D,
                            pos.getY() + 1.0D,
                            pos.getZ() + 0.5D,
                            Math.cos(theta) * 0.1D * level.random.nextDouble(),
                            level.random.nextDouble() * 0.3D + 0.2D,
                            Math.sin(theta) * 0.1D * level.random.nextDouble()
                    );
                    if (level.random.nextInt(3) == 0){
                        level.addParticle(
                                ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT,
                                        0.05F, 0.0F, 0.0F),
                                pos.getX() + 0.5D,
                                pos.getY() + 1.0D,
                                pos.getZ() + 0.5D,
                                0.1D,
                                0.2D,
                                0.1D
                        );
                    }
                }
            }
        }

        return couldActivate ? Result.SUCCESS_CONSUME : Result.FAIL;
    }
}
