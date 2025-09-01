package com.feliscape.sanguis.data.datagen.recipe;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.block.CoffinBlock;
import com.feliscape.sanguis.content.item.BloodBottleItem;
import com.feliscape.sanguis.data.recipe.BloodBottleIngredient;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.registry.SanguisTags;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SanguisRecipeProvider extends RecipeProvider {
    public SanguisRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static final ICustomIngredient NON_EMPTY_BLOOD_BOTTLE_INGREDIENT = BloodBottleIngredient.of();

    private static final Map<DyeColor, Item> WOOL_FROM_COLOR = ImmutableMap.<DyeColor, Item>builder()
            .put(DyeColor.BLACK, Items.BLACK_WOOL)
            .put(DyeColor.BLUE, Items.BLUE_WOOL)
            .put(DyeColor.BROWN, Items.BROWN_WOOL)
            .put(DyeColor.CYAN, Items.CYAN_WOOL)
            .put(DyeColor.GRAY, Items.GRAY_WOOL)
            .put(DyeColor.GREEN, Items.GREEN_WOOL)
            .put(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_WOOL)
            .put(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_WOOL)
            .put(DyeColor.LIME, Items.LIME_WOOL)
            .put(DyeColor.MAGENTA, Items.MAGENTA_WOOL)
            .put(DyeColor.ORANGE, Items.ORANGE_WOOL)
            .put(DyeColor.PINK, Items.PINK_WOOL)
            .put(DyeColor.PURPLE, Items.PURPLE_WOOL)
            .put(DyeColor.RED, Items.RED_WOOL)
            .put(DyeColor.YELLOW, Items.YELLOW_WOOL)
            .put(DyeColor.WHITE, Items.WHITE_WOOL)
            .build();

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<Item> dyes = List.of(
                Items.WHITE_DYE,
                Items.LIGHT_GRAY_DYE,
                Items.GRAY_DYE,
                Items.BLACK_DYE,
                Items.BROWN_DYE,
                Items.RED_DYE,
                Items.ORANGE_DYE,
                Items.YELLOW_DYE,
                Items.LIME_DYE,
                Items.GREEN_DYE,
                Items.CYAN_DYE,
                Items.LIGHT_BLUE_DYE,
                Items.BLUE_DYE,
                Items.PURPLE_DYE,
                Items.MAGENTA_DYE,
                Items.PINK_DYE
        );
        List<Item> coffins = List.of(
                SanguisBlocks.WHITE_COFFIN.asItem(),
                SanguisBlocks.LIGHT_GRAY_COFFIN.asItem(),
                SanguisBlocks.GRAY_COFFIN.asItem(),
                SanguisBlocks.BLACK_COFFIN.asItem(),
                SanguisBlocks.BROWN_COFFIN.asItem(),
                SanguisBlocks.RED_COFFIN.asItem(),
                SanguisBlocks.ORANGE_COFFIN.asItem(),
                SanguisBlocks.YELLOW_COFFIN.asItem(),
                SanguisBlocks.LIME_COFFIN.asItem(),
                SanguisBlocks.GREEN_COFFIN.asItem(),
                SanguisBlocks.CYAN_COFFIN.asItem(),
                SanguisBlocks.LIGHT_BLUE_COFFIN.asItem(),
                SanguisBlocks.BLUE_COFFIN.asItem(),
                SanguisBlocks.PURPLE_COFFIN.asItem(),
                SanguisBlocks.MAGENTA_COFFIN.asItem(),
                SanguisBlocks.PINK_COFFIN.asItem()
        );

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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SanguisItems.PARASOL)
                .pattern("###")
                .pattern("B/B")
                .pattern(" / ")
                .define('/', Items.IRON_INGOT)
                .define('#', Blocks.BLACK_WOOL)
                .define('B', SanguisItems.BAT_WING)
                .unlockedBy(getHasName(Blocks.BLACK_WOOL), has(Blocks.BLACK_WOOL))
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

        colorBlockWithDye(recipeOutput, dyes, coffins, "coffin");
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.WHITE_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.LIGHT_GRAY_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.GRAY_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.BLACK_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.BROWN_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.RED_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.ORANGE_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.YELLOW_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.LIME_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.GREEN_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.CYAN_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.LIGHT_BLUE_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.BLUE_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.PURPLE_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.MAGENTA_COFFIN.get());
        coffinFromPlanksAndWool(recipeOutput, SanguisBlocks.PINK_COFFIN.get());
    }

    private static void coffinFromPlanksAndWool(RecipeOutput recipeOutput, CoffinBlock block) {
        Item wool = WOOL_FROM_COLOR.get(block.getColor());
        if (wool == null){
            Sanguis.LOGGER.debug("[SanguisRecipeProvider] No wool for color {} is defined", block.getColor().getName());
            return;
        }
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block)
                .pattern("XXX")
                .pattern("###")
                .pattern("XXX")
                .define('X', ItemTags.PLANKS)
                .define('#', wool)
                .unlockedBy(getHasName(wool), has(wool))
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
