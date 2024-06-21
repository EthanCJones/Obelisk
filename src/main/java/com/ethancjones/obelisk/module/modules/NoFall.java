/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Prevents taking damage from falling
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.Obelisk;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePost;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePre;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.module.ModuleAPI;
import com.ethancjones.obelisk.util.ClientInfo;
import com.ethancjones.obelisk.util.ServerInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

public class NoFall extends Module
{
    public NoFall()
    {
        super("NoFall", 0xFF00FF00, GLFW.GLFW_KEY_N);
        toggle();
    }

    private final Listener<EventPlayerUpdatePre> onPlayerUpdatePre = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePre event)
        {
            if (ServerInfo.fallDistance >= 2)
            {
                if (ModuleAPI.antiCheat.isEnabled())
                {
                    event.cancelCall();
                    MinecraftClient.getInstance().getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
                    Obelisk.sendInvalidMovement();
                }
                else
                {
                    MinecraftClient.getInstance().player.setOnGround(true);
                }
            }
        }
    };

    private final Listener<EventPlayerUpdatePost> onPlayerUpdatePost = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePost event)
        {
            if (ServerInfo.onGround != ClientInfo.onGround)
            {
                MinecraftClient.getInstance().player.setOnGround(ClientInfo.onGround);
            }
        }
    };
}
