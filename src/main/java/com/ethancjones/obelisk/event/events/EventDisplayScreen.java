/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Called when a new screen is displayed
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.client.gui.screen.Screen;

public class EventDisplayScreen extends Event
{
    public Screen screen;

    public EventDisplayScreen(Screen screen)
    {
        this.screen = screen;
    }
}
