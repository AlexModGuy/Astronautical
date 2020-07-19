package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.ClientProxy;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class LayerSpaceSquidRider extends LayerRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> {
    private final IEntityRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> render;

    public LayerSpaceSquidRider(IEntityRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> render) {
        super(render);
        this.render = render;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntitySpaceSquid squid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(squid.isBeingRidden()){
            for(Entity passenger : squid.getPassengers()){
                float riderRot = passenger.prevRotationYaw + (passenger.rotationYaw - passenger.prevRotationYaw) * partialTicks;
                EntityRenderer render = Minecraft.getInstance().getRenderManager().getRenderer(passenger);
                EntityModel modelBase = null;
                if (render instanceof LivingRenderer) {
                    modelBase = ((LivingRenderer) render).getEntityModel();
                }
                if(modelBase != null){
                    ClientProxy.currentSquidRiders.remove(passenger.getUniqueID());
                    matrixStackIn.push();
                    translateToBody(matrixStackIn);
                    matrixStackIn.translate(0, 0.05F, 0.4F);
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(riderRot + 180F));
                    renderEntity(passenger, 0, 0, 0, squid.renderYawOffset, partialTicks, matrixStackIn, bufferIn, packedLightIn);
                    matrixStackIn.pop();
                    ClientProxy.currentSquidRiders.add(passenger.getUniqueID());
                }

            }
        }
    }

    public <E extends Entity> void renderEntity(E entityIn, double x, double y, double z, float yaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
        try {
            render = manager.getRenderer(entityIn);

            if (render != null) {
                try {
                    render.render(entityIn, yaw, partialTicks, matrixStack, bufferIn, packedLight);
                } catch (Throwable throwable1) {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Rendering entity in world"));
                }
            }
        } catch (Throwable throwable3) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            entityIn.fillCrashReport(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addDetail("Assigned renderer", render);
            crashreportcategory1.addDetail("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
            crashreportcategory1.addDetail("Rotation", Float.valueOf(yaw));
            crashreportcategory1.addDetail("Delta", Float.valueOf(partialTicks));
            throw new ReportedException(crashreport);
        }
    }

    protected void translateToBody(MatrixStack matrixStackIn) {
        AdvancedModelBox body1 = ((TabulaModel) this.render.getEntityModel()).getCube("body1");
        matrixStackIn.translate((double)(body1.rotationPointX / 16.0F), (double)(body1.rotationPointY / 16.0F), (double)(body1.rotationPointZ / 16.0F));
        if (body1.rotateAngleZ != 0.0F) {
            matrixStackIn.rotate(Vector3f.ZP.rotation(body1.rotateAngleZ));
        }

        if (body1.rotateAngleY != 0.0F) {
            matrixStackIn.rotate(Vector3f.YP.rotation(body1.rotateAngleY));
        }

        if (body1.rotateAngleX != 0.0F) {
            matrixStackIn.rotate(Vector3f.XP.rotation(body1.rotateAngleX));
        }

    }

}