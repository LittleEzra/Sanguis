package com.feliscape.sanguis.content.block;

import com.feliscape.sanguis.util.VampireUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CoffinBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<CoffinBlock> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            DyeColor.CODEC.fieldOf("color").forGetter(CoffinBlock::getColor), propertiesCodec())
            .apply(inst, CoffinBlock::new)
    );
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    private final DyeColor color;

    protected static final VoxelShape BASE = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE,
            Block.box(2.0D, 4.0D, 14.0D, 14.0D, 16.0D, 16.0D), // End
            Block.box(0.0D, 4.0D, 0.0D, 2.0D, 16.0D, 16.0D), // Right
            Block.box(14.0D, 4.0D, 0.0D, 16.0D, 16.0D, 16.0D)  // Left
    );
    protected static final VoxelShape NORTH_SHAPE = Shapes.or(BASE,
            Block.box(2.0D, 4.0D, 0.0D, 14.0D, 16.0D, 2.0D), // End
            Block.box(0.0D, 4.0D, 0.0D, 2.0D, 16.0D, 16.0D), // Right
            Block.box(14.0D, 4.0D, 0.0D, 16.0D, 16.0D, 16.0D)  // Left
    );
    protected static final VoxelShape WEST_SHAPE = Shapes.or(BASE,
            Block.box(0.0D, 4.0D, 2.0D, 2.0D, 16.0D, 14.0D), // End
            Block.box(0.0D, 4.0D, 0.0D, 16.0D, 16.0D, 2.0D), // Right
            Block.box(0.0D, 4.0D, 14.0D, 16.0D, 16.0D, 16.0D)  // Left
    );
    protected static final VoxelShape EAST_SHAPE = Shapes.or(BASE,
            Block.box(14.0D, 4.0D, 2.0D, 16.0D, 16.0D, 14.0D), // End
            Block.box(0.0D, 4.0D, 0.0D, 16.0D, 16.0D, 2.0D), // Right
            Block.box(0.0D, 4.0D, 14.0D, 16.0D, 16.0D, 16.0D)  // Left
    );

    @Override
    protected MapCodec<? extends CoffinBlock> codec() {
        return CODEC;
    }

    public CoffinBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(OCCUPIED, Boolean.FALSE));
    }

    @Nullable
    public static Direction getBedOrientation(BlockGetter level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        return blockstate.getBlock() instanceof CoffinBlock ? blockstate.getValue(FACING) : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!VampireUtil.isVampire(player)) return InteractionResult.PASS;

        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (state.getValue(PART) != BedPart.HEAD) {
                pos = pos.relative(state.getValue(FACING));
                state = level.getBlockState(pos);
                if (!state.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn(level)) {
                level.removeBlock(pos, false);
                BlockPos blockpos = pos.relative(state.getValue(FACING).getOpposite());
                if (level.getBlockState(blockpos).is(this)) {
                    level.removeBlock(blockpos, false);
                }

                Vec3 vec3 = pos.getCenter();
                level.explode(null, level.damageSources().badRespawnPointExplosion(vec3), null, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return InteractionResult.SUCCESS;
            } else if (state.getValue(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(level, pos)) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return InteractionResult.SUCCESS;
            } else {
                player.startSleepInBed(pos).ifLeft(problem -> {
                    if (problem.getMessage() != null) {
                        player.displayClientMessage(problem.getMessage(), true);
                    }
                });
                return InteractionResult.SUCCESS;
            }
        }
    }
    public static boolean canSetSpawn(Level level) {
        return level.dimensionType().bedWorks();
    }

    private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
        List<Villager> list = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            list.get(0).stopSleeping();
            return true;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockPos blockpos = pos.relative(state.getValue(FACING));
        level.setBlock(blockpos, state.setValue(PART, BedPart.HEAD), 3);
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            return facingState.is(this) && facingState.getValue(PART) != state.getValue(PART)
                    ? state.setValue(OCCUPIED, facingState.getValue(OCCUPIED))
                    : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
    }

    private static Direction getNeighbourDirection(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BedPart bedpart = state.getValue(PART);
            if (bedpart == BedPart.FOOT) {
                BlockPos blockpos = pos.relative(getNeighbourDirection(bedpart, state.getValue(FACING)));
                BlockState blockstate = level.getBlockState(blockpos);
                if (blockstate.is(this) && blockstate.getValue(PART) == BedPart.HEAD) {
                    level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos blockpos = context.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction);
        Level level = context.getLevel();
        return level.getBlockState(blockpos1).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(blockpos1)
                ? this.defaultBlockState().setValue(FACING, direction)
                : null;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = getConnectedDirection(state).getOpposite();
        switch (direction) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return EAST_SHAPE;
        }
    }

    public static Direction getConnectedDirection(BlockState state) {
        Direction direction = state.getValue(FACING);
        return state.getValue(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
    }

    @Override
    protected long getSeed(BlockState state, BlockPos pos) {
        BlockPos blockpos = pos.relative(state.getValue(FACING), state.getValue(PART) == BedPart.HEAD ? 0 : 1);
        return Mth.getSeed(blockpos.getX(), pos.getY(), blockpos.getZ());
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, OCCUPIED, FACING);
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter level, BlockPos pos, LivingEntity sleeper) {
        return VampireUtil.isVampire(sleeper);
    }
}
