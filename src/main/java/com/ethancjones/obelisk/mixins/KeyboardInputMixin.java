/* ==========================================================
 * Author : Ethan Jones
 * Date   : 06/06/2024
 * TODO   : Nothing
 * Uses   : Hooks into movement input to allow manipulation
 * of movement
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventKeyboardInput;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin
{
    @Inject(method = "tick", at = @At("TAIL"))
    public void ias$tick(boolean slowDown, float slowDownFactor, CallbackInfo ci)
    {
        EventAPI.call(new EventKeyboardInput(slowDown, slowDownFactor));
    }
}
