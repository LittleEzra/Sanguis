package com.feliscape.sanguis.data.datagen.model;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.SanguisItemProperties;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisDataComponents;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class SanguisItemModelProvider extends ItemModelProvider {
    public SanguisItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Sanguis.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.simpleItem(SanguisItems.BLOODY_FANG);
        this.simpleItem(SanguisItems.VAMPIRE_BLOOD);
        this.simpleItem(SanguisItems.ACTIVATED_VAMPIRE_BLOOD);
        this.bloodBottleItem(SanguisItems.BLOOD_BOTTLE);

        this.simpleItem(SanguisItems.GARLIC_SOLUTION);
        this.handheldItem(SanguisItems.WOODEN_STAKE);

        this.simpleItem(SanguisItems.GARLIC);
        this.simpleItem(SanguisItems.GARLIC_FLOWER);
        this.spawnEggItem(SanguisItems.VAMPIRE_SPAWN_EGG.get());

        this.blockItemSprite(SanguisBlocks.GARLIC_STRING);
    }
    
    private ItemModelBuilder bloodBottleItem(Supplier<? extends Item> item){
        int maxBlood = item.get().getDefaultInstance().getOrDefault(SanguisDataComponents.MAX_BLOOD, 0);
        if (maxBlood == 0){
            throw new IllegalArgumentException("Item %s has no default sanguis:max_blood component".formatted(getLocation(item)));
        }

        var builder = simpleItem(item);

        for (int i = 1; i <= maxBlood; i++){
            ResourceLocation location = getLocation(item).withSuffix("_" + i);
            simpleItem(location);

            builder.override()
                    .predicate(SanguisItemProperties.BLOOD, (float) i / maxBlood)
                    .model(new ModelFile.UncheckedModelFile(location.withPrefix("item/")))
                    .end()
            ;
        }
        return builder;
    }

    private ItemModelBuilder simpleItem(Supplier<? extends Item> item){
        return withExistingParent(getLocation(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", Sanguis.location("item/" + getLocation(item.get()).getPath()));
    }
    private ItemModelBuilder simpleItem(ResourceLocation item){
        return withExistingParent(item.toString(),
                ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }
    private ItemModelBuilder simpleDoubleLayered(Supplier<? extends Item> item){
        return withExistingParent(getLocation(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", Sanguis.location("item/" + getLocation(item.get()).getPath()))
                .texture("layer1", Sanguis.location("item/" + getLocation(item.get()).getPath() + "_overlay"))
                ;
    }
    private ItemModelBuilder handheldItem(Supplier<? extends Item> item){
        return withExistingParent(getLocation(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/handheld"))
                .texture("layer0", Sanguis.location("item/" + getLocation(item.get()).getPath()));
    }
    private ItemModelBuilder rotatedHandheldItem(Supplier<? extends Item> item){
        return withExistingParent(getLocation(item.get()).getPath(),
                Sanguis.location("item/rotated_handheld"))
                .texture("layer0", Sanguis.location("item/" + getLocation(item.get()).getPath()));
    }
    private ItemModelBuilder itemWithBlockTexture(Supplier<? extends Item> item){
        return withExistingParent(getLocation(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", Sanguis.location("block/" + getLocation(item.get()).getPath()));
    }

    public void manualBlockItem(Supplier<? extends Block> block) {
        this.withExistingParent(Sanguis.MOD_ID + ":" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath(),
                modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(Supplier<? extends Block> block) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(),
                modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(Supplier<? extends Block> block, Supplier<? extends Block> baseBlock) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  Sanguis.location("block/" + BuiltInRegistries.BLOCK.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(Supplier<? extends Block> block, Supplier<? extends Block> baseBlock) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  Sanguis.location("block/" + BuiltInRegistries.BLOCK.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(Supplier<? extends Block> block, Supplier<? extends Block> baseBlock) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  Sanguis.location("block/" + BuiltInRegistries.BLOCK.getKey(baseBlock.get()).getPath()));
    }
    public void minecraftBasedWallItem(Supplier<? extends Block> block, Block baseBlock) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  ResourceLocation.withDefaultNamespace("block/" + BuiltInRegistries.BLOCK.getKey(baseBlock).getPath()));
    }

    private ItemModelBuilder blockItemSprite(Supplier<? extends Block> block) { // Uses a block instead of item with a unique item texture (Example: Doors or Lanterns)
        return withExistingParent(getBlockLocation(block.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                Sanguis.location("item/" + getBlockLocation(block.get()).getPath()));
    }
    private ItemModelBuilder blockItemSprite(Supplier<? extends Block> block, ResourceLocation texture) { // Uses a block instead of item with a unique item texture (Example: Doors or Lanterns)
        return withExistingParent(getBlockLocation(block.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0", texture);
    }
    private ItemModelBuilder blockItemSpriteLayered(Supplier<? extends Block> block) { // Uses a block instead of item with a unique item texture (Example: Doors or Lanterns)
        return withExistingParent(getBlockLocation(block.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", Sanguis.location("item/" + getBlockLocation(block.get()).getPath()))
                .texture("layer1", Sanguis.location("item/" + getBlockLocation(block.get()).getPath()) + "_overlay")
                ;
    }
    private ItemModelBuilder generatedBlockItem(Supplier<? extends Block> block) { // Uses the texture from textures/block (Example: Saplings or Torches)
        return withExistingParent(getBlockLocation(block.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                Sanguis.location("block/" + getBlockLocation(block.get()).getPath()));
    }
    private ItemModelBuilder generatedBlockItem(Supplier<? extends Block> block, ResourceLocation sprite) { // Uses the texture from textures/block (Example: Saplings or Torches)
        return withExistingParent(getBlockLocation(block.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                sprite);
    }

    private ResourceLocation getLocation(Supplier<? extends Item> item){
        return BuiltInRegistries.ITEM.getKey(item.get());
    }
    private ResourceLocation getLocation(Item item){
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private ResourceLocation getBlockLocation(Supplier<? extends Block> block){
        return BuiltInRegistries.BLOCK.getKey(block.get());
    }
    private ResourceLocation getBlockLocation(Block block){
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private ResourceLocation blockTexture(Supplier<? extends Block> block){
        ResourceLocation location = getBlockLocation(block);
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "block/" + location.getPath());
    }
    private ResourceLocation blockTexture(Block block){
        ResourceLocation location = getBlockLocation(block);
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "block/" + location.getPath());
    }

    private ResourceLocation getBlockItemSpriteLocation(Supplier<? extends Block> block){
        return Sanguis.location("item/" + getBlockLocation(block.get()).getPath());
    }
}
