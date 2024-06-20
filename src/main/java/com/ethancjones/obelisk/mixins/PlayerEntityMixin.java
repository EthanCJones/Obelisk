/* ==========================================================
 * Author : Ethan Jones
 * Date   : 18/06/2024
 * TODO   : Nothing
 * Uses   :
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.module.ModuleAPI;
import com.ethancjones.obelisk.module.modules.AntiPush;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin
{
    @Inject(method = "isPushedByFluids", at = @At("RETURN"), cancellable = true)
    public void ias$isPushedByFluids(CallbackInfoReturnable<Boolean> cir)
    {
        if (ModuleAPI.antiPush.isEnabled())
        {
            if (ModuleAPI.antiPush.water.getValue())
            {
                cir.setReturnValue(false);
            }
        }
    }
}
