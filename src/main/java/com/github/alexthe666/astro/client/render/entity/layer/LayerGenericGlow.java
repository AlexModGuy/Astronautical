package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.render.AstroRenderTypes;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class LayerGenericGlow<T extends LivingEntity, M extends SegmentedModel<T>> extends LayerRenderer<T, M> {
    private static RenderType TEXTURE;
    private final IEntityRenderer<T, M> render;

    public LayerGenericGlow(IEntityRenderer<T, M> render, ResourceLocation tex) {
        super(render);
        TEXTURE =  AstroRenderTypes.getTransparentGlowy(tex);
        this.render = render;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T squid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, LivingRenderer.getPackedOverlay(squid, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);

    }
}