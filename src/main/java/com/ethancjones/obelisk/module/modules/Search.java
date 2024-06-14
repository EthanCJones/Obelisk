/* ==========================================================
 * Author : Ethan Jones
 * Date   : 02/06/2024
 * TODO   : Nothing
 * Uses   : Allows the user to search for blocks within the
 * world. Displays their location
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventReceivePacket;
import com.ethancjones.obelisk.event.events.EventRender3D;
import com.ethancjones.obelisk.event.events.EventTick;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.render.RenderUtils;
import com.ethancjones.obelisk.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Search extends Module
{
    private ArrayList<Block> searchBlocks = new ArrayList<>();
    private ConcurrentHashMap<BlockPos, Integer> foundBlocks = new ConcurrentHashMap<>();

    private final Command addSearch;
    private final Command remSearch;
    private final Command clear;

    private final Command<Integer> interval;
    private final Command<Integer> range;
    private final Command<Integer> maxBlocks;

    public Search()
    {
        super("Search", 0, 0);
        addSearch = new Command(getName(), "add")
        {
            @Override
            public void run(String[] args)
            {
                searchBlocks.add(Registries.BLOCK.get(Identifier.of(args[2])));
                ChatUtil.addChatMessage("Added " + args[2] + " to the search");
            }
        };
        remSearch = new Command(getName(), "remove")
        {
            @Override
            public void run(String[] args)
            {
                searchBlocks.remove(Registries.BLOCK.get(Identifier.of(args[2])));
                ChatUtil.addChatMessage("Removed " + args[2] + " from the search");
            }
        };
        clear = new Command(getName(), "clear")
        {
            @Override
            public void run(String[] args)
            {
                searchBlocks.clear();
                foundBlocks.clear();
                ChatUtil.addChatMessage("Cleared the search");
            }
        };
        interval = new Command<>(getName(), "interval", 5000, 1000, 10000);
        range = new Command<>(getName(), "range", 128, 32, 256);
        maxBlocks = new Command<>(getName(), "maxblocks", 20000, 1, 50000);
        EventAPI.register(onTick);
        toggle();
    }

    private final Listener<EventTick> onTick = new Listener<>()
    {
        @Override
        public void call(EventTick event)
        {
            if (event.shouldCall(interval.getValue()))
            {
                foundBlocks.forEach((blockPos, integer) ->
                {
                    if (!searchBlocks.contains(MinecraftClient.getInstance().world.getBlockState(blockPos).getBlock()))
                    {
                        foundBlocks.remove(blockPos);
                    }
                });
                for (int x = -range.getValue(); x < range.getValue(); x++)
                {
                    for (int z = -range.getValue(); z < range.getValue(); z++)
                    {
                        for (int y = 0; y < 256; y++)
                        {
                            int blockX = MinecraftClient.getInstance().player.getBlockX() + x;
                            int blockZ = MinecraftClient.getInstance().player.getBlockZ() + z;
                            BlockPos blockPos = new BlockPos(blockX, y, blockZ);
                            Block block = MinecraftClient.getInstance().world.getBlockState(blockPos).getBlock();
                            if (searchBlocks.contains(block))
                            {
                                foundBlocks.putIfAbsent(blockPos, block.getDefaultMapColor().color);
                                if (foundBlocks.size() >= maxBlocks.getValue())
                                {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    private final Listener<EventRender3D> onRender3D = new Listener<>()
    {
        @Override
        public void call(EventRender3D event)
        {
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            foundBlocks.forEach(((blockPos, integer) ->
            {
                int x = blockPos.getX();
                int y = blockPos.getY();
                int z = blockPos.getZ();

                float red = (integer >> 16 & 0xFF) / 255.0F;
                float green = (integer >> 8 & 0xFF) / 255.0F;
                float blue = (integer & 0xFF) / 255.0F;
                float alpha = 0.2F;

                RenderUtils.render3DBox(buffer, x, y, z, x + 1, y + 1, z + 1, red, green, blue, alpha);
            }));
            RenderUtils.draw(buffer);
        }
    };

    private final Listener<EventReceivePacket> onReceivePacket = new Listener<EventReceivePacket>()
    {
        @Override
        public void call(EventReceivePacket event)
        {
            if (event.packet instanceof BlockUpdateS2CPacket)
            {
                if (((BlockUpdateS2CPacket) event.packet).getState().getBlock() == Blocks.AIR)
                {
                    foundBlocks.remove(((BlockUpdateS2CPacket) event.packet).getPos());
                }
                else if (searchBlocks.contains(((BlockUpdateS2CPacket) event.packet).getState().getBlock()))
                {
                    foundBlocks.put(((BlockUpdateS2CPacket) event.packet).getPos(), ((BlockUpdateS2CPacket) event.packet).getState().getBlock().getDefaultMapColor().color);
                }
            }
        }
    };
}
