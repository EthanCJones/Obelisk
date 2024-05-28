/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Displays useful information on the HUD
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventRender2D;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;

public class HUD extends Module
{
    private final Command<Boolean> watermark;

    public HUD()
    {
        super("HUD", 0, 0);
        watermark = new Command<>(getName(), "watermark", true);
        toggle();
    }

    private final Listener<EventRender2D> onRender2D = new Listener<>()
    {
        @Override
        public void call(EventRender2D event)
        {
            if (watermark.getValue())
            {
                event.context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, "Obelisk", 2, 2, 0xFFFFFFFF);
            }
        }
    };
}
