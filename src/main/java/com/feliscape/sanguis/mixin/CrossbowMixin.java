package com.feliscape.sanguis.mixin;

import com.feliscape.sanguis.registry.SanguisTags;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(CrossbowItem.class)
public class CrossbowMixin {
    @Unique
    private static final Predicate<ItemStack> QUARRELS = itemStack -> itemStack.is(SanguisTags.Items.QUARRELS);

    @ModifyReturnValue(method = "getSupportedHeldProjectiles", at = @At("TAIL"))
    public Predicate<ItemStack> injectQuarrelsToHeld(Predicate<ItemStack> original){
        return original.or(QUARRELS);
    }
    @ModifyReturnValue(method = "getAllSupportedProjectiles", at = @At("TAIL"))
    public Predicate<ItemStack> injectQuarrelsToAll(Predicate<ItemStack> original){
        return original.or(QUARRELS);
    }
}
