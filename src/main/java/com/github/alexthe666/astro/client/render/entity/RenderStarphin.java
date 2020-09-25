package com.github.alexthe666.astro.client.render.entity;

import com.github.alexthe666.astro.client.render.entity.layer.LayerGenericGlow;
import com.github.alexthe666.astro.client.render.entity.layer.LayerStaronGlow;
import com.github.alexthe666.astro.server.entity.EntityStaron;
import com.github.alexthe666.astro.server.entity.EntityStarphin;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;

public class RenderStarphin extends MobRenderer<EntityStarphin, SegmentedModel<EntityStarphin>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("astro:textures/entity/starphin.png");
    private static final ResourceLocation GLOW = new ResourceLocation("astro:textures/entity/starphin_glow.png");

    public RenderStarphin(EntityRendererManager renderManagerIn, SegmentedModel<EntityStarphin> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
        this.addLayer(new LayerGenericGlow(this, GLOW));
    }

    @Override
    protected void preRenderCallback(EntityStarphin chovy, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.translate(0, 0.25F, 0);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityStarphin entity) {
        return TEXTURE;
    }
}
