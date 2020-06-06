package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.render.AstroRenderTypes;
import com.github.alexthe666.astro.client.render.entity.RenderStarchovy;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class LayerStarchovyGlow extends LayerRenderer<EntityStarchovy, SegmentedModel<EntityStarchovy>> {
    private static final RenderType TEXTURE = AstroRenderTypes.getTransparentGlowy(new ResourceLocation("astro:textures/entity/starchovy_glow.png"));
    private final IEntityRenderer<EntityStarchovy, SegmentedModel<EntityStarchovy>> render;

    public LayerStarchovyGlow(IEntityRenderer<EntityStarchovy, SegmentedModel<EntityStarchovy>> render) {
        super(render);
        this.render = render;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityStarchovy squid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, LivingRenderer.getPackedOverlay(squid, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);

    }
}