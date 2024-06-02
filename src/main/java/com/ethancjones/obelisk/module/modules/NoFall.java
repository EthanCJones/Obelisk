/* ==========================================================
 * Author : Ethan Jones
 * Date   : 01/06/2024
 * TODO   : Nothing
 * Uses   : Prevents taking damage from falling
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePost;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePre;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class NoFall extends Module
{
    boolean trueOnGround;

    public NoFall()
    {
        super("NoFall", 0xFF00FF00, GLFW.GLFW_KEY_N);
    }

    private final Listener<EventPlayerUpdatePre> onPlayerUpdatePre = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePre event)
        {
            trueOnGround = MinecraftClient.getInstance().player.isOnGround();
            MinecraftClient.getInstance().player.setOnGround(true);
        }
    };

    private final Listener<EventPlayerUpdatePost> onPlayerUpdatePost = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePost event)
        {
            MinecraftClient.getInstance().player.setOnGround(trueOnGround);
        }
    };
}
