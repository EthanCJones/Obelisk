/* ==========================================================
 * Author : Ethan Jones
 * Date   : 18/06/2024
 * TODO   : Nothing
 * Uses   : Allows creation of waypoints to remember useful
 * points on the map
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventRender3D;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.module.ModuleAPI;
import com.ethancjones.obelisk.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Waypoint extends Module
{
    private final ArrayList<Float[]> sphere = new ArrayList<>();

    private final ConcurrentLinkedQueue<Point> waypoints = new ConcurrentLinkedQueue<>();

    private final Command add;
    private final Command remove;
    private final Command here;

    private float size = 3;

    public Waypoint()
    {
        super("Waypoint", 0, 0);
        toggle();

        for (int phi = -90; phi <= 90; phi += 10)
        {
            for (int theta = 0; theta <= 360; theta += 10)
            {
                sphere.add(new Float[] {(float) (Math.cos(Math.toRadians(phi)) * Math.cos(Math.toRadians(theta))), (float) (Math.cos(Math.toRadians(phi)) * Math.sin(Math.toRadians(theta))), (float) Math.sin(Math.toRadians(phi))});
                if (theta == 360)
                {
                    sphere.add(new Float[]{0F, 0F, 0F});
                }
            }
        }

        for (int phi = -90; phi <= 90; phi += 15)
        {
            for (int theta = 0; theta <= 360; theta += 15)
            {
                sphere.add(new Float[] {(float) Math.sin(Math.toRadians(phi)), (float) (Math.cos(Math.toRadians(phi)) * Math.sin(Math.toRadians(theta))), (float) (Math.cos(Math.toRadians(phi)) * Math.cos(Math.toRadians(theta)))});
                if (theta == 360)
                {
                    sphere.add(new Float[]{0F, 0F, 0F});
                }
            }
        }

        add = new Command(getName(), "add")
        {
            @Override
            public void run(String[] args)
            {
                waypoints.add(new Point(args[2], Float.valueOf(args[3]), Float.valueOf(args[4]), Float.valueOf(args[5]), false));
            }
        };

        remove = new Command(getName(), "remove")
        {
            @Override
            public void run(String[] args)
            {
                waypoints.removeIf(point -> point.name.equalsIgnoreCase(args[2]));
            }
        };

        here = new Command(getName(), "here")
        {
            @Override
            public void run(String[] args)
            {
                float x = (float) MinecraftClient.getInstance().gameRenderer.getCamera().getPos().x;
                float y = (float) MinecraftClient.getInstance().gameRenderer.getCamera().getPos().y;
                float z = (float) MinecraftClient.getInstance().gameRenderer.getCamera().getPos().z;
                waypoints.add(new Point(args[2], x, y, z, ModuleAPI.getModuleByClass(Freecam.class).isEnabled()));
            }
        };
    }

    private final Listener<EventRender3D> onRender3D = new Listener<>()
    {
        @Override
        public void call(EventRender3D event)
        {
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
            for (Point point : waypoints)
            {
                if (point.removeOnContact)
                {
                    if (Math.sqrt(MinecraftClient.getInstance().player.squaredDistanceTo(point.x, point.y, point.z)) < size)
                    {
                        waypoints.remove(point);
                        continue;
                    }
                }
                for (Float[] spherePoint : sphere)
                {
                    if (spherePoint[0] == 0 && spherePoint[1] == 0 && spherePoint[2] == 0)
                    {
                        RenderUtils.draw(buffer);
                        buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
                    }
                    else
                    {
                        buffer.vertex(point.x + spherePoint[0] * size, point.y + spherePoint[1] * size, point.z + spherePoint[2] * size).color(255, 255, 255, 255);
                    }
                }
            }
            RenderUtils.draw(buffer);
        }
    };

    private record Point(String name, float x, float y, float z, boolean removeOnContact)
    {}
}
