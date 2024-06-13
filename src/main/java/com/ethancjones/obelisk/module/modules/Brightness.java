/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Allows modification of the brightness values
 * within the world
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventLighting;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class Brightness extends Module
{
    private final Command<Integer> brightness;

    public Brightness()
    {
        super("Brightness", 0xFFFFAAAA, GLFW.GLFW_KEY_B);
        brightness = new Command<>(getName(), "brightness", 12, 1, 15);
    }

    @Override
    protected void onEnable()
    {
        super.onEnable();
        MinecraftClient.getInstance().worldRenderer.reload();
    }

    @Override
    protected void onDisable()
    {
        super.onDisable();
        MinecraftClient.getInstance().worldRenderer.reload();
    }

    private final Listener<EventLighting> onLighting = new Listener<>()
    {
        @Override
        public void call(EventLighting event)
        {
            event.light = brightness.getValue();
        }
    };
}
