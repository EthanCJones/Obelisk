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

import java.util.Iterator;

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
        height = new Command<>(getName(), "height", 1.0625F, 0.5F, 10F);
        toggle();
    }

    private final Listener<EventMove> onMove = new Listener<>(Listener.Priority.LOWEST)
    {
        @Override
        public void call(EventMove event)
        {
            if (acTick > 0)
            {
                switch (acTick)
                {
                    case 1:
                        //event.movement = new Vec3d(0, 0.42, 0);
                        event.movement = new Vec3d(0, offsetY * 0.42, 0);
                        break;
                    case 2:
                        //event.movement = new Vec3d(0, 0.33, 0);
                        event.movement = new Vec3d(0, offsetY * 0.33, 0);
                        break;
                    case 3:
                        acTick = -1;
                        //event.movement = new Vec3d(moveX, 0.25, moveZ);
                        event.movement = new Vec3d(moveX, offsetY * 0.25, moveZ);
                        break;
                }
                acTick++;
            }
            else if (MinecraftClient.getInstance().player.isOnGround() && (event.movement.x != 0 || event.movement.z != 0))
            {
                //Player box without touching ground
                Box playerBox = MinecraftClient.getInstance().player.getBoundingBox().offset(0, 0.01, 0);
                offsetY = 0;
                //Check if the player will collide with block on next movement update
                if (MinecraftClient.getInstance().world.getBlockCollisions(MinecraftClient.getInstance().player, playerBox.offset(event.movement.x, 0, event.movement.z)).iterator().hasNext())
                {
                    //Check all y values for the step height rounded up
                    double upperStepHeight = Math.ceil(height.getValue());
                    for (int y = 0; y < upperStepHeight; y++)
                    {
                        //Check the blocks above the player's head at that y level to see if movement is possible
                        if (!MinecraftClient.getInstance().world.getBlockCollisions(MinecraftClient.getInstance().player, playerBox.offset(0, y, 0)).iterator().hasNext())
                        {
                            //Get the blocks that are to be stepped on to
                            Iterable<VoxelShape> stepBoxes = MinecraftClient.getInstance().world.getBlockCollisions(MinecraftClient.getInstance().player, playerBox.offset(event.movement.x, y, event.movement.z));
                            //If there are blocks
                            if (stepBoxes.iterator().hasNext())
                            {
                                //Check next block up is air
                                if (!MinecraftClient.getInstance().world.getBlockCollisions(MinecraftClient.getInstance().player, playerBox.offset(event.movement.x, y + 1, event.movement.z)).iterator().hasNext())
                                {
                                    for (VoxelShape stepBox : stepBoxes)
                                    {
                                        for (Box box : stepBox.getBoundingBoxes())
                                        {
                                            double change = box.maxY - MinecraftClient.getInstance().player.getY();
                                            if (box.intersects(playerBox.offset(event.movement.x, y, event.movement.z)))
                                            {
                                                if (change > offsetY)
                                                {
                                                    offsetY = change;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //If the block to be stepped on is greater than normal step height and smaller than the step height setting
                if (offsetY > 0.6 && offsetY <= (ModuleAPI.antiCheat.isEnabled() ? 1.0625 : height.getValue()))
                {
                    moveX = event.movement.x;
                    moveZ = event.movement.z;
                    if (ModuleAPI.antiCheat.isEnabled())
                    {
                        acTick = 2;
                        event.movement = new Vec3d(0, offsetY * 0.42, 0);
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
