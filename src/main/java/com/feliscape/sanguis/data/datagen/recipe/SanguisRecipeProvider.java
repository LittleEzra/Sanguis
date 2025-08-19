package com.feliscape.sanguis.data.datagen.recipe;

import com.feliscape.sanguis.content.item.BloodBottleItem;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SanguisItems.WOODEN_STAKE.get())
                .pattern("/")
                .pattern("#")
                .pattern("#")
                .define('/', Tags.Items.RODS_WOODEN)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SanguisItems.REINFORCED_STAKE.get())
                .pattern("/")
                .pattern("I")
                .pattern("#")
                .define('I', Items.IRON_INGOT)
                .define('/', Tags.Items.RODS_WOODEN)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SanguisItems.GOLDEN_QUARREL.get())
                .pattern("#")
                .pattern("/")
                .pattern("F")
                .define('#', Items.GOLD_NUGGET)
                .define('/', Tags.Items.RODS_WOODEN)
                .define('F', Items.FEATHER)
                .unlockedBy("has_gold_nugget", has(Items.GOLD_NUGGET))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SanguisItems.SYRINGE)
                .pattern("#")
                .pattern("#")
                .pattern("I")
                .define('#', Tags.Items.GLASS_BLOCKS)
                .define('I', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SanguisItems.GARLIC_INJECTION)
                .requires(SanguisItems.SYRINGE)
                .requires(SanguisItems.GARLIC, 3)
                .requires(Items.GOLDEN_APPLE)
                .unlockedBy(getHasName(SanguisItems.SYRINGE), has(SanguisItems.SYRINGE))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SanguisItems.ACID_INJECTION)
                .requires(SanguisItems.SYRINGE)
                .requires(Items.APPLE, 2)
                .requires(Items.SLIME_BALL)
                .requires(SanguisItems.VAMPIRE_BLOOD)
                .unlockedBy(getHasName(SanguisItems.SYRINGE), has(SanguisItems.SYRINGE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BloodBottleItem.getWithFill(0))
                .pattern("# #")
                .pattern("I#I")
                .define('#', Tags.Items.GLASS_BLOCKS)
                .define('I', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SanguisBlocks.GARLIC_STRING, 4)
                .pattern("GSG")
                .pattern("GSG")
                .pattern("GSG")
                .define('S', Items.STRING)
                .define('G', SanguisItems.GARLIC_FLOWER)
                .unlockedBy(getHasName(SanguisItems.GARLIC_FLOWER), has(SanguisItems.GARLIC_FLOWER))
                .save(recipeOutput);
    }
}
