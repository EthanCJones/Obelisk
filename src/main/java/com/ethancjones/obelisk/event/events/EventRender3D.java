/* ==========================================================
 * Author : Ethan Jones
 * Date   : 29/05/2024
 * TODO   : Nothing
 * Uses   : Called when the 3D environment is rendered
 * Used to draw with the lwjgl library
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.util.math.Vec3d;

public class EventRender3D extends Event
{
    public final BufferBuilder buffer;
    public final Vec3d camera;
    public final Vec3d look;
    public final float tickDelta;

    public EventRender3D(BufferBuilder buffer, Vec3d camera, Vec3d look, float tickDelta)
    {
        this.buffer = buffer;
        this.camera = camera;
        this.look = look;
        this.tickDelta = tickDelta;
    }
}
