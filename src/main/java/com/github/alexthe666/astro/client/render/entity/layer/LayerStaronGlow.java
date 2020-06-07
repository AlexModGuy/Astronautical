package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.render.AstroRenderTypes;
import com.github.alexthe666.astro.client.render.entity.RenderStaron;
import com.github.alexthe666.astro.server.entity.EntityStaron;
import com.github.alexthe666.astro.server.entity.EntityStaron;
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

public class LayerStaronGlow extends LayerRenderer<EntityStaron, SegmentedModel<EntityStaron>> {
    private static final RenderType TEXTURE = AstroRenderTypes.getTransparentGlowy(new ResourceLocation("astro:textures/entity/staron_eyes.png"));
    private static final RenderType TEXTURE_TAME = AstroRenderTypes.getTransparentGlowy(new ResourceLocation("astro:textures/entity/staron_eyes_tame.png"));
    private final IEntityRenderer<EntityStaron, SegmentedModel<EntityStaron>> render;

    public LayerStaronGlow(IEntityRenderer<EntityStaron, SegmentedModel<EntityStaron>> render) {
        super(render);
        this.render = render;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityStaron staron, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IVertexBuilder ivertexbuilder = staron.isNonHostile() ? bufferIn.getBuffer(TEXTURE_TAME) : bufferIn.getBuffer(TEXTURE);
        this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, LivingRenderer.getPackedOverlay(staron, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);

    }
}