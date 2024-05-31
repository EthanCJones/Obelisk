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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.glfw.GLFW;

public class ESP extends Module
{
    public ESP()
    {
        super("ESP", 0, GLFW.GLFW_KEY_Y);
    }

    private final Listener<EventRender3D> onRender3D = new Listener<>()
    {
        @Override
        public void call(EventRender3D event)
        {
            event.buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            for (Entity entity : MinecraftClient.getInstance().world.getEntities())
            {
                if (entity instanceof LivingEntity)
                {
                    if (entity != MinecraftClient.getInstance().player)
                    {
                        event.buffer.vertex(event.camera.x + event.look.x, event.camera.y + event.look.y, event.camera.z + event.look.z).color(1.0F, 1.0F, 1.0F, 1.0F).next();
                        event.buffer.vertex(entity.getX(), entity.getY(), entity.getZ()).color(1.0F, 1.0F, 1.0F, 1.0F).next();
                    }
                }
            }
            BufferRenderer.drawWithGlobalProgram(event.buffer.end());
        }
    };
}
