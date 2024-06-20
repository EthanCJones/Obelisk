/* ==========================================================
 * Author : Ethan Jones
 * Date   : 19/06/2024
 * TODO   : Nothing
 * Uses   : Utilities for finding angles
 * ==========================================================
 */
package com.ethancjones.obelisk.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

public class AngleUtil
{
    public static float[] anglesToEntity(LivingEntity entity)
    {
        double x = entity.getX() - MinecraftClient.getInstance().player.getX();
        double y = entity.getEyeY() - MinecraftClient.getInstance().player.getEyeY();
        double z = entity.getZ() - MinecraftClient.getInstance().player.getZ();

        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z)));

        return new float[] {yaw, pitch};
    }

    public static boolean lookingAtEntity(LivingEntity entity)
    {
        float[] angles = anglesToEntity(entity);

        float deltaYaw = Math.abs(angles[0] - ServerInfo.yaw);
        float deltaPitch = Math.abs(angles[1] - ServerInfo.pitch);

        if (deltaYaw < 30 && deltaPitch < 30)
        {
            return true;
        }

        return false;
    }
}
