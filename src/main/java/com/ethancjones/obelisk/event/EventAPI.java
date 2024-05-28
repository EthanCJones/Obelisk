/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Handles calling of listeners
 * ==========================================================
 */
package com.ethancjones.obelisk.event;

import com.ethancjones.obelisk.util.Logger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class EventAPI
{
    //Stores all currently active listeners to be called
    private final static HashMap<Class<? extends Event>, TreeSet<Listener<?>>> eventListenerMap = new HashMap<>();

    //Registers a listener to be called with the event
    //Allows for multiple listeners to be registered
    public static void register(Listener<?>... listeners)
    {
        for (Listener<?> listener : listeners)
        {
            if (!eventListenerMap.containsKey(listener.getEvent()))
            {
                eventListenerMap.put(listener.getEvent(), new TreeSet<>(Comparator.comparing(Listener::getPriority)));
            }
            eventListenerMap.get(listener.getEvent()).add(listener);
            Logger.log("Registered listener " + listener);
        }
    }

    //Deregisters a listener to be called with the event
    //Allows for multiple listeners to be deregistered
    public static void deregister(Listener<?>... listeners)
    {
        for (Listener<?> listener : listeners)
        {
            if (!eventListenerMap.containsKey(listener.getEvent()))
            {
                eventListenerMap.put(listener.getEvent(), new TreeSet<>(Comparator.comparing(Listener::getPriority)));
            }
            eventListenerMap.get(listener.getEvent()).remove(listener);
            Logger.log("Deregistered listener " + listener);
        }
    }

    public static Event call(Event event)
    {
        if (eventListenerMap.containsKey(event.getClass()))
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
        return event;
    }
}
