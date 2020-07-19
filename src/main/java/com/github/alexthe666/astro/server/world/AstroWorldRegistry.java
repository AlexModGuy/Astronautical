package com.github.alexthe666.astro.server.world;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.world.feature.AstroFeatureRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroWorldRegistry {

    public static Biome COSMIC_SEA;
    public static final CosmicSurfaceBuilder COSMIC_SURFACE = new CosmicSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_);


    public static void setup() {

    }

    @SubscribeEvent
    public static void registerSurfaces(final RegistryEvent.Register<SurfaceBuilder<?>> event) {
        event.getRegistry().register(COSMIC_SURFACE);
    }

    @SubscribeEvent
    public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
        AstroFeatureRegistry.register();
        event.getRegistry().register(COSMIC_SEA = new BiomeCosmicSea());
        BiomeDictionary.addTypes(COSMIC_SEA, BiomeDictionary.Type.VOID);
    }
}
