package com.github.alexthe666.astro.server.world;

import com.github.alexthe666.astro.server.entity.AstroEntityRegistry;
import com.github.alexthe666.astro.server.world.feature.AstroFeatureRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BiomeCosmicSea extends Biome {

    public static final SurfaceBuilderConfig SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.SAND.getDefaultState());

    protected BiomeCosmicSea() {
        super((new Biome.Builder()).surfaceBuilder(AstroWorldRegistry.COSMIC_SURFACE, SURFACE_BUILDER_CONFIG).precipitation(RainType.NONE).category(Category.NONE).scale(0.1F).temperature(0.55F).downfall(0.5F).depth(1).func_235097_a_((new BiomeAmbience.Builder()).func_235246_b_(4445678).func_235248_c_(270131).func_235239_a_(12638463).func_235243_a_(MoodSoundAmbience.field_235027_b_).func_235238_a_()).parent((String)null));
        this.setRegistryName("astro:open_cosmic_ocean");
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AstroFeatureRegistry.PLANETOID.withConfiguration(new NoFeatureConfig()));
        this.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, AstroFeatureRegistry.ASTEROID.withConfiguration(new NoFeatureConfig()));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(AstroEntityRegistry.SPACE_SQUID, 10, 1, 1));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(AstroEntityRegistry.STARCHOVY, 35, 10, 20));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(AstroEntityRegistry.SCUTTLEFISH, 5, 1, 1));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(AstroEntityRegistry.STARON, 15, 1, 2));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(AstroEntityRegistry.GLOPEPOD, 45, 3, 5));
    }
}
