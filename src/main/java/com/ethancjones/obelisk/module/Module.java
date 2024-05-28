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

import java.lang.reflect.Field;

public class Module
{
    private final String name;
    private boolean enabled;
    private final int colour;

    public Module(String name, int colour)
    {
        this.name = name;
        this.colour = colour;
    }

    public void initialise()
    {
        for (Field field : getClass().getDeclaredFields())
        {
            if (field.getType() == Command.class)
            {
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
