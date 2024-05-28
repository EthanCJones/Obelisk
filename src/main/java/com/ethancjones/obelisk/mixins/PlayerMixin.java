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
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientPlayerEntity.class)
public class PlayerMixin
{
    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
    public Vec3d ias$move(Vec3d vec3d)
    {
        return ((EventMove) EventAPI.call(new EventMove(vec3d))).movement;
    }
}
