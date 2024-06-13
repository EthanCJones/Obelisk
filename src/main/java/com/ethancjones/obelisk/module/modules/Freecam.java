/* ==========================================================
 * Author : Ethan Jones
 * Date   : 02/06/2024
 * TODO   : Nothing
 * Uses   : Allows the player to look around freely
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventCamera;
import com.ethancjones.obelisk.event.events.EventKeyboardInput;
import com.ethancjones.obelisk.event.events.EventRender2D;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

public class Freecam extends Module
{
    private float forward;
    private float sideways;

    private float initYaw;
    private float initPitch;

    private double xDelta;
    private double yDelta;
    private double zDelta;

    private Command<Double> speed;

    public Freecam()
    {
        super("Freecam", 0xFF00FFFF, GLFW.GLFW_KEY_C);
        speed = new Command<>(getName(), "speed", 0.3D, 0.1D, 1D);
    }

    @Override
    protected void onEnable()
    {
        super.onEnable();
        initYaw = MinecraftClient.getInstance().player.getYaw();
        initPitch = MinecraftClient.getInstance().player.getPitch();
        xDelta = 0;
        yDelta = 0;
        zDelta = 0;
        MinecraftClient.getInstance().gameRenderer.setRenderHand(false);
        MinecraftClient.getInstance().chunkCullingEnabled = false;
        MinecraftClient.getInstance().worldRenderer.reload();
    }

    @Override
    protected void onDisable()
    {
        super.onDisable();
        MinecraftClient.getInstance().gameRenderer.setRenderHand(true);
        MinecraftClient.getInstance().player.setYaw(initYaw);
        MinecraftClient.getInstance().player.setPitch(initPitch);
        MinecraftClient.getInstance().chunkCullingEnabled = true;
        MinecraftClient.getInstance().worldRenderer.reload();
    }

    private final Listener<EventCamera> onCamera = new Listener<>()
    {
        @Override
        public void call(EventCamera event)
        {
            if (MinecraftClient.getInstance().options.jumpKey.isPressed())
            {
                yDelta += speed.getValue() * event.tickDelta;
            }
            if (MinecraftClient.getInstance().options.sneakKey.isPressed())
            {
                yDelta -= speed.getValue() * event.tickDelta;
            }
            xDelta += forward * -Math.sin(Math.toRadians(MinecraftClient.getInstance().player.getYaw())) * event.tickDelta * speed.getValue();
            zDelta += forward * Math.cos(Math.toRadians(MinecraftClient.getInstance().player.getYaw())) * event.tickDelta * speed.getValue();

            xDelta += sideways * -Math.sin(Math.toRadians(MinecraftClient.getInstance().player.getYaw() - 90)) * event.tickDelta * speed.getValue();
            zDelta += sideways * Math.cos(Math.toRadians(MinecraftClient.getInstance().player.getYaw() - 90)) * event.tickDelta * speed.getValue();

            event.pos = event.pos.add(xDelta, yDelta, zDelta);
        }
    };

    private final Listener<EventKeyboardInput> onKeyboardInput = new Listener<>(Listener.Priority.HIGHEST)
    {
        @Override
        public void call(EventKeyboardInput event)
        {
            forward = MinecraftClient.getInstance().player.input.movementForward;
            sideways = MinecraftClient.getInstance().player.input.movementSideways;
            if (event.slowDown)
            {
                forward /= event.factor;
                sideways /= event.factor;
            }
            MinecraftClient.getInstance().player.input.movementForward = 0;
            MinecraftClient.getInstance().player.input.movementSideways = 0;
            MinecraftClient.getInstance().player.input.jumping = false;
            endCall();
        }
    };

    private final Listener<EventRender2D> onRender2D = new Listener<>()
    {
        @Override
        public void call(EventRender2D event)
        {
            event.cancelCall();
        }
    };
}
