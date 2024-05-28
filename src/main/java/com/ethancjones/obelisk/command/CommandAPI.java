/* ==========================================================
 * Author : Ethan Jones
 * Date   : 28/05/2024
 * TODO   : Nothing
 * Uses   : Used to handle command calls
 * ==========================================================
 */
package com.ethancjones.obelisk.command;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventSendPacket;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.module.ModuleAPI;
import com.ethancjones.obelisk.util.Logger;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandAPI
{
    private static final HashMap<String, ArrayList<Command<?>>> commands = new HashMap<>();

    public static void initialise()
    {
        commands.put("", new ArrayList<>());
        EventAPI.register(onSendPacket);
    }

    public static void register(Command<?> command)
    {
        if (!commands.containsKey(command.getHostString()))
        {
            commands.put(command.getHostString(), new ArrayList<>());
        }
        commands.get(command.getHostString()).add(command);
        Logger.log("Command registered: " + command);
    }

    //Used to determine when a command should be called in the client
    //Default operation is when a chat message is sent beginning with '.'
    private static final Listener<EventSendPacket> onSendPacket = new Listener<>()
    {
        @Override
        public void call(EventSendPacket event)
        {
            if (event.packet instanceof ChatMessageC2SPacket)
            {
                if (((ChatMessageC2SPacket) event.packet).chatMessage().startsWith("."))
                {
                    event.cancelCall();
                    String[] noDotArgs = ((ChatMessageC2SPacket) event.packet).chatMessage().substring(1).split(" ");

                    for (Module module : ModuleAPI.getModules())
                    {
                        if (noDotArgs[0].equalsIgnoreCase(module.getName()))
                        {
                            if (noDotArgs.length == 1)
                            {
                                module.toggle();
                                break;
                            }
                            else
                            {
                                commands.get(module.getName()).forEach(command ->
                                {
                                    if (noDotArgs[1].equalsIgnoreCase(command.getCallString()))
                                    {
                                        command.run(noDotArgs);
                                    }
                                });
                            }
                            break;
                        }
                    }

                    commands.get("").forEach(command ->
                    {
                        if (noDotArgs[0].equalsIgnoreCase(command.getCallString()))
                        {
                            command.run(noDotArgs);
                        }
                    });
                }
            }
        }
    };
}
