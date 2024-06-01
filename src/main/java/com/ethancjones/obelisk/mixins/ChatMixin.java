/* ==========================================================
 * Author : Ethan Jones
 * Date   : 31/05/2024
 * TODO   : Nothing
 * Uses   : Used to hook into chat
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventChatMessage;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatMixin
{
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    public void ias$addMessage(Text message, CallbackInfo ci)
    {
        if (EventAPI.call(new EventChatMessage(message)).isCallCancelled())
        {
            ci.cancel();
        }
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    public void ias$addMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci)
    {
        if (EventAPI.call(new EventChatMessage(message)).isCallCancelled())
        {
            ci.cancel();
        }
    }
}
