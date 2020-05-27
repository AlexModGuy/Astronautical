package com.github.alexthe666.astro.server.world.gen;

import net.minecraft.entity.EntityClassification;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.CatSpawner;
import net.minecraft.world.spawner.PatrolSpawner;
import net.minecraft.world.spawner.PhantomSpawner;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.List;

public class ChunkProviderCosmicSea extends NoiseChunkGenerator<CosmicChunkSettings> {

    public ChunkProviderCosmicSea(IWorld worldIn, BiomeProvider provider, CosmicChunkSettings settingsIn) {
        super(worldIn, provider, 4, 8, 256, settingsIn, true);
        this.randomSeed.skip(2620);
    }

    public void spawnMobs(WorldGenRegion region) {
        int i = region.getMainChunkX();
        int j = region.getMainChunkZ();
        Biome biome = region.getBiome((new ChunkPos(i, j)).asBlockPos());
        SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
        sharedseedrandom.setDecorationSeed(region.getSeed(), i << 4, j << 4);
        WorldEntitySpawner.performWorldGenSpawning(region, biome, i, j, sharedseedrandom);
    }

    protected void fillNoiseColumn(double[] noiseColumn, int noiseX, int noiseZ) {
        double d0 = 684.4119873046875D;
        double d1 = 684.4119873046875D;
        double d2 = 8.555149841308594D;
        double d3 = 4.277574920654297D;
        int i = 0;
        int j = 0;
        this.calcNoiseColumn(noiseColumn, noiseX, noiseZ, 684.4119873046875D, 684.4119873046875D, 8.555149841308594D, 4.277574920654297D, 3, -10);
    }

    protected double func_222545_a(double p_222545_1_, double p_222545_3_, int p_222545_5_) {
        double d0 = 8.5D;
        double d1 = ((double)p_222545_5_ - (8.5D + p_222545_1_ * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / p_222545_3_;
        if (d1 < 0.0D) {
            d1 *= 4.0D;
        }

        return d1;
    }

    protected double[] getBiomeNoiseColumn(int noiseX, int noiseZ) {
        double[] adouble = new double[2];
        return adouble;
    }

    private double getNoiseDepthAt(int noiseX, int noiseZ) {
        return 0;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos) {
        return super.getPossibleCreatures(creatureType, pos);
    }

    public void spawnMobs(ServerWorld worldIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs) {

    }

    public int getGroundHeight() {
        return this.world.getSeaLevel() + 1;
    }

    public int getSeaLevel() {
        return 0;
    }
}
