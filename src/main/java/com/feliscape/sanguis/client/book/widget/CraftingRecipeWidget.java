package com.feliscape.sanguis.client.book.widget;

import com.feliscape.sanguis.Sanguis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CraftingRecipeWidget extends BookWidget{
    public static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation CRAFTING_GRID = Sanguis.location("textures/gui/guide_book/crafting_grid.png");
    private static final ResourceLocation CRAFTING_GRID_2X2 = Sanguis.location("textures/gui/guide_book/crafting_grid_2x2.png");

    public static final MapCodec<CraftingRecipeWidget> CODEC = RecordCodecBuilder.mapCodec(
            inst -> codecStart(inst).and(
                    ResourceLocation.CODEC.fieldOf("recipe").forGetter(CraftingRecipeWidget::getRecipeId)
    ).apply(inst, CraftingRecipeWidget::new));

    private ResourceLocation recipeId;

    public CraftingRecipeWidget(int displayPage, WidgetType<?> type, int x, int y, ResourceLocation recipeId) {
        super(displayPage, type, x, y);
        this.recipeId = recipeId;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        Recipe<?> recipe = getRecipe(this.getRecipeId());
        x += this.getX();
        y += this.getY();

        if (recipe == null || Minecraft.getInstance().level == null) return;

        float playerTicks = Minecraft.getInstance().player.tickCount;

        if (recipe instanceof CraftingRecipe craftingRecipe){
            if (craftingRecipe.canCraftInDimensions(2, 2)){
                guiGraphics.blit(CRAFTING_GRID_2X2,
                        x, y, 0, 0, 0,
                        116, 54, 116, 54);
                NonNullList<Ingredient> ingredients = craftingRecipe.getIngredients();

                int width = 2;
                int height = 2;
                if (craftingRecipe instanceof ShapedRecipe shaped){
                    width = shaped.getWidth();
                    height = shaped.getHeight();
                }

                for (int i = 0; i < ingredients.size(); i++){
                    Ingredient ingredient = ingredients.get(i);
                    if (ingredient.isEmpty()) continue;

                    ItemStack itemStack = ingredient.getItems()[(int)((playerTicks / 20F) % ingredient.getItems().length)];

                    guiGraphics.renderItem(itemStack, x + 9 + (i % width) * 17, y + 10 + Mth.floor(i / (float)height) * 17 + (2 - height) * 17);
                }
                guiGraphics.renderItem(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()), x + 88, y + 19);
            } else{
                guiGraphics.blit(CRAFTING_GRID,
                        x, y, 0, 0, 0,
                        116, 54, 116, 54);

                NonNullList<Ingredient> ingredients = craftingRecipe.getIngredients();

                int width = 3;
                int height = 3;
                if (craftingRecipe instanceof ShapedRecipe shaped){
                    width = shaped.getWidth();
                    height = shaped.getHeight();
                }

                for (int i = 0; i < ingredients.size(); i++){
                    Ingredient ingredient = ingredients.get(i);
                    if (ingredient.isEmpty()) continue;

                    ItemStack itemStack = ingredient.getItems()[(int)((playerTicks / 20F) % ingredient.getItems().length)];

                    guiGraphics.renderItem(itemStack, x + 2 + (i % width) * 17, y + 2 + Mth.floor(i / (float)width) * 17 + (3 - height) * 17);
                }
                guiGraphics.renderItem(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()), x + 95, y + 19);
            }
        }
    }

    public ResourceLocation getRecipeId() {
        return recipeId;
    }

    private Recipe<?> getRecipe(ResourceLocation location){
        if (Minecraft.getInstance().level == null) return null;

        try{
            RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
            Optional<RecipeHolder<?>> recipeOptional = manager.byKey(recipeId);
            if (recipeOptional.isPresent()){
                return recipeOptional.get().value();
            }
        } catch (Exception e){
            LOGGER.error("Could not find recipe {} due to an unexpected error", location, e);
        }
        return null;
    }
}
