/* ==========================================================
 * Author : Ethan Jones
 * Date   : 21/06/2024
 * TODO   : Nothing
 * Uses   : Called when the player's block breaking progress
 * is updated
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.util.math.BlockPos;

public class EventBreakBlock extends Event
{
    public BlockPos blockPos;
    public float progress;

    public EventBreakBlock(BlockPos blockPos, float progress)
    {
        this.blockPos = blockPos;
        this.progress = progress;
    }
}
