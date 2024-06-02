/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Called when lighting values are questioned by
 * the game
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;

public class EventLighting extends Event
{
    public int light;

    public EventLighting(int light)
    {
        this.light = light;
    }
}
