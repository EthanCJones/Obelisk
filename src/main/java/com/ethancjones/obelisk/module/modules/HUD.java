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
import com.ethancjones.obelisk.module.ModuleAPI;
import net.minecraft.client.MinecraftClient;

public class HUD extends Module
{
    private final Command<Boolean> watermark;
    private final Command<Boolean> list;

    public HUD()
    {
        super("HUD", 0, 0);
        watermark = new Command<>(getName(), "watermark", true);
        list = new Command<>(getName(), "list", true);
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

            if (list.getValue())
            {
                int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
                int y = 2;

                for (Module module : ModuleAPI.getModules())
                {
                    if (module.isEnabled())
                    {
                        if (module.getColour() != 0)
                        {
                            event.context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, module.getName(), screenWidth - MinecraftClient.getInstance().textRenderer.getWidth(module.getName()) - 2, y, module.getColour());
                            y += MinecraftClient.getInstance().textRenderer.fontHeight;
                        }
                    }
                }
            }
        }
    };
}
