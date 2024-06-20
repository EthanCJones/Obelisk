/* ==========================================================
 * Author : Ethan Jones
 * Date   : 18/06/2024
 * TODO   : Nothing
 * Uses   : Allows manipulation of methods in the living
 * entity class. Mainly used to stop pushing
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.module.ModuleAPI;
import com.ethancjones.obelisk.module.modules.AntiPush;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    @Inject(method = "isPushable", at = @At("RETURN"), cancellable = true)
    public void ias$isPushable(CallbackInfoReturnable<Boolean> cir)
    {
        if (ModuleAPI.antiPush.isEnabled())
        {
            if (ModuleAPI.antiPush.entities.getValue())
            {
                cir.setReturnValue(false);
            }
        }
    }
}
