/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Listens to calls for events to run the code
 * ==========================================================
 */
package com.ethancjones.obelisk.event;

import java.lang.reflect.ParameterizedType;

public abstract class Listener<T extends Event>
{
    //The priority in which the listener should be called
    //Descending order
    private Priority priority = Priority.NORMAL;

    private final Class<T> event;

    //Stops the call method early to stop lower priority calls from occurring
    private boolean endCall;

    public Listener()
    {
        event = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Listener(Priority priority)
    {
        event = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.priority = priority;
    }

    public Class<T> getEvent()
    {
        return event;
    }

    public abstract void call(T event);

    public Priority getPriority()
    {
        return priority;
    }

    public void endCall()
    {
        endCall = true;
    }

    public boolean shouldEndCall()
    {
        return endCall;
    }

    public enum Priority
    {
        HIGHEST,
        HIGH,
        NORMAL,
        LOW,
        LOWEST
    }
}
