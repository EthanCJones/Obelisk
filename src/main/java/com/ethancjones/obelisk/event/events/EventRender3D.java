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
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.Vec3d;

public class EventRender3D extends Event
{
    public final Vec3d camera;
    public final Vec3d look;
    public final RenderTickCounter tickCounter;

    public EventRender3D(Vec3d camera, Vec3d look, RenderTickCounter tickCounter)
    {
        this.camera = camera;
        this.look = look;
        this.tickCounter = tickCounter;
    }
}
