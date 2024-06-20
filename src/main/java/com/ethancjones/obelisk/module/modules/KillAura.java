/* ==========================================================
 * Author : Ethan Jones
 * Date   : 18/06/2024
 * TODO   : Nothing
 * Uses   : Used to attack a target close to the player
 * automatically
 * ==========================================================
 */
package com.ethancjones.obelisk.module.modules;

import com.ethancjones.obelisk.Obelisk;
import com.ethancjones.obelisk.command.Command;
import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.Listener;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePost;
import com.ethancjones.obelisk.event.events.EventPlayerUpdatePre;
import com.ethancjones.obelisk.event.events.EventReceivePacket;
import com.ethancjones.obelisk.event.events.EventTick;
import com.ethancjones.obelisk.module.Module;
import com.ethancjones.obelisk.util.AngleUtil;
import com.ethancjones.obelisk.util.ClientInfo;
import com.ethancjones.obelisk.util.ServerInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class KillAura extends Module
{
    private LivingEntity target;
    private final HashMap<Integer, Long> hurtTimes = new HashMap<>();

    private final Command<Double> distance;

    public KillAura()
    {
        super("Kill Aura", 0xFFFF0000, GLFW.GLFW_KEY_K);
        distance = new Command<>(getName(), "distance", 3.5D, 1D, 6D);
        EventAPI.register(onTick);
    }

    private final Listener<EventPlayerUpdatePre> onPlayerUpdatePre = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePre event)
        {
            if (target != null)
            {
                float[] angles = AngleUtil.anglesToEntity(target);

                MinecraftClient.getInstance().player.setYaw(angles[0]);
                MinecraftClient.getInstance().player.setPitch(angles[1]);
            }
        }
    };

    private final Listener<EventPlayerUpdatePost> onPlayerUpdatePost = new Listener<>()
    {
        @Override
        public void call(EventPlayerUpdatePost event)
        {
            MinecraftClient.getInstance().player.setYaw(ClientInfo.yaw);
            MinecraftClient.getInstance().player.setPitch(ClientInfo.pitch);
        }
    };

    private final Listener<EventTick> onTick = new Listener<>()
    {
        @Override
        public void call(EventTick event)
        {
            if (isEnabled())
            {
                LivingEntity potentialTarget = null;
                float priority = 0;

                for (Entity entity : MinecraftClient.getInstance().world.getEntities())
                {
                    if (checkEntity(entity))
                    {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        float tempPriority = getEntityPriority(livingEntity);
                        if (tempPriority > priority)
                        {
                            priority = tempPriority;
                            potentialTarget = livingEntity;
                        }
                    }
                }

                target = potentialTarget;

                if (target != null)
                {
                    hurtTimes.putIfAbsent(target.getId(), 0L);
                    if (!MinecraftClient.getInstance().player.handSwinging)
                    {
                        if (MinecraftClient.getInstance().player.getAttackCooldownProgress(0.5F) >= 1)
                        {
                            if (AngleUtil.lookingAtEntity(target))
                            {
                                if (System.nanoTime() / 1000000 - hurtTimes.get(target.getId()) >= 500)
                                {
                                    MinecraftClient.getInstance().interactionManager.attackEntity(MinecraftClient.getInstance().player, target);
                                    MinecraftClient.getInstance().player.swingHand(Hand.MAIN_HAND);
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    private final Listener<EventReceivePacket> onReceivePacket = new Listener<>()
    {
        @Override
        public void call(EventReceivePacket event)
        {
            if (event.packet instanceof EntityDamageS2CPacket)
            {
                hurtTimes.put(((EntityDamageS2CPacket) event.packet).entityId(), System.nanoTime() / 1000000);
            }
        }
    };

    private boolean checkEntity(Entity entity)
    {
        if (entity == null)
        {
            return false;
        }
        if (!entity.isAlive())
        {
            return false;
        }
        if (entity == MinecraftClient.getInstance().player)
        {
            return false;
        }
        if (MinecraftClient.getInstance().player.distanceTo(entity) > distance.getValue())
        {
            return false;
        }
        if (!MinecraftClient.getInstance().player.canSee(entity))
        {
            return false;
        }
        /*if (ObeliskWrapper.friend.isFriend(ObeliskWrapper.getEntityName(entity)))
        {
            return false;
        }*/
        /*if (entity instanceof PlayerEntity)
        {
            if (Obelisk.MINECRAFT.player.getScoreboardTeam() != null)
            {
                if (Obelisk.MINECRAFT.player.isTeammate(entity))
                {
                    return !team.getValue();
                }
            }
            return players.getValue();
        }
        if (entity instanceof BeeEntity || entity instanceof EndermanEntity || entity instanceof IronGolemEntity || entity instanceof PolarBearEntity || entity instanceof WolfEntity || entity instanceof ZombifiedPiglinEntity)
        {
            if (((Angerable) entity).hasAngerTime())
            {
                return angry.getValue();
            }
            else
            {
                return false;
            }
        }
        if (entity instanceof HostileEntity)
        {
            return mobs.getValue();
        }
        if (entity instanceof TameableEntity)
        {
            return tameable.getValue();
        }
        if (entity instanceof AnimalEntity)
        {
            return animals.getValue();
        }
        if (entity instanceof BoatEntity || entity instanceof MinecartEntity)
        {
            return vehicles.getValue();
        }*/
        return true;
    }

    private float getEntityPriority(LivingEntity entity)
    {
        float priority = 0;

        priority += MinecraftClient.getInstance().player.distanceTo(entity) * 3;

        for (StatusEffectInstance potionEffect : entity.getStatusEffects())
        {
            if (potionEffect.getEffectType().value().isBeneficial())
            {
                priority++;
            }
            else
            {
                priority--;
            }
        }

        priority += entity.getHealth();

        priority += entity.getArmor();

        if (entity instanceof CreeperEntity)
        {
            priority += 100;
        }
        else
        {
            if (entity instanceof SkeletonEntity)
            {
                priority += 200;
            }
            else
            {
                if (entity instanceof MobEntity)
                {
                    priority += 300;
                }
                else
                {
                    priority += 400;
                }
            }
        }

        return priority;
    }
}
