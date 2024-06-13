/* ==========================================================
 * Author : Ethan Jones
 * Date   : 07/06/2024
 * TODO   : Nothing
 * Uses   :
 * ==========================================================
 */
package com.ethancjones.obelisk.render;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.util.math.Box;

import java.awt.geom.AffineTransform;

public class RenderUtils
{
    public static void render3DBox(BufferBuilder buffer, double x, double y, double z, double x1, double y1, double z1, float red, float green, float blue, float alpha)
    {
        buffer.vertex(x, y, z).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y, z).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x, y, z1).color(red, green, blue, alpha).next();

        buffer.vertex(x, y1, z).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y1, z).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x, y1, z1).color(red, green, blue, alpha).next();

        buffer.vertex(x, y, z).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y, z).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y1, z).color(red, green, blue, alpha).next();
        buffer.vertex(x, y1, z).color(red, green, blue, alpha).next();

        buffer.vertex(x, y, z).color(red, green, blue, alpha).next();
        buffer.vertex(x, y, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x, y1, z).color(red, green, blue, alpha).next();

        buffer.vertex(x1, y, z).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y1, z).color(red, green, blue, alpha).next();

        buffer.vertex(x, y, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(x, y1, z1).color(red, green, blue, alpha).next();
    }

    public static void render3DBoxWithRotation(BufferBuilder buffer, Box box, double rotation, float red, float green, float blue, float alpha)
    {
        rotation = Math.toRadians(rotation);

        double sin = Math.sin(rotation);
        double cos = Math.cos(rotation);

        double centreX = box.minX + (box.maxX - box.minX) / 2;
        double centreZ = box.minZ + (box.maxZ - box.minZ) / 2;
        double minX = box.minX - centreX;
        double minZ = box.minZ - centreZ;
        double maxX = box.maxX - centreX;
        double maxZ = box.maxZ - centreZ;

        double renderMinX = minX * cos - minZ * sin;
        double renderMinZ = minX * sin + minZ * cos;
        double renderMinX1 = minX * cos - maxZ * sin;
        double renderMinZ1 = minX * sin + maxZ * cos;
        double renderMaxX = maxX * cos - maxZ * sin;
        double renderMaxZ = maxX * sin + maxZ * cos;
        double renderMaxX1 = maxX * cos - minZ * sin;
        double renderMaxZ1 = maxX * sin + minZ * cos;

        renderMinX += centreX;
        renderMinZ += centreZ;
        renderMinX1 += centreX;
        renderMinZ1 += centreZ;
        renderMaxX += centreX;
        renderMaxZ += centreZ;
        renderMaxX1 += centreX;
        renderMaxZ1 += centreZ;

        buffer.vertex(renderMinX, box.minY, renderMinZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMinX1, box.minY, renderMinZ1).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX, box.minY, renderMaxZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX1, box.minY, renderMaxZ1).color(red, green, blue, alpha).next();

        buffer.vertex(renderMinX, box.maxY, renderMinZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMinX1, box.maxY, renderMinZ1).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX, box.maxY, renderMaxZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX1, box.maxY, renderMaxZ1).color(red, green, blue, alpha).next();

        buffer.vertex(renderMinX, box.minY, renderMinZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMinX1, box.minY, renderMinZ1).color(red, green, blue, alpha).next();
        buffer.vertex(renderMinX1, box.maxY, renderMinZ1).color(red, green, blue, alpha).next();
        buffer.vertex(renderMinX, box.maxY, renderMinZ).color(red, green, blue, alpha).next();

        buffer.vertex(renderMinX1, box.minY, renderMinZ1).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX, box.minY, renderMaxZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX, box.maxY, renderMaxZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMinX1, box.maxY, renderMinZ1).color(red, green, blue, alpha).next();

        buffer.vertex(renderMaxX, box.minY, renderMaxZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX1, box.minY, renderMaxZ1).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX1, box.maxY, renderMaxZ1).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX, box.maxY, renderMaxZ).color(red, green, blue, alpha).next();

        buffer.vertex(renderMaxX1, box.minY, renderMaxZ1).color(red, green, blue, alpha).next();
        buffer.vertex(renderMinX, box.minY, renderMinZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMinX, box.maxY, renderMinZ).color(red, green, blue, alpha).next();
        buffer.vertex(renderMaxX1, box.maxY, renderMaxZ1).color(red, green, blue, alpha).next();
    }
}
