/* ==========================================================
 * Author : Ethan Jones
 * Date   : 19/06/2024
 * TODO   : Nothing
 * Uses   : Injects into world renderer to allow editing
 * of rendering
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.module.ModuleAPI;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRenderMixin
{
    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    public void ias$renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci)
    {
        if (ModuleAPI.optimisation.isEnabled())
        {
            if (ModuleAPI.optimisation.culled.contains(entity.getId()))
            {
                ci.cancel();
            }
        }
    }
}
