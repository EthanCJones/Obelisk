/* ==========================================================
 * Author : Ethan Jones
 * Date   : 18/06/2024
 * TODO   : Nothing
 * Uses   : Displays where the player has moved
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventRender3D;
import com.ethancjones.obelisk.event.events.EventTick;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import java.util.LinkedList;

public class Breadcrumbs extends Module
{
    private double distance = 8;

    private final LinkedList<Float[]> crumbs = new LinkedList<>();

    public Breadcrumbs()
    {
        super("Breadcrumbs", 0, 0);
        EventAPI.register(onTick);
    }

    private final Listener<EventRender3D> onRender3D = new Listener<>()
    {
        @Override
        public void call(EventRender3D event)
        {
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
            for (Float[] crumb : crumbs)
            {
                buffer.vertex(crumb[0], crumb[1], crumb[2]).color(255, 255, 255, 255);
            }
            buffer.vertex((float) MinecraftClient.getInstance().player.getX(), (float) MinecraftClient.getInstance().player.getY(), (float) MinecraftClient.getInstance().player.getZ()).color(255, 255, 255, 255);
            RenderUtils.draw(buffer);
        }
    };

    private final Listener<EventTick> onTick = new Listener<>()
    {
        @Override
        public void call(EventTick event)
        {
            if (event.shouldCall(100))
            {
                if (crumbs.isEmpty())
                {
                    crumbs.add(new Float[]{(float) MinecraftClient.getInstance().player.getX(), (float) MinecraftClient.getInstance().player.getY(), (float) MinecraftClient.getInstance().player.getZ()});
                }
                else
                {
                    Float[] last = crumbs.getLast();
                    if (Math.sqrt(MinecraftClient.getInstance().player.squaredDistanceTo(last[0], last[1], last[2])) > distance)
                    {
                        crumbs.add(new Float[]{(float) MinecraftClient.getInstance().player.getX(), (float) MinecraftClient.getInstance().player.getY(), (float) MinecraftClient.getInstance().player.getZ()});
                    }
                }
            }
        }
    };
}
