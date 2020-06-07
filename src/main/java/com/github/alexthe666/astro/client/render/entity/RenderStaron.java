package com.github.alexthe666.astro.client.render.entity;

import com.github.alexthe666.astro.client.render.entity.layer.LayerStarchovyGlow;
import com.github.alexthe666.astro.client.render.entity.layer.LayerStaronGlow;
import com.github.alexthe666.astro.server.entity.EntityStaron;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;

public class RenderStaron extends MobRenderer<EntityStaron, SegmentedModel<EntityStaron>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("astro:textures/entity/staron.png");
    private static final ResourceLocation TEXTURE_ARMORED = new ResourceLocation("astro:textures/entity/staron_armor.png");

    public RenderStaron(EntityRendererManager renderManagerIn, SegmentedModel<EntityStaron> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
        this.addLayer(new LayerStaronGlow(this));
    }

    @Override
    protected void preRenderCallback(EntityStaron chovy, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.translate(0, 0.25F, 0);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityStaron entity) {
        return entity.isArmored() ? TEXTURE_ARMORED : TEXTURE;
    }
}
