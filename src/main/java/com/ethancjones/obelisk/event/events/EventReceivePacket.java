/* ==========================================================
 * Author : Ethan Jones
 * Date   : 31/05/2024
 * TODO   : Nothing
 * Uses   : Called when a packet is received from the server
 * Allows manipulation of client side information
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.network.packet.Packet;

public class EventReceivePacket extends Event
{
    public Packet<?> packet;

    public EventReceivePacket(Packet<?> packet)
    {
        this.packet = packet;
    }
}
