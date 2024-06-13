/* ==========================================================
 * Author : Ethan Jones
 * Date   : 02/06/2024
 * TODO   : Nothing
 * Uses   : A multithreaded tick event called every ms
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;

public class EventTick extends Event
{
    private long lastCallTime;

    public EventTick()
    {
        lastCallTime = getCurrentTime();
    }

    private long getCurrentTime()
    {
        return System.nanoTime() / 1000000;
    }

    public boolean shouldCall(int time)
    {
        if (getCurrentTime() - lastCallTime >= time)
        {
            lastCallTime = getCurrentTime();
            return true;
        }
        else
        {
            return false;
        }
    }
}
