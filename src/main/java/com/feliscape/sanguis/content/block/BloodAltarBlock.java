package com.feliscape.sanguis.content.block;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.block.entity.BloodAltarBlockEntity;
import com.feliscape.sanguis.content.ritual.BloodRitual;
import com.feliscape.sanguis.registry.SanguisDataComponents;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.util.VampireUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodAltarBlock extends BaseEntityBlock {
    private static final MapCodec<BloodAltarBlock> CODEC = simpleCodec(BloodAltarBlock::new);
    public static BooleanProperty FILLED = BooleanProperty.create("filled");

    public static VoxelShape SHAPE = Shapes.or(
            Block.box(6, 0, 6, 10, 12, 10),
            Block.box(0, 12, 0, 16, 16, 16)
    );

    public BloodAltarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FILLED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        boolean filled = state.getValue(FILLED);
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BloodAltarBlockEntity bloodAltar){
            bloodAltar.removeItems();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack,
                                              BlockState state,
                                              Level level,
                                              BlockPos pos,
                                              Player player,
                                              InteractionHand hand,
                                              BlockHitResult hitResult) {
        boolean filled = state.getValue(FILLED);
        if (filled){
            if (stack.is(SanguisItems.BLOOD_BOTTLE)){
                int blood = stack.getOrDefault(SanguisDataComponents.BLOOD, 0);
                int maxBlood = stack.getOrDefault(SanguisDataComponents.MAX_BLOOD, 0);
                if (blood < maxBlood){
                    stack.set(SanguisDataComponents.BLOOD, blood + 1);
                } else if (!player.hasInfiniteMaterials()){
                    return ItemInteractionResult.FAIL;
                }
                level.setBlock(pos, state.setValue(FILLED, false), Block.UPDATE_ALL);
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            } else{
                var blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BloodAltarBlockEntity bloodAltar){
                    if (stack.isEmpty()){
                        bloodAltar.removeItems();
                    } else{
                        bloodAltar.useItem(player, stack);
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        } else{
            if (stack.is(SanguisItems.BLOOD_BOTTLE)){
                int blood = stack.getOrDefault(SanguisDataComponents.BLOOD, 0);
                if (blood > 0){
                    if (!player.hasInfiniteMaterials()) stack.set(SanguisDataComponents.BLOOD, blood - 1);
                    level.setBlock(pos, state.setValue(FILLED, true), Block.UPDATE_ALL);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
                return ItemInteractionResult.FAIL;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
    private ItemInteractionResult commenceRitual(BloodRitual ritual,
                                                 Level level,
                                                 BlockPos pos,
                                                 BlockState state,
                                                 Player player,
                                                 ItemStack stack){
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(4.0D, 2.5D, 4.0D),
                p -> VampireUtil.isVampire(p));
        var result = ritual.activate(level, pos, nearbyPlayers, player);

        if (result.consumesItem()){
            stack.consume(1, player);
            level.setBlock(pos, state.setValue(FILLED, false), Block.UPDATE_ALL);
        }
        return result.isSuccess() ? ItemInteractionResult.sidedSuccess(level.isClientSide()) : ItemInteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BloodAltarBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
