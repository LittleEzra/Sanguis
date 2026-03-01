package com.feliscape.sanguis.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class SanguisRenderTypes {
    public static final Function<ResourceLocation, RenderType> GUI_TRIANGLE = (ResourceLocation texture) -> RenderType.create(
            "gui_triangle",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.TRIANGLES,
            786432,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_GUI_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                    .createCompositeState(false)
    );

    public static RenderType guiTriangle(ResourceLocation texture){
        return GUI_TRIANGLE.apply(texture);
    }
}
