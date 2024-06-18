/* ==========================================================
 * Author : Ethan Jones
 * Date   : 02/06/2024
 * TODO   : Nothing
 * Uses   : Allows the player to look around freely
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.*;
import com.ethancjones.obelisk.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Freecam extends Module
{
    private float forward;
    private float sideways;

    private float initYaw;
    private float initPitch;

    private double x;
    private double y;
    private double z;

    private Command<Double> speed;

    public Freecam()
    {
        super("Freecam", 0xFF00FFFF, GLFW.GLFW_KEY_C);
        speed = new Command<>(getName(), "speed", 0.3D, 0.1D, 1D);
        EventAPI.register(onTick);
    }

    @Override
    protected void onEnable()
    {
        super.onEnable();
        initYaw = MinecraftClient.getInstance().gameRenderer.getCamera().getYaw();
        initPitch = MinecraftClient.getInstance().gameRenderer.getCamera().getPitch();
        x = MinecraftClient.getInstance().gameRenderer.getCamera().getPos().x;
        y = MinecraftClient.getInstance().gameRenderer.getCamera().getPos().y;
        z = MinecraftClient.getInstance().gameRenderer.getCamera().getPos().z;
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
            if (MinecraftClient.getInstance().player.isDead())
            {
                toggle();
            }

            event.pos = new Vec3d(x, y, z);
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

    private final Listener<EventTick> onTick = new Listener<>()
    {
        @Override
        public void call(EventTick event)
        {
            if (isEnabled())
            {
                if (event.shouldCall(10))
                {
                    if (MinecraftClient.getInstance().options.jumpKey.isPressed())
                    {
                        y += speed.getValue();
                    }
                    if (MinecraftClient.getInstance().options.sneakKey.isPressed())
                    {
                        y -= speed.getValue();
                    }

                    float yaw = MinecraftClient.getInstance().gameRenderer.getCamera().getYaw();

                    x += forward * -Math.sin(Math.toRadians(yaw)) * speed.getValue();
                    z += forward * Math.cos(Math.toRadians(yaw)) * speed.getValue();

                    x += sideways * -Math.sin(Math.toRadians(yaw - 90)) * speed.getValue();
                    z += sideways * Math.cos(Math.toRadians(yaw - 90)) * speed.getValue();
                }
            }
        }
    };
}
