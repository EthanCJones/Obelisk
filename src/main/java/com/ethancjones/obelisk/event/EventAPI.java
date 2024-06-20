/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Handles calling of listeners
 * ==========================================================
 */
package com.ethancjones.obelisk.event;

import com.ethancjones.obelisk.event.events.EventTick;
import com.ethancjones.obelisk.util.Logger;
import net.minecraft.client.MinecraftClient;

import java.util.*;

public class EventAPI
{
    //Stores all currently active listeners to be called
    private static final HashMap<Class<? extends Event>, LinkedList<Listener<?>>> eventListenerMap = new HashMap<>();

    //Registers a listener to be called with the event
    //Allows for multiple listeners to be registered
    public static void register(Listener<?>... listeners)
    {
        for (Listener listener : listeners)
        {
            if (listener.getEvent() == EventTick.class)
            {
                EventTick eventTick = new EventTick();
                new Timer().scheduleAtFixedRate(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        if (MinecraftClient.getInstance().world != null)
                        {
                            if (MinecraftClient.getInstance().player != null)
                            {
                                try
                                {
                                    listener.call(eventTick);
                                }
                                catch (Exception ignored)
                                {
                                }
                            }
                        }
                    }
                }, 0, 1);
            }
            else
            {
                eventListenerMap.putIfAbsent(listener.getEvent(), new LinkedList<>());
                eventListenerMap.get(listener.getEvent()).add(listener);
                eventListenerMap.get(listener.getEvent()).sort(Comparator.comparing(Listener::getPriority));
            }
            Logger.log("Registered listener " + listener);
        }
    }

    //Deregisters a listener to be called with the event
    //Allows for multiple listeners to be deregistered
    public static void deregister(Listener<?>... listeners)
    {
        for (Listener<?> listener : listeners)
        {
            eventListenerMap.get(listener.getEvent()).remove(listener);
            Logger.log("Deregistered listener " + listener);
        }
    }

    public static Event call(Event event)
    {
        if (eventListenerMap.containsKey(event.getClass()))
        {
            synchronized (eventListenerMap)
            {
                for (Listener listener : eventListenerMap.get(event.getClass()))
                {
                    listener.call(event);
                    if (listener.shouldEndCall())
                    {
                        break;
                    }
                }
            }
        }
        return event;
    }
}
