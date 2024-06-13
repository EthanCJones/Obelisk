/* ==========================================================
 * Author : Ethan Jones
 * Date   : 03/06/2024
 * TODO   : Nothing
 * Uses   : Allows the player to step up blocks
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventMove;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.module.ModuleAPI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public class Step extends Module
{
    private final Command<Float> height;
    private int acTick;
    private double offsetY;
    private double moveX;
    private double moveZ;

    public Step()
    {
        super("Step", 0, 0);
        height = new Command<>(getName(), "height", 1.0F, 0.5F, 10F);
    }

    private final Listener<EventMove> onMove = new Listener<>()
    {
        @Override
        public void call(EventMove event)
        {
            if (acTick > 0)
            {
                switch (acTick)
                {
                    case 1:
                        event.movement = new Vec3d(0, 0.42, 0);
                        break;
                    case 2:
                        event.movement = new Vec3d(0, 0.33, 0);
                        break;
                    case 3:
                        acTick = -1;
                        event.movement = new Vec3d(moveX, 0.25, moveZ);
                        break;
                }
                acTick++;
            }
            else if (MinecraftClient.getInstance().player.isOnGround() && MinecraftClient.getInstance().player.horizontalCollision)
            {
                offsetY = 0;
                for (int y = 0; y < (ModuleAPI.antiCheat.isEnabled() ? 1 : height.getValue()); y++)
                {
                    //Player box without touching ground
                    Box playerBox = MinecraftClient.getInstance().player.getBoundingBox().offset(0, 0.01, 0);
                    //Offset according to movement and y step height
                    if (MinecraftClient.getInstance().world.getBlockCollisions(MinecraftClient.getInstance().player, playerBox.offset(event.movement.x, y, event.movement.z)).iterator().hasNext())
                    {
                        //Offset according to the block up check if air
                        if (!MinecraftClient.getInstance().world.getBlockCollisions(MinecraftClient.getInstance().player, playerBox.offset(event.movement.x, y + 1, event.movement.z)).iterator().hasNext())
                        {
                            offsetY = y + 1;
                        }
                    }
                }
                if (offsetY > 0)
                {
                    moveX = Math.clamp(event.movement.x, -0.001, 0.001);
                    moveZ = Math.clamp(event.movement.z, -0.001, 0.001);
                    if (ModuleAPI.antiCheat.isEnabled())
                    {
                        acTick = 1;
                    }
                    else
                    {
                        event.movement = new Vec3d(moveX, offsetY, moveZ);
                    }
                }
            }
        }
    };
}
