package com.feliscape.sanguis.content.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

import java.util.ArrayList;
import java.util.List;

public abstract class ReagentListRitual extends BloodRitual{
    private final List<ItemStack> ingredients;
    private final ItemStack reagent;

    public ReagentListRitual(List<ItemStack> ingredients, ItemStack reagent) {
        this.ingredients = ingredients;
        this.reagent = reagent;
    }

    @Override
    protected boolean verifyItem(Player player, ItemStack itemStack) {
        return ItemStack.isSameItem(itemStack, reagent);
    }

    @Override
    public boolean verify(Level level, BlockPos pos, Player player, List<ItemStack> itemStacks, ItemStack reagent) {
        if (itemStacks.size() != ingredients.size()) return false;
        var shoppingList = new ArrayList<>(ingredients);
        for (ItemStack itemStack : itemStacks){
            if (shoppingList.isEmpty()) break;
            for (ItemStack requiredStack : shoppingList){
                if (ItemStack.isSameItem(itemStack, requiredStack)){
                    shoppingList.remove(requiredStack);
                    break;
                }
            }
        }
        if (!shoppingList.isEmpty()) return false;
        return super.verify(level, pos, player, itemStacks, reagent);
    }
}
