package com.feliscape.sanguis.data.datagen.recipe;

import com.feliscape.sanguis.content.item.BloodBottleItem;
import com.feliscape.sanguis.data.recipe.BloodBottleIngredient;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.concurrent.CompletableFuture;

public class SanguisRecipeProvider extends RecipeProvider {
    public SanguisRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static final ICustomIngredient NON_EMPTY_BLOOD_BOTTLE_INGREDIENT = BloodBottleIngredient.of();

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
                .pattern("S")
                .pattern("#")
                .define('S', SanguisItems.STEEL_INGOT)
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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SanguisItems.BLOOD_SOAKED_COIN)
                .pattern("WGW")
                .pattern(" B ")
                .define('G', Items.GOLD_INGOT)
                .define('W', SanguisItems.BAT_WING)
                .define('B', NON_EMPTY_BLOOD_BOTTLE_INGREDIENT.toVanilla())
                .unlockedBy(getHasName(SanguisItems.BAT_WING), has(SanguisItems.BAT_WING))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SanguisItems.STEEL_BLEND)
                .requires(Items.IRON_INGOT, 2)
                .requires(ItemTags.COALS)
                .requires(SanguisItems.GARLIC)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SanguisItems.DAEMONOLOGIE)
                .requires(SanguisTags.Items.GUIDE_BOOK_MATERIALS)
                .requires(Items.BOOK)
                .unlockedBy("has_guide_book_material", has(SanguisTags.Items.GUIDE_BOOK_MATERIALS))
                .save(recipeOutput);
        ;
        smelting(recipeOutput, SanguisItems.STEEL_INGOT, SanguisItems.STEEL_BLEND);
        blasting(recipeOutput, SanguisItems.STEEL_INGOT, SanguisItems.STEEL_BLEND);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SanguisItems.STEEL_CLEAVER)
                .pattern("SSG")
                .pattern("S/G")
                .pattern("S/ ")
                .define('/', Tags.Items.RODS_WOODEN)
                .define('S', SanguisItems.STEEL_INGOT)
                .define('G', SanguisItems.GARLIC)
                .unlockedBy(getHasName(SanguisItems.STEEL_INGOT), has(SanguisItems.STEEL_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SanguisItems.DIAMOND_CLEAVER)
                .pattern("SDG")
                .pattern("D/G")
                .pattern("S/ ")
                .define('/', Tags.Items.RODS_WOODEN)
                .define('S', SanguisItems.STEEL_INGOT)
                .define('D', Items.DIAMOND)
                .define('G', SanguisItems.GARLIC)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(recipeOutput);
    }

    private static void smelting(RecipeOutput recipeOutput, DeferredItem<Item> result, DeferredItem<Item> ingredient) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), RecipeCategory.BUILDING_BLOCKS, result, 0.1F, 200)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput, getSmeltingRecipeName(result));
    }
    private static void blasting(RecipeOutput recipeOutput, DeferredItem<Item> result, DeferredItem<Item> ingredient) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), RecipeCategory.BUILDING_BLOCKS, result, 0.1F, 100)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput, getBlastingRecipeName(result));
    }
}
