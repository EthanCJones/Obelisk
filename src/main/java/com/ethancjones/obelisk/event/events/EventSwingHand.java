/* ==========================================================
 * Author : Ethan Jones
 * Date   : 21/06/2024
 * TODO   : Nothing
 * Uses   : Called when the player swings their hand
 * ==========================================================
 */
package com.ethancjones.obelisk.event.events;

import com.ethancjones.obelisk.event.Event;
import net.minecraft.util.Hand;

public class EventSwingHand extends Event
{
    public Hand hand;

    public EventSwingHand(Hand hand)
    {
        this.hand = hand;
    }
}
