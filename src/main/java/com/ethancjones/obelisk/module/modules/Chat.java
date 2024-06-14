/* ==========================================================
 * Author : Ethan Jones
 * Date   : 31/05/2024
 * TODO   : Nothing
 * Uses   : Changes the default minecraft chat into a nice
 * looking version
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventChatMessage;
import com.ethancjones.obelisk.event.events.EventDisplayScreen;
import com.ethancjones.obelisk.event.events.EventRender2D;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.util.ChatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Chat extends Module
{
    private final LinkedHashMap<Long, Text> chatMessages = new LinkedHashMap<>();

    private int x = 2;
    private int y = -420;

    private final Command<Integer> chatTime;
    private final Command<Integer> chatWidth;
    private int currentChatSize;
    private final Command<Integer> messageCount;

    private int scrollPoint = 0;

    public Chat()
    {
        super("Chat", 0, 0);
        chatTime = new Command<>(getName(), "time", 6000, 1000, 10000);
        chatWidth = new Command<>(getName(), "width", 300, 100, 500);
        messageCount = new Command<>(getName(), "count", 6, 1, 10);
        EventAPI.register(onChatMessage);
        toggle();
    }

    @Override
    protected void onEnable()
    {
        EventAPI.register(onRender2D);
        EventAPI.register(onDisplayScreen);
        if (MinecraftClient.getInstance().inGameHud != null)
        {
            MinecraftClient.getInstance().inGameHud.getChatHud().clear(true);
        }
    }

    @Override
    protected void onDisable()
    {
        EventAPI.deregister(onRender2D);
        EventAPI.deregister(onDisplayScreen);
        Text[] messages = Arrays.copyOf(chatMessages.values().toArray(new Text[] {}), chatMessages.size());
        for (Text message : messages)
        {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
        }
    }

    private final Listener<EventRender2D> onRender2D = new Listener<>()
    {
        @Override
        public void call(EventRender2D event)
        {
            currentChatSize = 0;
            if (y == -420)
            {
                y = (int) (MinecraftClient.getInstance().getWindow().getScaledHeight() * 0.75);
            }

            ArrayList<String> lines = new ArrayList<>();

            long currentTime = System.nanoTime() / 1000000;
            Long[] times = chatMessages.keySet().toArray(new Long[0]);
            for (int point = 0; point < chatMessages.size() - scrollPoint; point++)
            {
                if (currentTime - times[point] < chatTime.getValue() || MinecraftClient.getInstance().currentScreen instanceof ChatScreen)
                {
                    StringBuilder text = new StringBuilder();
                    LinkedList<TextColor> prevColor = new LinkedList<>();
                    prevColor.add(TextColor.fromFormatting(Formatting.WHITE));
                    chatMessages.get(times[point]).asOrderedText().accept(((index, style, codePoint) ->
                    {
                        if (style.getColor() != null)
                        {
                            if (prevColor.getLast() != style.getColor())
                            {
                                prevColor.add(style.getColor());
                                text.append(Formatting.FORMATTING_CODE_PREFIX);
                                text.append(Formatting.byName(style.getColor().getName()).getCode());
                            }
                        }
                        text.append((char) codePoint);
                        return true;
                    }));
                    StringBuilder splitText = new StringBuilder();
                    char prevColorLine = Formatting.WHITE.getCode();
                    String message = text.toString();
                    for (int pos = 0; pos < message.length(); pos++)
                    {
                        char c = message.charAt(pos);
                        if (c == Formatting.FORMATTING_CODE_PREFIX)
                        {
                            prevColorLine = message.charAt(pos + 1);
                        }
                        if (ChatUtil.CHAT.getStringWidth(splitText.toString()) + ChatUtil.CHAT.getStringWidth(String.valueOf(c)) >= chatWidth.getValue())
                        {
                            lines.add(splitText.toString());
                            splitText.delete(0, splitText.length());
                            splitText.append(Formatting.FORMATTING_CODE_PREFIX);
                            splitText.append(prevColorLine);
                        }
                        splitText.append(c);
                    }
                    lines.add(splitText.toString());
                    if (lines.size() > messageCount.getValue())
                    {
                        lines.removeFirst();
                    }
                    currentChatSize = lines.size();
                }
            }

            event.context.fill(x, y + 9, x + chatWidth.getValue(), y - (lines.size() * ChatUtil.CHAT.getFontSize()) + ChatUtil.CHAT.getFontSize(), 0xAA000000);

            int lineY = y;

            for (String message : lines.reversed())
            {
                ChatUtil.CHAT.drawString(event.context, message, x, lineY, true);
                lineY -= ChatUtil.CHAT.getFontSize();
            }
        }
    };

    private final Listener<EventChatMessage> onChatMessage = new Listener<>()
    {
        @Override
        public void call(EventChatMessage event)
        {
            chatMessages.put(System.nanoTime() / 1000000, event.text);
            if (isEnabled())
            {
                event.cancelCall();
            }
        }
    };

    private final Listener<EventDisplayScreen> onDisplayScreen = new Listener<>()
    {
        @Override
        public void call(EventDisplayScreen event)
        {
            if (event.screen instanceof ChatScreen)
            {
                scrollPoint = 0;

                event.screen = new ChatScreen("")
                {
                    @Override
                    public void render(DrawContext context, int mouseX, int mouseY, float delta)
                    {
                        super.render(context, mouseX, mouseY, delta);
                        int renderY = y - currentChatSize * ChatUtil.CHAT.getFontSize() - 2;
                        context.fill(x, renderY, x + chatWidth.getValue(), renderY + ChatUtil.CHAT.getFontSize(), 0xAA000000);
                        ChatUtil.CHAT.drawString(context, "Chat", x + 2, renderY + 1, true);
                    }

                    @Override
                    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
                    {
                        scrollPoint += (int) verticalAmount;
                        if (scrollPoint > chatMessages.size() - messageCount.getValue() + 1)
                        {
                            scrollPoint = chatMessages.size() - messageCount.getValue() + 1;
                        }
                        if (scrollPoint < 0)
                        {
                            scrollPoint = 0;
                        }
                        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
                    }
                };
            }
        }
    };
}
