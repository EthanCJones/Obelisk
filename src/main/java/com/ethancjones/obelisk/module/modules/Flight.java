/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Allows the user to fly
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.Obelisk;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventMove;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePost;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePre;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.module.ModuleAPI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Flight extends Module
{
    private boolean trueOnGround;
    private int resetTick;
    private int resetTicks = 70;
    private int acTick;

    public Flight()
    {
        super("Flight", 0xFFAAFFAA, GLFW.GLFW_KEY_G);
    }

    @Override
    protected void onDisable()
    {
        super.onDisable();
        MinecraftClient.getInstance().player.setVelocity(0, 0, 0);
    }

    private final Listener<EventMove> onMove = new Listener<>(Listener.Priority.HIGHEST)
    {
        @Override
        public void call(EventMove event)
        {
            double x = event.movement.x * (ModuleAPI.antiCheat.isEnabled() ? 0.0624 : 3);
            double y = 0;
            double z = event.movement.z * (ModuleAPI.antiCheat.isEnabled() ? 0.0624 : 3);

            if (ModuleAPI.antiCheat.isEnabled())
            {
                if (MinecraftClient.getInstance().options.jumpKey.isPressed() || MinecraftClient.getInstance().options.sneakKey.isPressed())
                {
                    x = 0;
                    z = 0;
                }
            }
            else
            {
                if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_SPACE) == 1)
                {
                    y = 0.5;
                }
                else if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == 1)
                {
                    y = -0.5;
                }
            }

            if (resetTick >= resetTicks)
            {
                resetTick = 0;
                y = -0.032;
            }

            event.movement = new Vec3d(x, y, z);
            endCall();
        }
    };

    private final Listener<EventPlayerUpdatePre> onPlayerUpdatePre = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePre event)
        {
            resetTick++;

            if (ModuleAPI.antiCheat.isEnabled())
            {
                acTick++;

                double x = MinecraftClient.getInstance().player.getX();
                double y = MinecraftClient.getInstance().player.getY();
                double z = MinecraftClient.getInstance().player.getZ();

                event.cancelCall();
                if (resetTick < resetTicks)
                {
                    if (acTick >= 3)
                    {
                        acTick = 0;
                        resetTick++;
                        MinecraftClient.getInstance().getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + (MinecraftClient.getInstance().options.jumpKey.isPressed() ? 0.0624 : 0) - (MinecraftClient.getInstance().options.sneakKey.isPressed() ? 0.0624 : 0), z, false));
                        Obelisk.sendInvalidMovement();
                    }
                }
            }
            else
            {
                trueOnGround = MinecraftClient.getInstance().player.isOnGround();
                MinecraftClient.getInstance().player.setOnGround(true);
            }
        }
    };

    private final Listener<EventPlayerUpdatePost> onPlayerUpdatePost = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePost event)
        {
            if (!ModuleAPI.antiCheat.isEnabled())
            {
                MinecraftClient.getInstance().player.setOnGround(trueOnGround);
            }
        }
    };
}
