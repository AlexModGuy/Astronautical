package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.render.entity.RenderSpaceSquid;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class LayerSpaceSquidGlow extends LayerRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> {
    public static final RenderType TEAL = RenderType.getEyes(new ResourceLocation("astro:textures/entity/space_squid/teal_glow.png"));
    public static final RenderType PURPLE = RenderType.getEyes(new ResourceLocation("astro:textures/entity/space_squid/purple_glow.png"));
    public static final RenderType BLUE = RenderType.getEyes(new ResourceLocation("astro:textures/entity/space_squid/blue_glow.png"));
    private final IEntityRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> render;

    public LayerSpaceSquidGlow(IEntityRenderer<EntitySpaceSquid, SegmentedModel<EntitySpaceSquid>> render) {
        super(render);
        this.render = render;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntitySpaceSquid squid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType type;
       if(!squid.isFallen() || squid.isInterested()){
           switch (squid.getColorVariant()){
               case 0:
                   type = TEAL;
                   break;
               case 1:
                   type = PURPLE;
                   break;
               case 2:
                   type = BLUE;
                   break;
               default:
                   type = TEAL;
           }
           IVertexBuilder ivertexbuilder = bufferIn.getBuffer(type);
           this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
       }
    }
}