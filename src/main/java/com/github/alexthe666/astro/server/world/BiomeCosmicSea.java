package com.github.alexthe666.astro.server.world;

import com.github.alexthe666.astro.server.entity.AstroEntityRegistry;
import com.github.alexthe666.astro.server.world.feature.AstroFeatureRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BiomeCosmicSea extends Biome {

    protected BiomeCosmicSea() {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG).precipitation(RainType.NONE).category(Category.OCEAN).scale(0.1F).temperature(0.55F).downfall(0.5F).waterColor(4445678).depth(1).waterFogColor(270131).parent((String)null));
        this.setRegistryName("astro:open_cosmic_ocean");
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AstroFeatureRegistry.PLANETOID.withConfiguration(new NoFeatureConfig()));
        this.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, AstroFeatureRegistry.ASTEROID.withConfiguration(new NoFeatureConfig()));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(AstroEntityRegistry.SPACE_SQUID, 20, 1, 2));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(AstroEntityRegistry.STARCHOVY, 35, 1, 20));
        this.addSpawn(EntityClassification.AMBIENT, new Biome.SpawnListEntry(AstroEntityRegistry.GLOPEPOD, 55, 1, 5));
    }
}
