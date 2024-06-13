/* ==========================================================
 * Author : Ethan Jones
 * Date   : 06/06/2024
 * TODO   : Nothing
 * Uses   : Allows manipulation of movement directly from
 * the input values
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;

public class EventKeyboardInput extends Event
{
    public boolean slowDown;
    public float factor;

    public EventKeyboardInput(boolean slowDown, float factor)
    {
        this.slowDown = slowDown;
        this.factor = factor;
    }
}
