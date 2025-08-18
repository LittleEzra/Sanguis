package com.feliscape.sanguis.content.block;

import com.feliscape.sanguis.registry.SanguisItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GarlicCropBlock extends CropBlock {
    public static final MapCodec<GarlicCropBlock> CODEC = simpleCodec(GarlicCropBlock::new);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    public static final int MAX_AGE = 4;

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 7.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 11.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0)
    };

    public GarlicCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return SanguisItems.GARLIC;
    }

    @Override
    public MapCodec<? extends GarlicCropBlock> codec() {
        return CODEC;
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 1, 3);
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }
}
