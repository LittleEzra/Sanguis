package com.feliscape.sanguis.mixin.client;

import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @Inject(method = "renderEntityInInventoryFollowsAngle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;renderEntityInInventory(Lnet/minecraft/client/gui/GuiGraphics;FFFLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/world/entity/LivingEntity;)V"))
    private static void renderBatWithRotation(GuiGraphics guiGraphics,
                                              int x1, int y1,
                                              int x2, int y2,
                                              int scale, float yOffset,
                                              float angleXComponent, float angleYComponent,
                                              LivingEntity entity, CallbackInfo ci){
        if (VampireUtil.isBat(entity)){
            entity.getData(VampireData.type()).setBatRotation(true);
        }
    }
    @Inject(method = "renderEntityInInventoryFollowsAngle", at = @At(value = "TAIL"))
    private static void resetBatRotation(GuiGraphics guiGraphics,
                                              int x1, int y1,
                                              int x2, int y2,
                                              int scale, float yOffset,
                                              float angleXComponent, float angleYComponent,
                                              LivingEntity entity, CallbackInfo ci){
        if (VampireUtil.isBat(entity)){
            entity.getData(VampireData.type()).setBatRotation(false);
        }
    }
}
