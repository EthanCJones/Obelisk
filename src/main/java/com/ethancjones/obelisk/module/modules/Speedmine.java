/* ==========================================================
 * Author : Ethan Jones
 * Date   : 21/06/2024
 * TODO   : Nothing
 * Uses   : Allows the user to mine blocks faster than usual
 * ALso automatically switches to the best tool for the block
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventBreakBlock;
import com.ethancjones.obelisk.mixins.IClientPlayerInteractionMixin;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class Speedmine extends Module
{
    private Command<Integer> delay;

    public Speedmine()
    {
        super("Speedmine", 0xFF00CCAA, GLFW.GLFW_KEY_O);
        delay = new Command<>(getName(), "delay", 0, 0, 5);
        toggle();
    }

    private final Listener<EventBreakBlock> onBreakBlock = new Listener<>()
    {
        @Override
        public void call(EventBreakBlock event)
        {
            BlockState blockState = MinecraftClient.getInstance().world.getBlockState(event.blockPos);
            if (event.progress == 0)
            {
                ((IClientPlayerInteractionMixin) MinecraftClient.getInstance().interactionManager).setBlockBreakingCooldown(delay.getValue());
            }
            else if (event.progress >= 0.8F - blockState.calcBlockBreakingDelta(MinecraftClient.getInstance().player, MinecraftClient.getInstance().world, event.blockPos))
            {
                ((IClientPlayerInteractionMixin) MinecraftClient.getInstance().interactionManager).setCurrentBreakingProgress(1.0F);
            }
        }
    };
}
