package com.github.alexthe666.astro.client.render.item;

import com.github.alexthe666.astro.client.render.AstroRenderTypes;
import com.github.alexthe666.astro.server.item.AstroItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class FallingStarItemRenderer {

    private static final float field_229057_l_ = (float)(Math.sqrt(3.0D) / 2.0D);

    public static void renderFallingStar(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        combinedLightIn = 240;
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.push();
        matrixStackIn.push();
        Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(AstroItemRegistry.FALLING_STAR_RENDER), ItemCameraTransforms.TransformType.NONE, 240, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
        matrixStackIn.pop();

        matrixStackIn.push();
        float lvt_11_2_ = 3;
        float lvt_12_2_ = 0.2F;

        Random lvt_13_1_ = new Random(432L);
        float ticksExisted = Minecraft.getInstance().player.ticksExisted + Minecraft.getInstance().getRenderPartialTicks();
        IVertexBuilder lvt_14_1_ = bufferIn.getBuffer(AstroRenderTypes.STAR_ITEM);
        matrixStackIn.push();
        matrixStackIn.scale(0.05F, 0.05F, 0.05F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YN, ticksExisted, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.XN, ticksExisted * 1.25F - 50, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.ZN, ticksExisted * 0.85F + 40, true));

        for (int lvt_15_1_ = 0; (float) lvt_15_1_ < (lvt_11_2_ + lvt_11_2_ * lvt_11_2_) / 2.0F * 10.0F; ++lvt_15_1_) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F + lvt_11_2_ * 90.0F));
            float lvt_16_1_ = lvt_13_1_.nextFloat() * 10.0F + 5.0F + lvt_12_2_ * 2.0F;
            float lvt_17_1_ = lvt_13_1_.nextFloat() * 2.0F + 1.0F + lvt_12_2_ * 2.0F;
            Matrix4f lvt_18_1_ = matrixStackIn.getLast().getMatrix();
            int lvt_19_1_ = (int) (255.0F * (1.0F - lvt_12_2_));
            beginFlareWhite(lvt_14_1_, lvt_18_1_, lvt_19_1_);
            drawFlare1Yellow(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            drawFlare2Yellow(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            beginFlareWhite(lvt_14_1_, lvt_18_1_, lvt_19_1_);
            drawFlare2Yellow(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            drawFlare3Yellow(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            beginFlareWhite(lvt_14_1_, lvt_18_1_, lvt_19_1_);
            drawFlare3Yellow(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            drawFlare1Yellow(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
        }

        matrixStackIn.pop();
        matrixStackIn.pop();

        matrixStackIn.pop();
    }

    public static void renderCosmosStar(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        combinedLightIn = 240;
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.push();
        matrixStackIn.push();
        Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(AstroItemRegistry.COSMOS_STAR_RENDER), ItemCameraTransforms.TransformType.NONE, 240, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
        matrixStackIn.pop();

        matrixStackIn.push();
        float lvt_11_2_ = 2;
        float lvt_12_2_ = 0.2F;

        Random lvt_13_1_ = new Random(432L);
        float ticksExisted = Minecraft.getInstance().player.ticksExisted + Minecraft.getInstance().getRenderPartialTicks();
        IVertexBuilder lvt_14_1_ = bufferIn.getBuffer(AstroRenderTypes.STAR_ITEM);
        matrixStackIn.push();
        matrixStackIn.scale(0.05F, 0.05F, 0.05F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YN, ticksExisted * 1.25F, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.XN, ticksExisted * 1.0F - 50, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.ZN, ticksExisted * 0.45F + 40, true));

        for (int lvt_15_1_ = 0; (float) lvt_15_1_ < (lvt_11_2_ + lvt_11_2_ * lvt_11_2_) / 2.0F * 10.0F; ++lvt_15_1_) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(lvt_13_1_.nextFloat() * 360.0F + lvt_11_2_ * 90.0F));
            float lvt_16_1_ = lvt_13_1_.nextFloat() * 15.0F + 5.0F + lvt_12_2_ * 2.0F;
            float lvt_17_1_ = lvt_13_1_.nextFloat() * 5.0F + 1.0F + lvt_12_2_ * 2.0F;
            Matrix4f lvt_18_1_ = matrixStackIn.getLast().getMatrix();
            int lvt_19_1_ = (int) (255.0F * (1.0F - lvt_12_2_));
            beginFlarePurple(lvt_14_1_, lvt_18_1_, lvt_19_1_);
            drawFlare1Blue(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            drawFlare2Blue(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            beginFlarePurple(lvt_14_1_, lvt_18_1_, lvt_19_1_);
            drawFlare2Blue(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            drawFlare3Blue(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            beginFlarePurple(lvt_14_1_, lvt_18_1_, lvt_19_1_);
            drawFlare3Blue(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
            drawFlare1Blue(lvt_14_1_, lvt_18_1_, lvt_16_1_, lvt_17_1_);
        }

        matrixStackIn.pop();
        matrixStackIn.pop();

        matrixStackIn.pop();
    }

    private static void beginFlareWhite(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
    }

    private static void drawFlare1Yellow(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_) {
        p_229060_0_.pos(p_229060_1_, -field_229057_l_ * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(255, 169, 33, 0).endVertex();
    }

    private static void drawFlare2Yellow(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_) {
        p_229062_0_.pos(p_229062_1_, field_229057_l_ * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(255, 169, 33, 0).endVertex();
    }

    private static void drawFlare3Yellow(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_) {
        p_229063_0_.pos(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(255, 169, 33, 0).endVertex();
    }

    private static void beginFlarePurple(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(128, 117, 229, p_229061_2_).endVertex();
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(128, 117, 229, p_229061_2_).endVertex();
    }

    private static void drawFlare1Blue(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_) {
        p_229060_0_.pos(p_229060_1_, -field_229057_l_ * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(0, 0, 255, 0).endVertex();
    }

    private static void drawFlare2Blue(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_) {
        p_229062_0_.pos(p_229062_1_, field_229057_l_ * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(0, 0, 255, 0).endVertex();
    }

    private static void drawFlare3Blue(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_) {
        p_229063_0_.pos(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(0, 0, 255, 0).endVertex();
    }
}
