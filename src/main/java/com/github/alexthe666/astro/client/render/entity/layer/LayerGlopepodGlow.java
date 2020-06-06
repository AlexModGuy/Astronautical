package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.render.AstroRenderTypes;
import com.github.alexthe666.astro.client.render.entity.RenderGlopepod;
import com.github.alexthe666.astro.server.entity.EntityGlopepod;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL13.GL_TEXTURE1;

public class LayerGlopepodGlow extends LayerRenderer<EntityGlopepod, SegmentedModel<EntityGlopepod>> {

    public LayerGlopepodGlow(RenderGlopepod renderGlopepod) {
        super(renderGlopepod);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityGlopepod squid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(AstroRenderTypes.getTransparentGlowy2(new ResourceLocation("astro:textures/entity/glopepod_glow.png")));
        this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, LivingRenderer.getPackedOverlay(squid, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
