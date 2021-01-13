package com.github.alexthe666.astro.client.render.tile;

import com.github.alexthe666.astro.client.model.ModelBlockitWorm;
import com.github.alexthe666.astro.client.model.TabulaModels;
import com.github.alexthe666.astro.client.render.AstroRenderTypes;
import com.github.alexthe666.astro.client.render.entity.RenderSpaceSquid;
import com.github.alexthe666.astro.client.render.entity.layer.LayerSpaceSquidGlow;
import com.github.alexthe666.astro.server.block.BlockBlockitWormHole;
import com.github.alexthe666.astro.server.entity.tileentity.TileEntityBlockitHole;
import com.github.alexthe666.astro.server.entity.tileentity.TileEntitySquidTank;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderSquidTank extends TileEntityRenderer<TileEntitySquidTank> {

    private static final AxisAlignedBB OOZEBB = new AxisAlignedBB(-1.4F, -2.4F, -1.4F, 1.4F, 2.4F, 1.4F);
    private static final ResourceLocation OOZE_TEXTURE = new ResourceLocation("astro:textures/block/squid_tank_inside.png");
    private static final RenderType OOZE_TYPE = RenderType.getEntityTranslucentCull(OOZE_TEXTURE);
    public RenderSquidTank(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntitySquidTank te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn){
        float rotation = 0;
        float xRot = 0;
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutout(RenderSpaceSquid.TEAL));
        matrixStackIn.push();
        matrixStackIn.translate(0.5F, -0.25F, 0.5F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, rotation, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, xRot, true));
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
        matrixStackIn.scale(0.9F, 0.9F, 0.9F);
        animateSquid(partialTicks + te.ticksExisted);
        TabulaModels.SPACE_SQUID_TANK.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(LayerSpaceSquidGlow.TEAL);
        TabulaModels.SPACE_SQUID_TANK.render(matrixStackIn, ivertexbuilder2, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        matrixStackIn.pop();


        matrixStackIn.push();
        matrixStackIn.translate(0.5F, 0.5F, 0.5F);
        Minecraft.getInstance().getTextureManager().bindTexture(OOZE_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        renderAABB(bufferIn, OOZEBB, matrixStackIn, combinedLightIn, combinedOverlayIn);
        RenderSystem.disableBlend();
        matrixStackIn.pop();
    }

    private void animateSquid(float ageInTicks) {
        TabulaModel model = TabulaModels.SPACE_SQUID_TANK;
        float deadSpeed = 0.035F;
        model.resetToDefaultPose();
        model.swing(model.getCube("tenticleA1"), deadSpeed, 0.1F, false, 0, -0.1F, ageInTicks, 1);
        model.swing(model.getCube("tenticleB1"), deadSpeed, 0.1F, false, 1, 0, ageInTicks, 1);
        model.swing(model.getCube("tenticleA2"), deadSpeed, 0.1F, false, 1, 0.1F, ageInTicks, 1);
        model.walk(model.getCube("tenticleA2"), deadSpeed, 0.1F, false, 1, -0.1F, ageInTicks, 1);
        model.walk(model.getCube("tenticleB2Big"), deadSpeed, 0.05F, false, 0, -0.1F, ageInTicks, 1);
        model.walk(model.getCube("tenticleC2Big"), deadSpeed, 0.1F, true, 1, -0.2F, ageInTicks, 1);
        model.walk(model.getCube("suckerD2"), deadSpeed, 0.1F, true, 1, 0.2F, ageInTicks, 1);
        model.walk(model.getCube("tenticleA3"), deadSpeed, 0.1F, false, -3, -0.1F, ageInTicks, 1);
        model.walk(model.getCube("tenticleB3"), deadSpeed, 0.1F, false, -2, 0, ageInTicks, 1);
        model.swing(model.getCube("tenticleA4"), deadSpeed, 0.1F, false, -2, 0.1F, ageInTicks, 1);
        model.swing(model.getCube("tenticleB4"), deadSpeed, 0.1F, false, -3, 0, ageInTicks, 1);
        model.swing(model.getCube("tenticleA5"), deadSpeed, 0.1F, false, -1, -0.2F, ageInTicks, 1);
        model.swing(model.getCube("tenticleB5"), deadSpeed, 0.1F, false, -1, 0, ageInTicks, 1);
        model.swing(model.getCube("tenticleA6"), deadSpeed, 0.1F, false, -1, -0.0F, ageInTicks, 1);
        model.swing(model.getCube("tenticleB6"), deadSpeed, 0.1F, false, -3, -0.1F, ageInTicks, 1);
        model.walk(model.getCube("tenticleA7"), deadSpeed, 0.1F, false, 1, 0.1F, ageInTicks, 1);
        model.walk(model.getCube("tenticleB7Big"), deadSpeed, 0.1F, false, 0, -0.1F, ageInTicks, 1);
        model.walk(model.getCube("tenticleC7Big"), deadSpeed, 0.1F, false, -1, -0.1F, ageInTicks, 1);
        model.swing(model.getCube("tenticleA8"), deadSpeed, 0.1F, false, 3, -0.2F, ageInTicks, 1);
        model.swing(model.getCube("tenticleB8"), deadSpeed, 0.1F, false, 2, 0, ageInTicks, 1);
        model.swing(model.getCube("siphon"), deadSpeed, 0.3F, false, 1, -0.2F, ageInTicks, 1);
        float bodyScale = 1 + 0.05F * (float) Math.sin(ageInTicks * deadSpeed * 2 + 3.5F);
        model.getCube("body1").setShouldScaleChildren(false);
        model.getCube("body1").setScale(bodyScale, bodyScale, bodyScale);
        model.getCube("body2").rotationPointZ -= bodyScale * 2;
        model.getCube("body2").setScale(bodyScale, bodyScale, bodyScale);
        model.getCube("body3").rotationPointZ -= bodyScale * 2;
        float siphonScale = 1 + 0.25F * (float) Math.sin(ageInTicks * deadSpeed * 2 + 2.5F);
        model.getCube("siphon").setScale(siphonScale, siphonScale, siphonScale);
        model.getCube("body1").rotationPointY -= (float) Math.sin(ageInTicks * 0.05F) * 3F;
    }

    public static void renderAABB(IRenderTypeBuffer bufferIn, AxisAlignedBB boundingBox, MatrixStack matrixStack, int combinedLight, int overlay) {
        matrixStack.push();
        float f3 = (float) (System.currentTimeMillis() % 3000L) / 3000.0F;
        float maxX = (float)boundingBox.maxX * 0.5F;
        float minX = (float)boundingBox.minX * 0.5F;
        float maxY = (float)boundingBox.maxY * 0.5F;
        float minY = (float)boundingBox.minY * 0.5F;
        float maxZ = (float)boundingBox.maxZ * 0.5F;
        float minZ = (float)boundingBox.minZ * 0.5F;
        IVertexBuilder vertexbuffer = bufferIn.getBuffer(AstroRenderTypes.func_239272_l_(OOZE_TEXTURE));
        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + maxY - minY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f,  (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + maxY - minY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f,  (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f,  (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, -1.0F).endVertex();

        vertexbuffer.pos(matrix4f,  (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + maxY - minY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + maxY - minY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, 1.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + maxZ - minZ).overlay(overlay).lightmap(combinedLight).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + maxZ - minZ).overlay(overlay).lightmap(combinedLight).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + maxZ - minZ).overlay(overlay).lightmap(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + maxZ - minZ).overlay(overlay).lightmap(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + maxY - minY).overlay(overlay).lightmap(combinedLight).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + maxY - minY).overlay(overlay).lightmap(combinedLight).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX,  (float)boundingBox.minY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.minZ).color(255, 255, 255, 255).tex(0 + minX - maxX, f3 + maxY - minY).overlay(overlay).lightmap(combinedLight).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.maxY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + maxY - minY).overlay(overlay).lightmap(combinedLight).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX,  (float)boundingBox.minY,  (float)boundingBox.maxZ).color(255, 255, 255, 255).tex(0 + maxX - minX, f3 + minY - maxY).overlay(overlay).lightmap(combinedLight).normal(1.0F, 0.0F, 0.0F).endVertex();
        matrixStack.pop();

    }
}
