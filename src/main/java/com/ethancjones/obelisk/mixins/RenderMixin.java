/* ==========================================================
 * Author : Ethan Jones
 * Date   : 29/05/2024
 * TODO   : Nothing
 * Uses   : Hooks into the game renderer to allow rendering
 * in a 3D environment
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import com.ethancjones.obelisk.event.EventAPI;
import com.ethancjones.obelisk.event.events.EventRender3D;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class RenderMixin
{
    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    public void ias$render(RenderTickCounter tickCounter, CallbackInfo ci)
    {
        //Sets up the rendering environment
        //Reverses translations done to stop view bobbing from affecting rendering
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        RenderSystem.getModelViewStack().pushMatrix();
        Matrix4f matrix4f = new Matrix4f();
        MatrixStack matrixStack = new MatrixStack();
        bobView(matrixStack, camera.getLastTickDelta());
        matrix4f.mul(matrixStack.peek().getPositionMatrix().invert());
        matrix4f.mul(new Matrix4f().rotationXYZ(camera.getPitch() * ((float)Math.PI / 180), camera.getYaw() * ((float)Math.PI / 180) + (float)Math.PI, 0.0f));
        RenderSystem.getModelViewStack().mul(matrix4f);
        RenderSystem.getModelViewStack().translate((float) -camera.getPos().x, (float) -camera.getPos().y, (float) -camera.getPos().z);
        RenderSystem.applyModelViewMatrix();

        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.lineWidth(1.5F);

        EventAPI.call(new EventRender3D(camera.getPos(), MinecraftClient.getInstance().player.getRotationVecClient(), tickCounter));

        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.getModelViewStack().popMatrix();
    }

    @Unique
    private void bobView(MatrixStack matrices, float tickDelta)
    {
        if (!(MinecraftClient.getInstance().getCameraEntity() instanceof PlayerEntity))
        {
            return;
        }
        if (!MinecraftClient.getInstance().options.getBobView().getValue())
        {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity)MinecraftClient.getInstance().getCameraEntity();
        float f = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
        float g = -(playerEntity.horizontalSpeed + f * tickDelta);
        float h = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance);
        matrices.translate(MathHelper.sin(g * (float)Math.PI) * h * 0.5f, -Math.abs(MathHelper.cos(g * (float)Math.PI) * h), 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * (float)Math.PI) * h * 3.0f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * (float)Math.PI - 0.2f) * h) * 5.0f));
    }
}
