/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Injects into the HUD at runtime to allow 2D
 * rendering
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventRender2D;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HUDMixin
{
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;render(Lnet/minecraft/client/gui/DrawContext;F)V", shift = At.Shift.AFTER))
    public void ias$render(DrawContext context, float tickDelta, CallbackInfo ci)
    {
        EventAPI.call(new EventRender2D(context));
    }
}
