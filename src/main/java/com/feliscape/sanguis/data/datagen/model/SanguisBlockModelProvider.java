package com.feliscape.sanguis.data.datagen.model;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.block.CoffinBlock;
import com.feliscape.sanguis.content.block.GarlicCropBlock;
import com.feliscape.sanguis.registry.SanguisBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class SanguisBlockModelProvider extends BlockStateProvider {
    public SanguisBlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Sanguis.MOD_ID, exFileHelper);
    }

    private static final int DEFAULT_ANGLE_OFFSET = 180;

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(SanguisBlocks.QUEST_BOARD.get(), models().cubeColumn(
                name(SanguisBlocks.QUEST_BOARD.get()),
                blockTexture(SanguisBlocks.QUEST_BOARD.get()).withSuffix("_side"),
                ResourceLocation.withDefaultNamespace("block/stripped_oak_log_top")));
        simpleCropBlock(SanguisBlocks.GARLIC.get());
        crossBlockWithRenderType(SanguisBlocks.WILD_GARLIC.get(), "cutout");
        simpleBlock(SanguisBlocks.GARLIC_STRING.get(), models().getExistingFile(Sanguis.location("block/garlic_string")));
        
        coffin(SanguisBlocks.WHITE_COFFIN.get());
        coffin(SanguisBlocks.LIGHT_GRAY_COFFIN.get());
        coffin(SanguisBlocks.GRAY_COFFIN.get());
        coffin(SanguisBlocks.BLACK_COFFIN.get());
        coffin(SanguisBlocks.BROWN_COFFIN.get());
        coffin(SanguisBlocks.RED_COFFIN.get());
        coffin(SanguisBlocks.ORANGE_COFFIN.get());
        coffin(SanguisBlocks.YELLOW_COFFIN.get());
        coffin(SanguisBlocks.LIME_COFFIN.get());
        coffin(SanguisBlocks.GREEN_COFFIN.get());
        coffin(SanguisBlocks.CYAN_COFFIN.get());
        coffin(SanguisBlocks.LIGHT_BLUE_COFFIN.get());
        coffin(SanguisBlocks.BLUE_COFFIN.get());
        coffin(SanguisBlocks.PURPLE_COFFIN.get());
        coffin(SanguisBlocks.MAGENTA_COFFIN.get());
        coffin(SanguisBlocks.PINK_COFFIN.get());
    }

    private void coffin(CoffinBlock block){
        DyeColor color = block.getColor();

        ResourceLocation velvetLocation = Sanguis.location("block/" + color.getName() + "_velvet");
        ModelFile head = models().withExistingParent(name(block) + "_head", Sanguis.location("block/template_coffin_head"))
                .texture("velvet", velvetLocation);
        ModelFile foot = models().withExistingParent(name(block) + "_foot", Sanguis.location("block/template_coffin_foot"))
                .texture("velvet", velvetLocation);
        ModelFile headSealed = models().withExistingParent(name(block) + "_head_sealed", Sanguis.location("block/template_coffin_head_sealed"))
                .texture("velvet", velvetLocation);
        ModelFile footSealed = models().withExistingParent(name(block) + "_foot_sealed", Sanguis.location("block/template_coffin_foot_sealed"))
                .texture("velvet", velvetLocation);
        ModelFile inventory = models().withExistingParent(name(block) + "_inventory", Sanguis.location("block/template_coffin_inventory"))
                .texture("velvet", velvetLocation);

        simpleBlockItem(block, inventory);
        getVariantBuilder(block).forAllStatesExcept(
                state -> {
                    BedPart part = state.getValue(CoffinBlock.PART);
                    Direction rotation = state.getValue(HorizontalDirectionalBlock.FACING);
                    Boolean sealed = state.getValue(CoffinBlock.OCCUPIED);

                    return ConfiguredModel.builder()
                            .modelFile(sealed ? (part == BedPart.HEAD ? headSealed : footSealed) : (part == BedPart.HEAD ? head : foot))
                            .rotationY(((int) rotation.toYRot()) + DEFAULT_ANGLE_OFFSET).build();
                });
    }

    private void simpleCropBlock(CropBlock block) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    int age = block.getAge(state);
                    return ConfiguredModel.builder()
                            .modelFile(models().crop(name(block) + "_" + age, extend(blockTexture(block), "_" + age))
                                    .renderType("cutout"))
                            .build();
                });
    }

    private void blockWithItem(Supplier<? extends Block> block){
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
    private void blockWithItem(Supplier<? extends Block> block, String renderType){
        simpleBlockWithItem(block.get(), models().cubeAll(this.name(block.get()), this.blockTexture(block.get())).renderType(renderType));
    }

    private void leavesBlock(Supplier<? extends Block> block, String renderType){
        ModelFile model = models().withExistingParent(getLocation(block).getPath(), "minecraft:block/leaves")
                .texture("all", blockTexture(block.get())).renderType(renderType);
        getVariantBuilder(block.get())
                .partialState().setModels( new ConfiguredModel(model));
        simpleBlockItem(block.get(), model);
    }

    public void crossBlockWithRenderType(Block block, String renderType) {
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(models().cross(name(block), blockTexture(block)).renderType(renderType)));
    }

    private String name(Block block) {
        return this.getLocation(block).getPath();
    }

    private ResourceLocation getLocation(Supplier<? extends Block> block){
        return BuiltInRegistries.BLOCK.getKey(block.get());
    }
    private ResourceLocation getLocation(Block block){
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private ResourceLocation extend(ResourceLocation location, String suffix) {
        String namespace = location.getNamespace();
        String path = location.getPath();
        return ResourceLocation.fromNamespaceAndPath(namespace, path + suffix);
    }
}
