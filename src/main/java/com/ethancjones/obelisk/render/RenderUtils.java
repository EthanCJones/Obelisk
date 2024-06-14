/* ==========================================================
 * Author : Ethan Jones
 * Date   : 07/06/2024
 * TODO   : Nothing
 * Uses   :
 * ==========================================================
 */
package com.ethancjones.obelisk.render;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.util.math.Box;

import java.awt.geom.AffineTransform;

public class RenderUtils
{
    public static void render3DBox(BufferBuilder buffer, float x, float y, float z, float x1, float y1, float z1, float red, float green, float blue, float alpha)
    {
        buffer.vertex(x, y, z).color(red, green, blue, alpha);
        buffer.vertex(x1, y, z).color(red, green, blue, alpha);
        buffer.vertex(x1, y, z1).color(red, green, blue, alpha);
        buffer.vertex(x, y, z1).color(red, green, blue, alpha);

        buffer.vertex(x, y1, z).color(red, green, blue, alpha);
        buffer.vertex(x1, y1, z).color(red, green, blue, alpha);
        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha);
        buffer.vertex(x, y1, z1).color(red, green, blue, alpha);

        buffer.vertex(x, y, z).color(red, green, blue, alpha);
        buffer.vertex(x1, y, z).color(red, green, blue, alpha);
        buffer.vertex(x1, y1, z).color(red, green, blue, alpha);
        buffer.vertex(x, y1, z).color(red, green, blue, alpha);

        buffer.vertex(x, y, z).color(red, green, blue, alpha);
        buffer.vertex(x, y, z1).color(red, green, blue, alpha);
        buffer.vertex(x, y1, z1).color(red, green, blue, alpha);
        buffer.vertex(x, y1, z).color(red, green, blue, alpha);

        buffer.vertex(x1, y, z).color(red, green, blue, alpha);
        buffer.vertex(x1, y, z1).color(red, green, blue, alpha);
        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha);
        buffer.vertex(x1, y1, z).color(red, green, blue, alpha);

        buffer.vertex(x, y, z1).color(red, green, blue, alpha);
        buffer.vertex(x1, y, z1).color(red, green, blue, alpha);
        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha);
        buffer.vertex(x, y1, z1).color(red, green, blue, alpha);
    }

    public static void render3DBoxWithRotation(BufferBuilder buffer, Box box, double rotation, float red, float green, float blue, float alpha)
    {
        rotation = Math.toRadians(rotation);

        float sin = (float) Math.sin(rotation);
        float cos = (float) Math.cos(rotation);

        float centreX = (float) (box.minX + (box.maxX - box.minX) / 2);
        float centreZ = (float) (box.minZ + (box.maxZ - box.minZ) / 2);
        float minX = (float) (box.minX - centreX);
        float minY = (float) box.minY;
        float minZ = (float) (box.minZ - centreZ);
        float maxX = (float) (box.maxX - centreX);
        float maxY = (float) box.maxY;
        float maxZ = (float) (box.maxZ - centreZ);

        float renderMinX = minX * cos - minZ * sin;
        float renderMinZ = minX * sin + minZ * cos;
        float renderMinX1 = minX * cos - maxZ * sin;
        float renderMinZ1 = minX * sin + maxZ * cos;
        float renderMaxX = maxX * cos - maxZ * sin;
        float renderMaxZ = maxX * sin + maxZ * cos;
        float renderMaxX1 = maxX * cos - minZ * sin;
        float renderMaxZ1 = maxX * sin + minZ * cos;

        renderMinX += centreX;
        renderMinZ += centreZ;
        renderMinX1 += centreX;
        renderMinZ1 += centreZ;
        renderMaxX += centreX;
        renderMaxZ += centreZ;
        renderMaxX1 += centreX;
        renderMaxZ1 += centreZ;

        buffer.vertex(renderMinX, minY, renderMinZ).color(red, green, blue, alpha);
        buffer.vertex(renderMinX1, minY, renderMinZ1).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX, minY, renderMaxZ).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX1, minY, renderMaxZ1).color(red, green, blue, alpha);

        buffer.vertex(renderMinX, maxY, renderMinZ).color(red, green, blue, alpha);
        buffer.vertex(renderMinX1, maxY, renderMinZ1).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX, maxY, renderMaxZ).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX1, maxY, renderMaxZ1).color(red, green, blue, alpha);

        buffer.vertex(renderMinX, minY, renderMinZ).color(red, green, blue, alpha);
        buffer.vertex(renderMinX1, minY, renderMinZ1).color(red, green, blue, alpha);
        buffer.vertex(renderMinX1, maxY, renderMinZ1).color(red, green, blue, alpha);
        buffer.vertex(renderMinX, maxY, renderMinZ).color(red, green, blue, alpha);

        buffer.vertex(renderMinX1, minY, renderMinZ1).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX, minY, renderMaxZ).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX, maxY, renderMaxZ).color(red, green, blue, alpha);
        buffer.vertex(renderMinX1, maxY, renderMinZ1).color(red, green, blue, alpha);

        buffer.vertex(renderMaxX, minY, renderMaxZ).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX1, minY, renderMaxZ1).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX1, maxY, renderMaxZ1).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX, maxY, renderMaxZ).color(red, green, blue, alpha);

        buffer.vertex(renderMaxX1, minY, renderMaxZ1).color(red, green, blue, alpha);
        buffer.vertex(renderMinX, minY, renderMinZ).color(red, green, blue, alpha);
        buffer.vertex(renderMinX, maxY, renderMinZ).color(red, green, blue, alpha);
        buffer.vertex(renderMaxX1, maxY, renderMaxZ1).color(red, green, blue, alpha);
    }

    public static void draw(BufferBuilder buffer)
    {
        BuiltBuffer builtBuffer = buffer.endNullable();
        if (builtBuffer != null)
        {
            BufferRenderer.drawWithGlobalProgram(builtBuffer);
        }
    }
}
