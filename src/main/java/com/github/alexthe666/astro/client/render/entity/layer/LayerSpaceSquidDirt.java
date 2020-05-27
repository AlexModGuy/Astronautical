package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class LayerSpaceSquidDirt extends LayerRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> {
    private static final RenderType DIRT_TEXTURE = RenderType.getEntityNoOutline(new ResourceLocation("astro:textures/entity/space_squid/dirty.png"));
    private static final RenderType SADDLE_TEXTURE = RenderType.getEntityNoOutline(new ResourceLocation("astro:textures/entity/space_squid/saddle.png"));
    private final IEntityRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> render;

    public LayerSpaceSquidDirt(IEntityRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> render) {
        super(render);
        this.render = render;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntitySpaceSquid squid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (squid.isDirty()) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(DIRT_TEXTURE);
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (squid.isSaddled()) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(SADDLE_TEXTURE);
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}