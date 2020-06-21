package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.render.AstroRenderTypes;
import com.github.alexthe666.astro.server.entity.EntityScuttlefish;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;

public class LayerScutttlefishGlow extends LayerRenderer<EntityScuttlefish, SegmentedModel<EntityScuttlefish>> {
    private static final RenderType TEXTURE = AstroRenderTypes.getTransparentGlowy(new ResourceLocation("astro:textures/entity/scuttlefish_glow.png"));
    private final IEntityRenderer<EntityScuttlefish, SegmentedModel<EntityScuttlefish>> render;

    public LayerScutttlefishGlow(IEntityRenderer<EntityScuttlefish, SegmentedModel<EntityScuttlefish>> render) {
        super(render);
        this.render = render;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityScuttlefish squid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        float r = 1.0F;
        float g = 0.5F - (float) Math.cos(ageInTicks * 0.35) * 0.5F;
        float b = 1.0F;
        if (!squid.getHeldItemMainhand().isEmpty()) {
            if (squid.hasRecipe()) {
                r = 0.5F - (float) Math.cos(ageInTicks * 0.05) * 0.5F;
                g = 1.0F;
                b = 0.5F - (float) Math.cos(ageInTicks * 0.05) * 0.5F;
            } else {
                r = 1.0F;
                g = 0.5F - (float) Math.cos(ageInTicks * 0.75) * 0.5F;
                b = 0.5F - (float) Math.cos(ageInTicks * 0.75) * 0.5F;
            }
        }
        this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, LivingRenderer.getPackedOverlay(squid, 0.0F), r, g, b, 1.0F);

    }
}