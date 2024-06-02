/* ==========================================================
 * Author : Ethan Jones
 * Date   : 02/06/2024
 * TODO   : Nothing
 * Uses   : Allows the player to look around freely
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventReceivePacket;
import com.ethancjones.obelisk.event.events.EventSendPacket;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.lwjgl.glfw.GLFW;

public class Freecam extends Module
{
    private Vec3d pos;
    private float yaw;
    private float pitch;

    public Freecam()
    {
        super("Freecam", 0xFF00FFFF, GLFW.GLFW_KEY_C);
    }

    @Override
    protected void onEnable()
    {
        pos = MinecraftClient.getInstance().player.getPos();
        yaw = MinecraftClient.getInstance().player.getYaw();
        pitch = MinecraftClient.getInstance().player.getPitch();
        MinecraftClient.getInstance().interactionManager.setGameMode(GameMode.SPECTATOR);
        super.onEnable();
    }

    @Override
    protected void onDisable()
    {
        super.onDisable();
        MinecraftClient.getInstance().interactionManager.setGameMode(GameMode.SURVIVAL);
        MinecraftClient.getInstance().player.setVelocity(0, 0, 0);
        MinecraftClient.getInstance().player.setPosition(pos);
        MinecraftClient.getInstance().player.setYaw(yaw);
        MinecraftClient.getInstance().player.setPitch(pitch);
    }

    private final Listener<EventSendPacket> onSendPacket = new Listener<>()
    {
        @Override
        public void call(EventSendPacket event)
        {
            event.cancelCall();
        }
    };
}
