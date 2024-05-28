/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : Hooks into the connection to the server to
 * allow tracking and modifying of sent and received packets.
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventSendPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin
{
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void ias$send(Packet<?> packet, CallbackInfo ci)
    {
        EventSendPacket event = (EventSendPacket) EventAPI.call(new EventSendPacket(packet));
        if (event.isCallCancelled())
        {
            ci.cancel();
        }
    }
}
