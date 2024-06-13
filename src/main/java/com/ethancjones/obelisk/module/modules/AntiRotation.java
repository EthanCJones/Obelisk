/* ==========================================================
 * Author : Ethan Jones
 * Date   : 03/06/2024
 * TODO   : Nothing
 * Uses   : Stops rotation from being set by the server
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventReceivePacket;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class AntiRotation extends Module
{
    public AntiRotation()
    {
        super("AntiRotation", 0, 0);
        toggle();
    }

    private final Listener<EventReceivePacket> onReceivePacket = new Listener<>()
    {
        @Override
        public void call(EventReceivePacket event)
        {
            if (MinecraftClient.getInstance().world != null)
            {
                if (MinecraftClient.getInstance().player != null)
                {
                    if (event.packet instanceof PlayerPositionLookS2CPacket)
                    {
                        event.cancelCall();
                        Set<PositionFlag> flags = ((PlayerPositionLookS2CPacket) event.packet).getFlags();
                        double x = ((PlayerPositionLookS2CPacket) event.packet).getX();
                        double y = ((PlayerPositionLookS2CPacket) event.packet).getY();
                        double z = ((PlayerPositionLookS2CPacket) event.packet).getZ();

                        if (flags.contains(PositionFlag.X))
                        {
                            x += MinecraftClient.getInstance().player.getX();
                        }
                        if (flags.contains(PositionFlag.Y))
                        {
                            y += MinecraftClient.getInstance().player.getY();
                        }
                        if (flags.contains(PositionFlag.Z))
                        {
                            z += MinecraftClient.getInstance().player.getZ();
                        }

                        MinecraftClient.getInstance().player.setPosition(x, y, z);
                        MinecraftClient.getInstance().getNetworkHandler().sendPacket(new TeleportConfirmC2SPacket(((PlayerPositionLookS2CPacket) event.packet).getTeleportId()));
                    }
                }
            }
        }
    };
}
