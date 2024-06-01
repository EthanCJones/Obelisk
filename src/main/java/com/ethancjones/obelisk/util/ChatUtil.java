/* ==========================================================
 * Author : Ethan Jones
 * Date   : 31/05/2024
 * TODO   : Nothing
 * Uses   : A utility class involving the in game chat
 * ==========================================================
 */
package com.ethancjones.obelisk.util;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventInit;
import com.ethancjones.obelisk.gui.TrueTypeFont;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.util.Formatting;

public class ChatUtil
{
    public static TrueTypeFont CHAT;

    public static void initialise()
    {
        EventAPI.register(onInit);
    }

    public static void addChatMessage(String text)
    {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(MutableText.of(new PlainTextContent.Literal(Formatting.AQUA + "[Obelisk]: " + Formatting.WHITE + text)));
    }

    public static boolean isFormatColor(char colorChar)
    {
        return colorChar >= 'a' && colorChar <= 'f' || colorChar >= '0' && colorChar <= '9';
    }

    private static final Listener<EventInit> onInit = new Listener<>()
    {
        @Override
        public void call(EventInit event)
        {
            CHAT = new TrueTypeFont("Verdana", 9);
        }
    };
}
