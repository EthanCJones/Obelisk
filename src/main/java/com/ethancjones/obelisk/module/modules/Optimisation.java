/* ==========================================================
 * Author : Ethan Jones
 * Date   : 19/06/2024
 * TODO   : Nothing
 * Uses   : Client sided optimisations to increase FPS
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventTick;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Optimisation extends Module
{
    private int distance = 64;

    public final ConcurrentLinkedQueue<Integer> culled = new ConcurrentLinkedQueue<>();

    public Optimisation()
    {
        super("Optimisation", 0, 0);
        EventAPI.register(onTick);
        toggle();
    }

    private final Listener<EventTick> onTick = new Listener<>()
    {
        @Override
        public void call(EventTick event)
        {
            if (isEnabled())
            {
                if (event.shouldCall(5))
                {
                    ArrayList<Integer> ids = new ArrayList<>();
                    for (Entity entity : MinecraftClient.getInstance().world.getEntities())
                    {
                        if (shouldCull(entity))
                        {
                            ids.add(entity.getId());
                        }
                    }
                    culled.clear();
                    culled.addAll(ids);
                }
            }
        }
    };

    public boolean shouldCull(Entity entity)
    {
        if (entity instanceof PlayerEntity)
        {
            return false;
        }

        Vec3d camera = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

        if (camera.distanceTo(entity.getPos()) > distance)
        {
            return true;
        }

        Vec3d entityBottom = new Vec3d(entity.getX(), entity.getVisibilityBoundingBox().minY, entity.getZ());
        Vec3d entityMiddle = new Vec3d(entity.getX(), entity.getY() + entity.getVisibilityBoundingBox().getLengthY() / 2, entity.getZ());
        Vec3d entityTop = new Vec3d(entity.getX(), entity.getVisibilityBoundingBox().maxY, entity.getZ());

        boolean bottomTrace = MinecraftClient.getInstance().world.raycast(new RaycastContext(camera, entityBottom, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, ShapeContext.absent())).getType() != HitResult.Type.MISS;
        boolean middleTrace = MinecraftClient.getInstance().world.raycast(new RaycastContext(camera, entityMiddle, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, ShapeContext.absent())).getType() != HitResult.Type.MISS;
        boolean topTrace = MinecraftClient.getInstance().world.raycast(new RaycastContext(camera, entityTop, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, ShapeContext.absent())).getType() != HitResult.Type.MISS;

        return bottomTrace && middleTrace && topTrace;
    }
}
