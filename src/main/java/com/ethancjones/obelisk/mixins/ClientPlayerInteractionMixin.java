/* ==========================================================
 * Author : Ethan Jones
 * Date   : 21/06/2024
 * TODO   : Nothing
 * Uses   :
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventAttack;
import com.ethancjones.obelisk.event.events.EventBreakBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionMixin
{
    @Shadow
    private float currentBreakingProgress;

    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode = Opcodes.PUTFIELD, ordinal = 2, shift = At.Shift.BY, by = 2))
    public void ias$updateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir)
    {
        EventAPI.call(new EventBreakBlock(pos, currentBreakingProgress));
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    public void ias$attackEntity(PlayerEntity player, Entity target, CallbackInfo ci)
    {
        if (EventAPI.call(new EventAttack(target)).isCallCancelled())
        {
            ci.cancel();
        }
    }
}
