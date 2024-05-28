/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Called when the 2D rendering is done in game
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.client.gui.DrawContext;

public class EventRender2D extends Event
{
    public final DrawContext context;

    public EventRender2D(DrawContext context)
    {
        this.context = context;
    }
}
