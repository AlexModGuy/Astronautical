package com.github.alexthe666.astro.client.render.entity;

import com.github.alexthe666.astro.client.render.entity.layer.LayerGenericGlow;
import com.github.alexthe666.astro.client.render.entity.layer.LayerScutttlefishHeldItem;
import com.github.alexthe666.astro.client.render.entity.layer.LayerStaronGlow;
import com.github.alexthe666.astro.client.render.entity.layer.LayerStarphinHeldItem;
import com.github.alexthe666.astro.server.entity.EntityStaron;
import com.github.alexthe666.astro.server.entity.EntityStarphin;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RenderStarphin extends MobRenderer<EntityStarphin, SegmentedModel<EntityStarphin>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("astro:textures/entity/starphin.png");
    private static final ResourceLocation GLOW = new ResourceLocation("astro:textures/entity/starphin_glow.png");

    public RenderStarphin(EntityRendererManager renderManagerIn, SegmentedModel<EntityStarphin> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
        this.addLayer(new LayerGenericGlow(this, GLOW));
        this.addLayer(new LayerStarphinHeldItem(this));
    }

    @Override
    protected void preRenderCallback(EntityStarphin entity, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
        float rotY = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), entity.prevFishPitch, entity.getFishPitch());
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(rotY));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityStarphin entity) {
        return TEXTURE;
    }
}
