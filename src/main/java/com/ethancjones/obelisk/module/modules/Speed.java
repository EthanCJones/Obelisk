/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : Allows the player to move faster than normal
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventMove;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.module.ModuleAPI;
import org.lwjgl.glfw.GLFW;

public class Speed extends Module
{
    private final Command<Double> speed;

    public Speed()
    {
        super("Speed", 0xFFAAFFAA, GLFW.GLFW_KEY_R);
        speed = new Command<>(getName(), "speed", 2D, 0.1D, 10D);
    }

    private final Listener<EventMove> onMove = new Listener<>()
    {
        @Override
        public void call(EventMove event)
        {
            double mulSpeed = (ModuleAPI.antiCheat.isEnabled() ? 1.25 : speed.getValue());
            event.movement = event.movement.multiply(mulSpeed, 1, mulSpeed);
        }
    };
}
