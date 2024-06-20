/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Called after the client sends positional updates
 * to the server
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import com.ethancjones.obelisk.util.ServerInfo;
import net.minecraft.client.MinecraftClient;

public class EventPlayerUpdatePost extends Event
{
    public EventPlayerUpdatePost()
    {
        ServerInfo.x = MinecraftClient.getInstance().player.getX();
        ServerInfo.y = MinecraftClient.getInstance().player.getY();
        ServerInfo.z = MinecraftClient.getInstance().player.getZ();
        ServerInfo.yaw = MinecraftClient.getInstance().player.getYaw();
        ServerInfo.pitch = MinecraftClient.getInstance().player.getPitch();
        ServerInfo.onGround = MinecraftClient.getInstance().player.isOnGround();
    }
}
