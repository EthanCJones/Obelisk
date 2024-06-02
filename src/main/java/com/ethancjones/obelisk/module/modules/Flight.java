/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Allows the user to fly
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventMove;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePost;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePre;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Flight extends Module
{
    private boolean trueOnGround;

    public Flight()
    {
        super("Flight", 0xFFAAFFAA, GLFW.GLFW_KEY_G);
    }

    private final Listener<EventMove> onMove = new Listener<>()
    {
        @Override
        public void call(EventMove event)
        {
            double x = event.movement.x * 3;
            double y = 0;
            double z = event.movement.z * 3;

            if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_SPACE) == 1)
            {
                y = 1;
            }
            else if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == 1)
            {
                y = -1;
            }

            event.movement = new Vec3d(x, y, z);
        }
    };

    private final Listener<EventPlayerUpdatePre> onPlayerUpdatePre = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePre event)
        {
            trueOnGround = MinecraftClient.getInstance().player.isOnGround();
            MinecraftClient.getInstance().player.setOnGround(true);
        }
    };

    private final Listener<EventPlayerUpdatePost> onPlayerUpdatePost = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePost event)
        {
            MinecraftClient.getInstance().player.setOnGround(trueOnGround);
        }
    };
}
