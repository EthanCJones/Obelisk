/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : The base for modules
 * ==========================================================
 */
package com.ethancjones.obelisk.module;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.command.CommandAPI;
import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.keybind.Keybind;
import com.ethancjones.obelisk.keybind.KeybindAPI;

import java.lang.reflect.Field;

public class Module
{
    private final String name;
    private boolean enabled;
    private final int colour;
    private final Keybind keybind;

    public Module(String name, int colour, int key)
    {
        this.name = name;
        this.colour = colour;
        keybind = new Keybind(key)
        {
            @Override
            public void run()
            {
                toggle();
            }
        };
        KeybindAPI.register(keybind);
    }

    public void initialise()
    {
        for (Field field : getClass().getDeclaredFields())
        {
            if (field.getType() == Command.class)
            {
                field.trySetAccessible();
                try
                {
                    CommandAPI.register((Command<?>) field.get(this));
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void toggle()
    {
        enabled = !enabled;
        if (enabled)
        {
            onEnable();
        }
        else
        {
            onDisable();
        }
    }

    protected void onEnable()
    {
        for (Field field : getClass().getDeclaredFields())
        {
            field.trySetAccessible();
            if (field.getType() == Listener.class)
            {
                try
                {
                    EventAPI.register((Listener<?>) field.get(this));
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    protected void onDisable()
    {
        for (Field field : getClass().getDeclaredFields())
        {
            field.trySetAccessible();
            if (field.getType() == Listener.class)
            {
                try
                {
                    EventAPI.deregister((Listener<?>) field.get(this));
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String getName()
    {
        return name;
    }
}
