package com.github.alexthe666.astro.server.world;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BiomeCosmicSea extends Biome {

    public static final SurfaceBuilderConfig SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig(Blocks.VOID_AIR.getDefaultState(), Blocks.VOID_AIR.getDefaultState(), Blocks.VOID_AIR.getDefaultState());

    protected BiomeCosmicSea() {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG).precipitation(RainType.NONE).category(Category.OCEAN).scale(0.1F).temperature(0.55F).downfall(0.5F).waterColor(4445678).depth(1).waterFogColor(270131).parent((String)null));
        this.setRegistryName("astro:open_cosmic_ocean");
    }
}
