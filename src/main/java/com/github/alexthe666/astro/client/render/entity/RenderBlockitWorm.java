package com.github.alexthe666.astro.client.render.entity;

import com.github.alexthe666.astro.client.model.ModelBlockitWorm;
import com.github.alexthe666.astro.server.entity.EntityBlockitWorm;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;

public class RenderBlockitWorm extends LivingRenderer<EntityBlockitWorm, ModelBlockitWorm> {
    private static final ResourceLocation WORM_TEXTURE = new ResourceLocation("astro:textures/entity/blockit_worm.png");

    public RenderBlockitWorm(EntityRendererManager manager) {
        super(manager, new ModelBlockitWorm(), 0);
    }

    protected float getDeathMaxRotation(EntityBlockitWorm entityLivingBaseIn) {
        return 0F;
    }

    protected boolean canRenderName(EntityBlockitWorm entity) {
        return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
    }

    public void render(EntityBlockitWorm entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f = entity.ticksExisted + partialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        matrixStackIn.push();
        BlockState state = entity.getMeteoriteState();
        if (state != null) {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
            matrixStackIn.push();
            float f4 = 0.75F;
            matrixStackIn.translate(-0.5D, 0, -0.5D);
            blockrendererdispatcher.renderBlock(state, matrixStackIn, bufferIn, packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F));
            matrixStackIn.pop();
        }
        matrixStackIn.pop();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public Vector3d getRenderOffset(EntityBlockitWorm entityIn, float partialTicks) {
        int i = entityIn.getClientTeleportInterp();
        if (i > 0 && entityIn.isAttachedToBlock()) {
            BlockPos blockpos = entityIn.getAttachmentPos();
            BlockPos blockpos1 = entityIn.getOldAttachPos();
            double d0 = (double) ((float) i - partialTicks) / 6.0D;
            d0 = d0 * d0;
            double d1 = (double) (blockpos.getX() - blockpos1.getX()) * d0;
            double d2 = (double) (blockpos.getY() - blockpos1.getY()) * d0;
            double d3 = (double) (blockpos.getZ() - blockpos1.getZ()) * d0;
            return new Vector3d(-d1, -d2, -d3);
        } else {
            return super.getRenderOffset(entityIn, partialTicks);
        }
    }

    public boolean shouldRender(EntityBlockitWorm livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            if (livingEntityIn.getClientTeleportInterp() > 0 && livingEntityIn.isAttachedToBlock()) {
                Vector3d Vector3d = new Vector3d(livingEntityIn.getAttachmentPos().getX(), livingEntityIn.getAttachmentPos().getY(), livingEntityIn.getAttachmentPos().getZ());
                Vector3d Vector3d1 = new Vector3d(livingEntityIn.getOldAttachPos().getX(), livingEntityIn.getOldAttachPos().getY(), livingEntityIn.getOldAttachPos().getZ());
                return camera.isBoundingBoxInFrustum(new AxisAlignedBB(Vector3d1.x, Vector3d1.y, Vector3d1.z, Vector3d.x, Vector3d.y, Vector3d.z));
            }

            return false;
        }
    }

    protected void applyRotations(EntityBlockitWorm entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);
        matrixStackIn.rotate(entityLiving.getAttachmentFacing().getOpposite().getRotation());
        matrixStackIn.translate(0.0D, -0.5D, 0.0D);
    }

    protected void preRenderCallback(EntityBlockitWorm entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.999F;
        matrixStackIn.scale(0.999F, 0.999F, 0.999F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBlockitWorm entity) {
        return WORM_TEXTURE;
    }
}
