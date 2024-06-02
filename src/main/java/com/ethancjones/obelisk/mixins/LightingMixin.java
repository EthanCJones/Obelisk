/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Hooks into the lighting to allow changing
 * of brightness
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventLighting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkLightProvider.class)
public class LightingMixin
{
    @Inject(method = "getLightLevel", at = @At("RETURN"), cancellable = true)
    public void ias$getLightLevel(BlockPos pos, CallbackInfoReturnable<Integer> cir)
    {
        cir.setReturnValue(((EventLighting) EventAPI.call(new EventLighting(cir.getReturnValue()))).light);
    }
}
