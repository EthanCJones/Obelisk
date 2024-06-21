/* ==========================================================
 * Author : Ethan Jones
 * Date   : 20/06/2024
 * TODO   : Nothing
 * Uses   : Allows the user to always hit a critical strike
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventAttack;
import com.ethancjones.obelisk.event.events.EventSwingHand;
import com.ethancjones.obelisk.event.events.EventTick;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

public class Criticals extends Module
{
    private boolean doCrit;
    private long attackTime;
    private Entity target;

    public Criticals()
    {
        super("Criticals", 0xFFCCAA00, 0);
        EventAPI.register(onTick);
    }

    private final Listener<EventAttack> onAttack = new Listener<>()
    {
        @Override
        public void call(EventAttack event)
        {
            if (!doCrit)
            {
                if (MinecraftClient.getInstance().player.isOnGround())
                {
                    doCrit = true;
                    attackTime = System.nanoTime() / 1000000;
                    target = event.target;
                    MinecraftClient.getInstance().player.setVelocity(MinecraftClient.getInstance().player.getVelocity().x, 0.04, MinecraftClient.getInstance().player.getVelocity().z);
                    event.cancelCall();
                }
            }
        }
    };

    private final Listener<EventSwingHand> onSwingHand = new Listener<>()
    {
        @Override
        public void call(EventSwingHand event)
        {
            if (doCrit)
            {
                event.cancelCall();
            }
        }
    };

    private final Listener<EventTick> onTick = new Listener<>()
    {
        @Override
        public void call(EventTick event)
        {
            if (isEnabled())
            {
                if (doCrit)
                {
                    if (MinecraftClient.getInstance().player.fallDistance > 0)
                    {
                        MinecraftClient.getInstance().interactionManager.attackEntity(MinecraftClient.getInstance().player, target);
                        doCrit = false;
                        MinecraftClient.getInstance().player.swingHand(Hand.MAIN_HAND);
                    }
                }
            }
        }
    };
}
