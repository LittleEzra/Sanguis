package com.feliscape.sanguis.client.registry;

import com.feliscape.sanguis.Sanguis;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

public class ClientRitualStructures implements ResourceManagerReloadListener {
    private final Map<ResourceLocation, StructureTemplate> structures = Maps.newHashMap();

    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.structures.clear();
    }

    public StructureTemplate get(ResourceLocation location){
        return structures.computeIfAbsent(location, ClientRitualStructures::loadStructure);
    }

    private static StructureTemplate loadStructure(ResourceLocation location) {
        return loadStructure(Minecraft.getInstance().getResourceManager(), location);
    }

    private static StructureTemplate loadStructure(ResourceManager resourceManager, ResourceLocation location) {
        String namespace = location.getNamespace();
        String path = "ritual/" + location.getPath() + ".nbt";
        ResourceLocation location1 = ResourceLocation.fromNamespaceAndPath(namespace, path);

        Optional<Resource> optionalResource = resourceManager.getResource(location1);
        if (optionalResource.isEmpty()) {
            Sanguis.LOGGER.error("Ritual schematic missing: {}", location1);

            return new StructureTemplate();
        }

        Resource resource = optionalResource.get();
        try (InputStream inputStream = resource.open()) {
            return loadStructure(inputStream);
        } catch (IOException e) {
            Sanguis.LOGGER.error("Failed to read ritual schematic: {}", location1, e);
        }

        return new StructureTemplate();
    }
    private static StructureTemplate loadStructure(InputStream resourceStream) throws IOException {
        StructureTemplate t = new StructureTemplate();
        DataInputStream stream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(resourceStream)));
        CompoundTag nbt = NbtIo.read(stream, NbtAccounter.create(0x20000000L));
        //t.load(Minecraft.getInstance().level.holderLookup(Registries.BLOCK), nbt);
        t.load(BuiltInRegistries.BLOCK.asLookup(), nbt);
        return t;
    }
}
