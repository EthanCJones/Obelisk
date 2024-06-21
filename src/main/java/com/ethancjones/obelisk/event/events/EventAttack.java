/* ==========================================================
 * Author : Ethan Jones
 * Date   : 21/06/2024
 * TODO   : Nothing
 * Uses   : Called when the player attacks an entity
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event
{
    public Entity target;

    public EventAttack(Entity target)
    {
        this.target = target;
    }
}
