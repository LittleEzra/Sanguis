package com.feliscape.sanguis.content.block;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;
import java.util.function.Predicate;

public class BloodOrangeVineBlock extends Block implements BonemealableBlock {
    public static final BooleanProperty HEAD = BooleanProperty.create("head");

    private static final VoxelShape BODY_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape HEAD_SHAPE = Block.box(4.0D, 3.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public BloodOrangeVineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HEAD, true));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HEAD) ? HEAD_SHAPE : BODY_SHAPE;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !canSurvive(state, level, pos)){
            level.scheduleTick(pos, this, 1);
        }

        if (direction == Direction.DOWN){
            return state.setValue(HEAD, !this.isVineBlock(neighborState));
        } else{
            return state;
        }
    }

    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos posAbove = pos.above();
        BlockState aboveState = level.getBlockState(posAbove);
        return canAttachTo(aboveState, level, pos);
    }

    private boolean canAttachTo(BlockState state, LevelReader level, BlockPos pos){
        return state.isFaceSturdy(level, pos, Direction.DOWN, SupportType.CENTER) || state.is(this);
    }
    private boolean isVineBlock(BlockState state){
        return state.is(this) || state.is(SanguisBlocks.BLOOD_ORANGE_LEAVES);
    }
    private BlockState getHeadGrowthState(RandomSource randomSource){
        if (randomSource.nextInt(5) == 0) return SanguisBlocks.BLOOD_ORANGE_LEAVES.get().defaultBlockState();
        return SanguisBlocks.BLOOD_ORANGE_VINE.get().defaultBlockState().setValue(HEAD, true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HEAD);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HEAD);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0){
            grow(level, random, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        grow(serverLevel, randomSource, blockPos, blockState);
    }

    private void grow(Level level, RandomSource randomSource, BlockPos pos, BlockState state){
        BlockPos headPos;
        if (state.getValue(HEAD)) headPos = pos;
        else headPos = getHeadPos(level, pos, this).orElse(pos);

        BlockState belowState = level.getBlockState(headPos.below());
        if (belowState.isAir()){
            level.setBlock(pos, state.setValue(HEAD, false), Block.UPDATE_ALL);
            level.setBlockAndUpdate(headPos.below(), getHeadGrowthState(randomSource));
        }
    }

    private Optional<BlockPos> getHeadPos(BlockGetter level, BlockPos pos, Block block) {
        return getTopConnectedBlock(level, pos, block, Direction.DOWN, blockState -> blockState.is(this) && blockState.getValue(HEAD));
    }
    public static Optional<BlockPos> getTopConnectedBlock(BlockGetter getter, BlockPos pos, Block block, Direction direction, Predicate<BlockState> predicate) {
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        BlockState blockstate;
        do {
            mutablePos.move(direction);
            blockstate = getter.getBlockState(mutablePos);
        } while(!predicate.test(blockstate) && blockstate.is(block));

        return predicate.test(blockstate) ? Optional.of(mutablePos) : Optional.empty();
    }
}
