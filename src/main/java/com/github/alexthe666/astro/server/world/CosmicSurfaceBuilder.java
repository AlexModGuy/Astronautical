package com.github.alexthe666.astro.server.world;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;
import java.util.function.Function;

public class CosmicSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

    public CosmicSurfaceBuilder(Codec<SurfaceBuilderConfig> p_i232127_1_) {
        super(p_i232127_1_);
        this.setRegistryName("astro:cosmic_surface_builder");

    }

    public void setSeed(long seed) {
        super.setSeed(seed);
    }

    @Override
    public void buildSurface(Random rand, IChunk chunkIn, Biome biomeIn, int chunkX, int chunkZ, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
    }
}