package com.feliscape.sanguis.mixin.client;

import com.feliscape.sanguis.SanguisClient;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.content.event.WerebatHandler;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @SuppressWarnings("unchecked")
    @Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
    public <T extends Entity> void replacePlayerWithMyceling(T entity, CallbackInfoReturnable<EntityRenderer<? super T>> cir) {
        if (entity instanceof AbstractClientPlayer player){
            if (WerebatHandler.isWerebat(player)){
                PlayerSkin.Model model = player.getSkin().model();
                cir.setReturnValue((EntityRenderer<? super T>) SanguisClient.reloadListeners().getUniqueEntityRenderers().getWerebatRenderer(model == PlayerSkin.Model.SLIM));
            }
        }
    }
}
