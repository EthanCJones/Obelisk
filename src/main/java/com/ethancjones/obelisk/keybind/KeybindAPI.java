/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : Handles processing of keybinds
 * ==========================================================
 */
package com.ethancjones.obelisk.keybind;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventInit;
import com.ethancjones.obelisk.util.Logger;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;

public class KeybindAPI
{
    private static final HashMap<Integer, ArrayList<Keybind>> keybinds = new HashMap<>();

    public static void initialise()
    {
        EventAPI.register(onInit);
    }

    public static void register(Keybind keybind)
    {
        if (!keybinds.containsKey(keybind.getKey()))
        {
            keybinds.put(keybind.getKey(), new ArrayList<>());
        }
        keybinds.get(keybind.getKey()).add(keybind);
        Logger.log("Registered keybind: " + keybind);
    }

    private static final Listener<EventInit> onInit = new Listener<>()
    {
        @Override
        public void call(EventInit event)
        {
            GLFW.glfwSetKeyCallback(MinecraftClient.getInstance().getWindow().getHandle(), ((window, key, scancode, action, mods) ->
            {
                MinecraftClient.getInstance().keyboard.onKey(window, key, scancode, action, mods);
                if (action == 1)
                {
                    if (keybinds.containsKey(key))
                    {
                        keybinds.get(key).forEach(Runnable::run);
                    }
                }
            }));
        }
    };
}
