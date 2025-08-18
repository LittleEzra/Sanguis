package com.feliscape.sanguis.data.datagen.recipe;

import com.feliscape.sanguis.content.item.BloodBottleItem;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class SanguisRecipeProvider extends RecipeProvider {
    public SanguisRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SanguisItems.WOODEN_STAKE.get())
                .pattern("/")
                .pattern("#")
                .pattern("#")
                .define('/', Tags.Items.RODS_WOODEN)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SanguisItems.GARLIC_SOLUTION.get())
                .pattern(" G ")
                .pattern("G#G")
                .pattern(" A ")
                .define('#', SanguisItems.VAMPIRE_BLOOD)
                .define('G', SanguisItems.GARLIC)
                .define('A', Items.GOLDEN_APPLE)
                .unlockedBy(getHasName(SanguisItems.VAMPIRE_BLOOD), has(SanguisItems.VAMPIRE_BLOOD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BloodBottleItem.getWithFill(0))
                .pattern("# #")
                .pattern("I#I")
                .define('#', Tags.Items.GLASS_BLOCKS)
                .define('I', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(recipeOutput);
    }
}
