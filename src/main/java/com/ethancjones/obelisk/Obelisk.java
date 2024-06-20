/* ==========================================================
 * Author : Ethan Jones
 * Date   : 27/05/2024
 * TODO   : Nothing
 * Uses   : Called when the game starts by the fabric API to
 * initialise the client
 * ==========================================================
 */
package com.ethancjones.obelisk;

import com.ethancjones.obelisk.command.CommandAPI;
import com.ethancjones.obelisk.keybind.KeybindAPI;
import com.ethancjones.obelisk.module.ModuleAPI;
import com.ethancjones.obelisk.util.ChatUtil;
import com.ethancjones.obelisk.util.ServerInfo;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Obelisk implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        KeybindAPI.initialise();
        CommandAPI.initialise();
        ModuleAPI.initialise();
        ChatUtil.initialise();
        ServerInfo.initialise();
    }

    //Sends an invalid movement to the server
    //Can reset anti cheat timers
    public static void sendInvalidMovement()
    {
        if (!MinecraftClient.getInstance().isInSingleplayer())
        {
            double x = MinecraftClient.getInstance().player.getX();
            double z = MinecraftClient.getInstance().player.getZ();
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, Double.NEGATIVE_INFINITY, z, false));
        }
    }
}
