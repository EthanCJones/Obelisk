/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Called when a message is added to the chat
 * Allows manipulation of chat
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.text.Text;

public class EventChatMessage extends Event
{
    public final Text text;

    public EventChatMessage(Text text)
    {
        this.text = text;
    }
}
