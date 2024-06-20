/* ==========================================================
 * Author : Ethan Jones
 * Date   : 19/06/2024
 * TODO   : Nothing
 * Uses   : Stores useful data about the server such a
 * the player's position serverside and TPS/Ping.
 * ==========================================================
 */
package com.ethancjones.obelisk.util;

import com.ethancjones.obelisk.Obelisk;
import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventReceivePacket;
import com.ethancjones.obelisk.event.events.EventTick;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class ServerInfo
{
    public static double x;
    public static double y;
    public static double z;
    public static float yaw;
    public static float pitch;
    public static boolean onGround;

    private static long lastTPSTime = 1;
    public static double TPS;
    public static int ping;
    private static long pingTime;
    private static boolean pinging;

    public static void initialise()
    {
        EventAPI.register(onTick);
        EventAPI.register(onReceivePacket);
    }

    private static Listener<EventTick> onTick = new Listener<>()
    {
        @Override
        public void call(EventTick event)
        {
            if (event.shouldCall(1000))
            {
                if (pinging)
                {
                    if (System.nanoTime() / 1000000 - pingTime > 10000)
                    {
                        pinging = false;
                    }
                }
                else
                {
                    if (MinecraftClient.getInstance().getNetworkHandler() != null)
                    {
                        pinging = true;
                        pingTime = System.nanoTime() / 1000000;
                        MinecraftClient.getInstance().getNetworkHandler().sendPacket(new RequestCommandCompletionsC2SPacket(69420, ""));
                    }
                }
            }
        }
    };

    private static Listener<EventReceivePacket> onReceivePacket = new Listener<>()
    {
        @Override
        public void call(EventReceivePacket event)
        {
            if (event.packet instanceof WorldTimeUpdateS2CPacket)
            {
                if (System.nanoTime() / 1000000 - lastTPSTime > 0)
                {
                    double updateTime = System.nanoTime() / 1000000 - lastTPSTime;
                    double seconds = updateTime / 1000;
                    TPS = 20.0 / seconds;
                    lastTPSTime = System.nanoTime() / 1000000;
                }
            }
            if (event.packet instanceof CommandSuggestionsS2CPacket)
            {
                if (((CommandSuggestionsS2CPacket) event.packet).id() == 69420)
                {
                    ping = (int) (System.nanoTime() / 1000000 - pingTime);
                    pinging = false;
                }
            }
        }
    };
}
