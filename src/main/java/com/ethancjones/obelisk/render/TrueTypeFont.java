/* ==========================================================
 * Author : Ethan Jones
 * Date   : 31/05/2024
 * TODO   : Nothing
 * Uses   : Allows rendering of TrueTypeFont within the
 * Minecraft engine
 * ==========================================================
 */
package com.ethancjones.obelisk.render;

import com.ethancjones.obelisk.util.ChatUtil;
import com.ethancjones.obelisk.util.Logger;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TrueTypeFont
{
    private final char glyphEnd = 192;
    private final int scales = 5;
    private final float[] textureSizeX = new float[scales];
    private final float[] textureSizeY = new float[scales];
    private final Identifier[] resource = new Identifier[scales];
    private final GlyphVector[] glyphVectors = new GlyphVector[scales];
    private final float[] ascent = new float[scales];
    private final float[] descent = new float[scales];
    private final int fontSize;

    public TrueTypeFont(String fontName, int size)
    {
        fontSize = size;
        StringBuilder stringBuilder = new StringBuilder();
        for (char character = 0; character < glyphEnd; character++)
        {
            stringBuilder.append(character);
        }
        for (int scale = 1; scale < scales; ++scale)
        {
            Font font = new Font(fontName, Font.PLAIN, size * scale);
            GlyphVector glyphVector = font.layoutGlyphVector(new FontRenderContext(null, true, true), stringBuilder.toString().toCharArray(), 0, stringBuilder.length(), Font.LAYOUT_LEFT_TO_RIGHT);
            glyphVectors[scale] = glyphVector;
            double xPos = 0;
            double baseline = 0;
            for (int character = 0; character < glyphEnd; character++)
            {
                if (character == 111)
                {
                    baseline = glyphVector.getGlyphMetrics(character).getBounds2D().getMaxY();
                }
                else
                {
                    if (character == 103)
                    {
                        ascent[scale] = (float) glyphVector.getGlyphMetrics(character).getBounds2D().getHeight();
                        descent[scale] = (float) (glyphVector.getGlyphMetrics(character).getBounds2D().getMaxY() - baseline);
                    }
                }
                glyphVector.setGlyphPosition(character, new Point2D.Double(xPos, size * scale));
                xPos += glyphVector.getGlyphLogicalBounds(character).getBounds().getWidth() * 1.1;
            }
            textureSizeX[scale] = (float) glyphVector.getLogicalBounds().getWidth();
            textureSizeY[scale] = (float) (glyphVector.getLogicalBounds().getHeight() + descent[scale]);
            BufferedImage bufferedImage = new BufferedImage((int) textureSizeX[scale], (int) textureSizeY[scale], BufferedImage.TYPE_INT_ARGB);
            ((Graphics2D) bufferedImage.getGraphics()).drawGlyphVector(glyphVector, 0, 0);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try
            {
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            }
            catch (IOException e)
            {
                Logger.log("Could not create image for font");
            }
            try
            {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                NativeImage nativeImage = NativeImage.read(byteArrayInputStream);
                resource[scale] = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("ttf", new NativeImageBackedTexture(nativeImage));
            }
            catch (IOException e)
            {
                Logger.log("Could not generate font texture");
            }
        }
    }

    public void drawString(DrawContext context, String text, float x, float y, boolean shadow)
    {
        int scale = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
        x *= scale;
        y *= scale;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, resource[scale]);
        RenderSystem.enableBlend();
        context.getMatrices().push();
        context.getMatrices().scale(1.0F / scale, 1.0F / scale, 1.0F / scale);
        Tessellator.getInstance().getBuffer().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        int pos = 0;
        int color = Formatting.byCode('f').getColorValue();
        while (pos < text.length())
        {
            if (text.codePointAt(pos) == 167)
            {
                pos++;
                if (pos > text.length())
                {
                    break;
                }
                if (ChatUtil.isFormatColor(text.charAt(pos)))
                {
                    color = Formatting.byCode(text.charAt(pos)).getColorValue();
                }
            }
            else
            {
                int codePoint = text.codePointAt(pos);
                if (codePoint > glyphEnd)
                {
                    pos++;
                    continue;
                }
                if (shadow)
                {
                    renderChar(context, scale, codePoint, x + 2, y + 2, 0, 0, 0, 1);
                }
                renderChar(context, scale, codePoint, x, y, (float)(color >> 16) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, 1);
                x += glyphVectors[scale].getGlyphMetrics(codePoint).getAdvanceX();
            }
            pos++;
        }
        Tessellator.getInstance().draw();
        context.getMatrices().pop();
        RenderSystem.disableBlend();
    }

    public void renderChar(DrawContext context, int scale, int character, float x, float y, float r, float g, float b, float a)
    {
        float coordX = (float) glyphVectors[scale].getGlyphPosition(character).getX();
        float coordY = (float) (glyphVectors[scale].getGlyphPosition(character).getY() - ascent[scale]);
        float advance = glyphVectors[scale].getGlyphMetrics(character).getAdvanceX();
        float endX = x + advance;
        float endY = y + ascent[scale] + descent[scale];
        float textureX = coordX / textureSizeX[scale];
        float textureY = coordY / textureSizeY[scale];
        float endTextureX = (coordX + advance) / textureSizeX[scale];
        float endTextureY = (coordY + ascent[scale] + descent[scale]) / textureSizeY[scale];
        Tessellator.getInstance().getBuffer().vertex(context.getMatrices().peek().getPositionMatrix(), x, endY, 0).texture(textureX, endTextureY).color(r, g, b, a).next();
        Tessellator.getInstance().getBuffer().vertex(context.getMatrices().peek().getPositionMatrix(), endX, endY, 0).texture(endTextureX, endTextureY).color(r, g, b, a).next();
        Tessellator.getInstance().getBuffer().vertex(context.getMatrices().peek().getPositionMatrix(), endX, y, 0).texture(endTextureX, textureY).color(r, g, b, a).next();
        Tessellator.getInstance().getBuffer().vertex(context.getMatrices().peek().getPositionMatrix(), x, y, 0).texture(textureX, textureY).color(r, g, b, a).next();
    }

    public void drawCentredString(DrawContext context, String text, float x, float y, boolean shadow)
    {
        drawString(context, text, x - getStringWidth(text) / 2, y, shadow);
    }

    public void drawSheet(DrawContext context)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        int scale = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
        RenderSystem.setShaderTexture(0, resource[scale]);
        RenderSystem.enableBlend();
        context.getMatrices().push();
        context.getMatrices().scale(1.0F / scale, 1.0F / scale, 1.0F / scale);
        Tessellator.getInstance().getBuffer().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        Tessellator.getInstance().getBuffer().vertex(context.getMatrices().peek().getPositionMatrix(), 0, textureSizeY[scale], 0).texture(0, 1).next();
        Tessellator.getInstance().getBuffer().vertex(context.getMatrices().peek().getPositionMatrix(), textureSizeX[scale], textureSizeY[scale], 0).texture(1, 1).next();
        Tessellator.getInstance().getBuffer().vertex(context.getMatrices().peek().getPositionMatrix(), textureSizeX[scale], 0, 0).texture(1, 0).next();
        Tessellator.getInstance().getBuffer().vertex(context.getMatrices().peek().getPositionMatrix(), 0, 0, 0).texture(0, 0).next();
        Tessellator.getInstance().draw();
        context.getMatrices().pop();
        RenderSystem.disableBlend();
    }

    public float getStringWidth(String text)
    {
        int scale = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
        double width = 0;
        String formattedText = Formatting.strip(text);
        int pos = 0;
        while (pos < formattedText.length())
        {
            int codePoint = formattedText.codePointAt(pos);
            if (codePoint >= glyphEnd)
            {
                pos++;
                continue;
            }
            width += glyphVectors[scale].getGlyphMetrics(codePoint).getAdvanceX();
            pos++;
        }
        width += 2 * scale;
        return (float) (width / scale);
    }

    public int getFontSize()
    {
        return fontSize;
    }
}

