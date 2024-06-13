/* ==========================================================
 * Author : Ethan Jones
 * Date   : 06/06/2024
 * TODO   : Nothing
 * Uses   : Allows control over the game camera
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.util.math.Vec3d;

public class EventCamera extends Event
{
    public Vec3d pos;
    public float yaw;
    public float pitch;
    public float tickDelta;

    public EventCamera(Vec3d pos, float yaw, float pitch, float tickDelta)
    {
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
        this.tickDelta = tickDelta;
    }
}
