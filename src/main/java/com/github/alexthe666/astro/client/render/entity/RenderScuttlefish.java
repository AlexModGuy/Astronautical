package com.github.alexthe666.astro.client.render.entity;

import com.github.alexthe666.astro.client.render.entity.layer.LayerScutttlefishGlow;
import com.github.alexthe666.astro.client.render.entity.layer.LayerScutttlefishHeldItem;
import com.github.alexthe666.astro.client.render.entity.layer.LayerStaronGlow;
import com.github.alexthe666.astro.server.entity.EntityScuttlefish;
import com.github.alexthe666.astro.server.entity.EntityScuttlefish;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;

public class RenderScuttlefish extends MobRenderer<EntityScuttlefish, SegmentedModel<EntityScuttlefish>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("astro:textures/entity/scuttlefish.png");

    public RenderScuttlefish(EntityRendererManager renderManagerIn, SegmentedModel<EntityScuttlefish> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
        this.addLayer(new LayerScutttlefishGlow(this));
        this.addLayer(new LayerScutttlefishHeldItem(this));
    }

    @Override
    protected void preRenderCallback(EntityScuttlefish chovy, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.translate(0, 0, -0.4F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityScuttlefish entity) {
        return TEXTURE;
    }
}
