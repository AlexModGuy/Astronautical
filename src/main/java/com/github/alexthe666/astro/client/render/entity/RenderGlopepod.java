package com.github.alexthe666.astro.client.render.entity;

import com.github.alexthe666.astro.client.render.entity.layer.LayerGlopepodGlow;
import com.github.alexthe666.astro.server.entity.EntityGlopepod;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;

public class RenderGlopepod extends MobRenderer<EntityGlopepod, SegmentedModel<EntityGlopepod>> {

    private static final ResourceLocation TEXUTURE = new ResourceLocation("astro:textures/entity/glopepod.png");
    
    public RenderGlopepod(EntityRendererManager renderManagerIn, SegmentedModel<EntityGlopepod> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
        this.addLayer(new LayerGlopepodGlow(this));
    }

    @Override
    protected void preRenderCallback(EntityGlopepod chovy, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.45F, 0.45F, 0.45F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGlopepod entity) {
        return TEXUTURE;
    }
}
