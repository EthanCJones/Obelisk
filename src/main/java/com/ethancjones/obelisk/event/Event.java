/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : The base for all event calls in the client
 * ==========================================================
 */
package com.ethancjones.obelisk.event;

public class Event
{
    //Cancels the original method from executing any further in this cycle
    private boolean cancelCall;

    public void cancelCall()
    {
        cancelCall = true;
    }

    public boolean isCallCancelled()
    {
        return cancelCall;
    }
}
