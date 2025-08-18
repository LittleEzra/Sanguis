package com.feliscape.sanguis.client;

import com.feliscape.sanguis.Sanguis;
import com.google.common.collect.Sets;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.Set;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class SanguisModelLayers {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();

    public static final ModelLayerLocation VAMPIRE = register("vampire");
    public static final ModelLayerLocation VAMPIRE_ARMOR_OUTER = registerOuterArmor("vampire");
    public static final ModelLayerLocation VAMPIRE_ARMOR_INNER = registerInnerArmor("vampire");

    private static ModelLayerLocation register(String path) {
        return register(path, "main");
    }

    private static ModelLayerLocation registerInnerArmor(String path) {
        return register(path, "inner_armor");
    }
    private static ModelLayerLocation registerOuterArmor(String path) {
        return register(path, "outer_armor");
    }


    private static ModelLayerLocation register(String path, String model) {
        ModelLayerLocation modellayerlocation = createLocation(path, model);
        if (!ALL_MODELS.add(modellayerlocation)) {
            throw new IllegalStateException("Duplicate registration for " + modellayerlocation);
        } else {
            return modellayerlocation;
        }
    }

    private static ModelLayerLocation createLocation(String path, String model) {
        return new ModelLayerLocation(Sanguis.location(path), model);
    }

    public static Stream<ModelLayerLocation> getKnownLocations() {
        return ALL_MODELS.stream();
    }


    public static final CubeDeformation OUTER_ARMOR_DEFORMATION = new CubeDeformation(1.0F);
    public static final CubeDeformation INNER_ARMOR_DEFORMATION = new CubeDeformation(0.5F);

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        LayerDefinition outerArmor = LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_ARMOR_DEFORMATION), 64, 32);
        LayerDefinition innerArmor = LayerDefinition.create(HumanoidArmorModel.createBodyLayer(INNER_ARMOR_DEFORMATION), 64, 32);
        event.registerLayerDefinition(VAMPIRE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
        event.registerLayerDefinition(VAMPIRE_ARMOR_OUTER, () -> outerArmor);
        event.registerLayerDefinition(VAMPIRE_ARMOR_INNER, () -> innerArmor);
    }
}
