package com.github.alexthe666.astro.client.render.entity;

import com.github.alexthe666.astro.client.render.entity.layer.LayerSpaceSquidDirt;
import com.github.alexthe666.astro.client.render.entity.layer.LayerSpaceSquidGlow;
import com.github.alexthe666.astro.client.render.entity.layer.LayerSpaceSquidRider;
import com.github.alexthe666.astro.client.render.entity.layer.LayerStarchovyGlow;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;

public class RenderStarchovy extends MobRenderer<EntityStarchovy, SegmentedModel<EntityStarchovy>> {

    private static final ResourceLocation TEXUTURE = new ResourceLocation("astro:textures/entity/starchovy.png");
    
    public RenderStarchovy(EntityRendererManager renderManagerIn, SegmentedModel<EntityStarchovy> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
        this.addLayer(new LayerStarchovyGlow(this));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityStarchovy entity) {
        return TEXUTURE;
    }
}
