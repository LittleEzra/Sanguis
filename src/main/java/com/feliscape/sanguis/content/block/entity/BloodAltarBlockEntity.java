package com.feliscape.sanguis.content.block.entity;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.block.BloodAltarBlock;
import com.feliscape.sanguis.content.ritual.BloodRitual;
import com.feliscape.sanguis.registry.SanguisBlockEntityTypes;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BloodAltarBlockEntity extends BlockEntity {
    private NonNullList<ItemStack> items;

    public BloodAltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(SanguisBlockEntityTypes.BLOOD_ALTAR.get(), pos, blockState);
        items = NonNullList.create();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        items.clear();

        ListTag listTag = tag.getList("items", Tag.TAG_COMPOUND);
        for (Tag t : listTag){
            ItemStack.parse(registries, t).ifPresent(items::add);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ListTag listTag = new ListTag();
        for (ItemStack stack : items){
            if (stack.isEmpty()) continue;
            listTag.add(stack.save(registries));
        }
        tag.put("items", listTag);
    }

    public boolean isFilled(){
        return this.getBlockState().getValue(BloodAltarBlock.FILLED);
    }

    public void addItem(ItemStack itemStack){
        items.add(itemStack);
    }

    public void removeItems(){
        Sanguis.LOGGER.debug("Trying to spawn items");
        if (level == null || level.isClientSide()) return;
        Sanguis.LOGGER.debug("Spawning items");
        for (ItemStack itemStack : items){
            Sanguis.LOGGER.debug("Spawning item {}", itemStack);
            ItemEntity item = new ItemEntity(level,
                    getBlockPos().getX() + 0.5D,
                    getBlockPos().getY() + 1.05D,
                    getBlockPos().getZ() + 0.5D,
                    itemStack.copy());
            double theta = Math.TAU * level.random.nextDouble();
            item.setDeltaMovement(
                    Math.cos(theta) * 0.1D,
                    level.random.nextDouble() * 0.2 + 0.05D,
                    Math.sin(theta) * 0.1D
            );
            level.addFreshEntity(item);
        }
        items.clear();
    }

    public UseResult useItem(Player player, ItemStack reagent){
        if (level == null) return UseResult.FAIL;

        var ritual = getRitual(player, reagent);
        if (ritual == null) {
            if (!reagent.isEmpty()) {
                addItem(reagent.copyWithCount(1));
                reagent.consume(1, player);
            }
            return UseResult.RITUAL_SUCCESS;
        }

        List<Player> nearbyPlayers = level.getEntitiesOfClass(
                Player.class,
                new AABB(this.getBlockPos()).inflate(4.0D, 2.5D, 4.0D),
                VampireUtil::isVampire);

        var result = ritual.activate(level, this.getBlockPos(), nearbyPlayers, player);

        if (result.consumesItem()){
            items.clear();
            reagent.consume(1, player);
        }
        level.setBlock(
                this.getBlockPos(),
                this.getBlockState().setValue(BloodAltarBlock.FILLED, false),
                Block.UPDATE_ALL);
        return result.isSuccess() ? UseResult.RITUAL_SUCCESS : UseResult.RITUAL_FAIL;
    }

    public enum UseResult{
        FAIL,
        ADD_ITEM,
        RITUAL_FAIL,
        RITUAL_SUCCESS
    }

    private BloodRitual getRitual(Player player, ItemStack reagent){
        if (this.level == null) return null;

        var allRitualHolders = level.registryAccess().registryOrThrow(SanguisRegistries.Keys.RITUALS).holders();
        var allRituals = allRitualHolders.map(Holder::value).filter(r -> r.verify(
                this.level, this.getBlockPos(), player, this.items, reagent
        ));
        var any = allRituals.findAny();
        return any.orElse(null);
    }

}
