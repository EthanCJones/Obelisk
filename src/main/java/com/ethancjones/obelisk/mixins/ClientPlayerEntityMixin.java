/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : Hooks into the movement of the player to allow
 * manipulation
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventMove;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePost;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePre;
import com.ethancjones.obelisk.event.events.EventSwingHand;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
    public Vec3d ias$move(Vec3d vec3d)
    {
        return ((EventMove) EventAPI.call(new EventMove(vec3d))).movement;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER), cancellable = true)
    public void ias$tickPre(CallbackInfo ci)
    {
        if (EventAPI.call(new EventPlayerUpdatePre()).isCallCancelled())
        {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void ias$tickPost(CallbackInfo ci)
    {
        EventAPI.call(new EventPlayerUpdatePost());
    }

    @Inject(method = "swingHand", at = @At("HEAD"), cancellable = true)
    public void ias$swingHand(Hand hand, CallbackInfo ci)
    {
        if (EventAPI.call(new EventSwingHand(hand)).isCallCancelled())
        {
            ci.cancel();
        }
    }
}
