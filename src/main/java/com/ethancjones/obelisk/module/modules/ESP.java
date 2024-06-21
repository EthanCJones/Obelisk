/* ==========================================================
 * Author : Ethan Jones
 * Date   : 31/05/2024
 * TODO   : Nothing
 * Uses   : Allows the user to track entities through walls
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventRender3D;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.glfw.GLFW;

public class ESP extends Module
{
    int distance = 128;

    public ESP()
    {
        super("ESP", 0, GLFW.GLFW_KEY_Y);
    }

    private final Listener<EventRender3D> onRender3D = new Listener<>()
    {
        @Override
        public void call(EventRender3D event)
        {
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            for (Entity entity : MinecraftClient.getInstance().world.getEntities())
            {
                if (entity instanceof LivingEntity)
                {
                    if (entity != MinecraftClient.getInstance().player)
                    {
                        if (((LivingEntity) entity).getHealth() > 0)
                        {
                            float distanceClamped = Math.clamp(MinecraftClient.getInstance().player.distanceTo(entity), 1, distance);
                            float red = 1 / distanceClamped;
                            float green = distanceClamped / distance;
                            RenderUtils.render3DBoxWithRotation(buffer, entity.getBoundingBox(), ((LivingEntity) entity).headYaw, red, green, 0, 0.2F);
                        }
                    }
                }
            }
            RenderUtils.draw(buffer);
        }
    };
}
