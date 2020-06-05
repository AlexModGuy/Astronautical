package com.github.alexthe666.astro.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class AstroRenderTypes extends RenderType {

    public static final RenderType STAR_ITEM = makeType("lightning", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, RenderType.State.getBuilder().writeMask(COLOR_WRITE).transparency(TRANSLUCENT_TRANSPARENCY).shadeModel(SHADE_ENABLED).build(false));


    public static RenderType getTransparentGlowy(ResourceLocation p_228652_0_) {
        TextureState lvt_1_1_ = new TextureState(p_228652_0_, false, false);
        return makeType("transparent_glowy", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder().texture(lvt_1_1_).transparency(ADDITIVE_TRANSPARENCY).alpha(HALF_ALPHA).writeMask(COLOR_WRITE).fog(BLACK_FOG).build(false));
    }

    public AstroRenderTypes(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
        super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
    }
}
