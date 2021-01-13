package com.github.alexthe666.astro.client.render;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;

public class AstroRenderTypes extends RenderType {

    protected static final RenderState.TransparencyState GLOWY_TRANSPARENCY = new RenderState.TransparencyState("translucent_transparency", () -> {
        RenderSystem.pushMatrix();
        GlStateManager.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        RenderSystem.disableLighting();
        GL11.glDisable(GL11.GL_NORMALIZE);
        RenderSystem.glMultiTexCoord2f(GL_TEXTURE1, 61680, 0.0F);
    }, () -> {
        GL11.glEnable(GL11.GL_NORMALIZE);
        RenderSystem.enableLighting();
        GlStateManager.disableBlend();
        RenderSystem.popMatrix();
    });

    public static final RenderType STAR_ITEM = makeType("lightning", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, RenderType.State.getBuilder().writeMask(COLOR_WRITE).transparency(TRANSLUCENT_TRANSPARENCY).shadeModel(SHADE_ENABLED).build(false));


    public static RenderType getTransparentGlowy(ResourceLocation p_228652_0_) {
        TextureState lvt_1_1_ = new TextureState(p_228652_0_, false, false);
        return makeType("transparent_glowy", DefaultVertexFormats.ENTITY, 7, 256, false, false, RenderType.State.getBuilder().texture(lvt_1_1_).transparency(TRANSLUCENT_TRANSPARENCY).overlay(OVERLAY_ENABLED).writeMask(COLOR_WRITE).fog(BLACK_FOG).cull(CULL_DISABLED).build(false));
    }

    public static RenderType getTransparentGlowy2(ResourceLocation p_228652_0_) {
        TextureState lvt_1_1_ = new TextureState(p_228652_0_, false, false);
        return makeType("transparent_glowy", DefaultVertexFormats.ENTITY, 7, 256, false, false, RenderType.State.getBuilder().texture(lvt_1_1_).transparency(TRANSLUCENT_TRANSPARENCY).overlay(OVERLAY_DISABLED).writeMask(COLOR_DEPTH_WRITE).fog(BLACK_FOG).cull(CULL_DISABLED).build(false));
    }


    public static RenderType getSquidTankOoze(ResourceLocation locationIn) {
        TextureState lvt_1_1_ = new TextureState(locationIn, false, false);
        return makeType("ghost_iaf_day", DefaultVertexFormats.ENTITY, 7, 262144, false, true, RenderType.State.getBuilder().texture(lvt_1_1_).writeMask(COLOR_DEPTH_WRITE).depthTest(DEPTH_LEQUAL).alpha(DEFAULT_ALPHA).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).lightmap(LIGHTMAP_DISABLED).overlay(OVERLAY_ENABLED).transparency(TRANSLUCENT_TRANSPARENCY).fog(FOG).cull(RenderState.CULL_ENABLED).build(true));
    }

    public AstroRenderTypes(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
        super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
    }
}
