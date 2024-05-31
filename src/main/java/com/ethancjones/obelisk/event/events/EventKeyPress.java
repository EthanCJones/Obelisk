/* ==========================================================
 * Author : Ethan Jones
 * Date   : 31/05/2024
 * TODO   : Nothing
 * Uses   : Called when a key is pressed
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;

public class EventKeyPress extends Event
{
    public final int key;

    public EventKeyPress(int key)
    {
        this.key = key;
    }
}
