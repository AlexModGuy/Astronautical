package com.github.alexthe666.astro.client.render;

import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;

public class CosmicSeaRenderInfo extends DimensionRenderInfo {
    public CosmicSeaRenderInfo() {
        super(Float.NaN, true, DimensionRenderInfo.FogType.NONE, false, true);
    }

    public Vector3d func_230494_a_(Vector3d p_230494_1_, float p_230494_2_) {
        return p_230494_1_;
    }

    public boolean func_230493_a_(int p_230493_1_, int p_230493_2_) {
        return true;
    }
}