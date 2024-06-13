/* ==========================================================
 * Author : Ethan Jones
 * Date   : 06/06/2024
 * TODO   : Nothing
 * Uses   : Allows manipulation of the camera
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventCamera;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin
{
    @Shadow protected abstract void setPos(Vec3d pos);
    @Shadow public abstract Vec3d getPos();
    @Shadow protected abstract void setRotation(float yaw, float pitch);
    @Shadow public abstract float getYaw();
    @Shadow public abstract float getPitch();

    @Inject(method = "update", at = @At(value = "TAIL"))
    public void ias$update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci)
    {
        EventCamera camera = (EventCamera) EventAPI.call(new EventCamera(getPos(), getYaw(), getPitch(), tickDelta));
        setPos(camera.pos);
        setRotation(camera.yaw, camera.pitch);
    }
}
