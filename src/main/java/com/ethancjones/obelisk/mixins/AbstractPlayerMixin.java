/* ==========================================================
 * Author : Ethan Jones
 * Date   : 02/06/2024
 * TODO   : Nothing
 * Uses   :
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.module.ModuleAPI;
import com.ethancjones.obelisk.module.modules.Freecam;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractPlayerMixin
{
    @Inject(method = "isSpectator", at = @At("RETURN"), cancellable = true)
    public void ias$isSpectator(CallbackInfoReturnable<Boolean> cir)
    {
        if (ModuleAPI.getModuleByClass(Freecam.class).isEnabled())
        {
            cir.setReturnValue(true);
        }
        else
        {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}
