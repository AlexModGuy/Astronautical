package com.github.alexthe666.astro.server.world.feature;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class AstroFeatureRegistry {

    public static Feature<NoFeatureConfig> PLANETOID;
    public static Feature<NoFeatureConfig> ASTEROID;

    public static void register() {
        PLANETOID = Registry.register(Registry.FEATURE, "astro:planetoid", new FeaturePlanetoid(NoFeatureConfig.field_236558_a_));
        ASTEROID = Registry.register(Registry.FEATURE, "astro:asteroid", new FeatureAsteroid(NoFeatureConfig.field_236558_a_));

    }
}
