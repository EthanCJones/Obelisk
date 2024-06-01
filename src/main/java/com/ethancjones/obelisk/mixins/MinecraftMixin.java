/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : Used to hook into the base minecraft methods
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.Event;
import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventDisplayScreen;
import com.ethancjones.obelisk.event.events.EventInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftMixin
{
    @Inject(method = "<init>", at = @At("TAIL"))
    public void ias$init(RunArgs args, CallbackInfo ci)
    {
        EventAPI.call(new EventInit());
    }

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    public Screen ias$setScreen(Screen screen)
    {
        return ((EventDisplayScreen) EventAPI.call(new EventDisplayScreen(screen))).screen;
    }
}
