/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : Called when the player moves
 * Allows for manipulation of movement
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.util.math.Vec3d;

public class EventMove extends Event
{
    public Vec3d movement;

    public EventMove(Vec3d movement)
    {
        this.movement = movement;
    }
}
