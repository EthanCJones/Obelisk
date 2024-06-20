/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Called before the client sends positional updates
 * to server. Allows manipulation of position and rotation
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import com.ethancjones.obelisk.util.ClientInfo;
import net.minecraft.client.MinecraftClient;

public class EventPlayerUpdatePre extends Event
{
    public EventPlayerUpdatePre()
    {
        ClientInfo.x = MinecraftClient.getInstance().player.getX();
        ClientInfo.y = MinecraftClient.getInstance().player.getY();
        ClientInfo.z = MinecraftClient.getInstance().player.getZ();
        ClientInfo.yaw = MinecraftClient.getInstance().player.getYaw();
        ClientInfo.pitch = MinecraftClient.getInstance().player.getPitch();
        ClientInfo.onGround = MinecraftClient.getInstance().player.isOnGround();
    }
}
