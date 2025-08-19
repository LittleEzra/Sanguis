package com.feliscape.sanguis.mixin.client;

import com.feliscape.sanguis.client.model.SanguisAdditionalModels;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow @Final private ItemModelShaper itemModelShaper;

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    public void overrideCrossbowModel(ItemStack stack, Level level, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir){
        if (stack.is(Items.CROSSBOW)) {
            ChargedProjectiles projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
            if (projectiles != null && projectiles.contains(SanguisItems.GOLDEN_QUARREL.get())){
                cir.setReturnValue(this.itemModelShaper.getModelManager().getModel(SanguisAdditionalModels.CROSSBOW_GOLDEN_QUARREL));
            }
        }
    }
}
