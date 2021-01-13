package com.github.alexthe666.astro.client.render.entity;

import com.github.alexthe666.astro.client.render.entity.layer.LayerSpaceSquidDirt;
import com.github.alexthe666.astro.client.render.entity.layer.LayerSpaceSquidGlow;
import com.github.alexthe666.astro.client.render.entity.layer.LayerSpaceSquidRider;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSpaceSquid extends MobRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> {

    public static final ResourceLocation TEAL = new ResourceLocation("astro:textures/entity/space_squid/teal.png");
    public static final ResourceLocation TEAL_INJURED = new ResourceLocation("astro:textures/entity/space_squid/teal_injured.png");
    public static final ResourceLocation PURPLE = new ResourceLocation("astro:textures/entity/space_squid/purple.png");
    public static final ResourceLocation PURPLE_INJURED = new ResourceLocation("astro:textures/entity/space_squid/purple_injured.png");
    public static final ResourceLocation BLUE = new ResourceLocation("astro:textures/entity/space_squid/blue.png");
    public static final ResourceLocation BLUE_INJURED = new ResourceLocation("astro:textures/entity/space_squid/blue_injured.png");

    public RenderSpaceSquid(EntityRendererManager renderManagerIn, SegmentedModel<EntitySpaceSquid> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
        this.addLayer(new LayerSpaceSquidGlow(this));
        this.addLayer(new LayerSpaceSquidDirt(this));
        this.addLayer(new LayerSpaceSquidRider(this));

    }

    protected float getDeathMaxRotation(EntitySpaceSquid entityLivingBaseIn) {
        return 0F;
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySpaceSquid entity) {
        if (entity.isFallen()) {
            switch (entity.getColorVariant()) {
                case 0:
                    return TEAL_INJURED;
                case 1:
                    return PURPLE_INJURED;
                case 2:
                    return BLUE_INJURED;
                default:
                    return TEAL_INJURED;
            }
        }else{
            switch (entity.getColorVariant()) {
                case 0:
                    return TEAL;
                case 1:
                    return PURPLE;
                case 2:
                    return BLUE;
                default:
                    return TEAL;
            }
        }
    }
}
