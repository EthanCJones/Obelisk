/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : Called when a packet is sent by the client to
 * the server
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.network.packet.Packet;

public class EventSendPacket extends Event
{
    public Packet<?> packet;

    public EventSendPacket(Packet<?> packet)
    {
        this.packet = packet;
    }
}
