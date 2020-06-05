package com.github.alexthe666.astro.server.world;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.world.gen.ChunkProviderCosmicSea;
import com.github.alexthe666.astro.server.world.gen.CosmicChunkSettings;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class CosmicSeaDimension extends Dimension {

    public CosmicSeaDimension(final World worldIn, final DimensionType dimension) {
        super(worldIn, dimension, 0);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        CosmicChunkSettings settings = AstroWorldRegistry.COSMIC_CHUNK_GENERATOR.createSettings();
        settings.setDefaultBlock(Blocks.VOID_AIR.getDefaultState());
        SingleBiomeProviderSettings providerSettings = new SingleBiomeProviderSettings(null);
        providerSettings.setBiome(AstroWorldRegistry.COSMIC_SEA);
        return new ChunkProviderCosmicSea(this.world, new SingleBiomeProvider(providerSettings), settings);
    }


    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.45F;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return null;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public Vec3d getFogColor(final float p_76562_1_, final float p_76562_2_) {
        return new Vec3d(0, 0, 0);
    }


    @Override
    public boolean hasSkyLight() {
        return true;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public boolean isSkyColored() {
        return false;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public float getCloudHeight() {
        return 0;
    }

    @Override @Nullable
    public BlockPos findSpawn(final ChunkPos p_206920_1_, final boolean p_206920_2_) {
        final Random random = new Random(this.world.getSeed());
        final BlockPos blockpos = new BlockPos(p_206920_1_.getXStart() + random.nextInt(15), 0,
                p_206920_1_.getZEnd() + random.nextInt(15));
        return this.world.getGroundAboveSeaLevel(blockpos).getMaterial().blocksMovement() ? blockpos : null;
    }

    @Override @Nullable
    public BlockPos findSpawn(final int p_206921_1_, final int p_206921_2_, final boolean p_206921_3_) {
        return this.findSpawn(new ChunkPos(p_206921_1_ >> 4, p_206921_2_ >> 4), p_206921_3_);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public boolean doesXZShowFog(final int p_76568_1_, final int p_76568_2_) {
        return false;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    @Override
    public net.minecraftforge.client.IRenderHandler getSkyRenderer() {
        return (net.minecraftforge.client.IRenderHandler)Astronautical.PROXY.getSkyRendererForDim();
    }
}