package com.github.alexthe666.astro.server.world;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.world.gen.CosmicChunkGenerator;
import com.github.alexthe666.astro.server.world.gen.CosmicChunkSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.DebugChunkGenerator;
import net.minecraft.world.gen.DebugGenerationSettings;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroWorldRegistry {

    public static final ModDimension COSMIC_SEA_MOD_DIMENSION = new CosmicSeaModDimension(CosmicSeaDimension::new).setRegistryName("astro:cosmic_sea");
    public static DimensionType COSMIC_SEA_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation("astro:cosmic_sea"), COSMIC_SEA_MOD_DIMENSION, null, true);
    public static Biome COSMIC_SEA ;
    public static final CosmicSurfaceBuilder COSMIC_SURFACE = new CosmicSurfaceBuilder(SurfaceBuilderConfig::deserialize);
    public static final ChunkGeneratorType<CosmicChunkSettings, CosmicChunkGenerator> COSMIC_CHUNK_GENERATOR = new ChunkGeneratorType<>(CosmicChunkGenerator::new,false, CosmicChunkSettings::new);


    public static void setup() {

    }

    @SubscribeEvent
    public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
        event.getRegistry().register(COSMIC_SEA = new BiomeCosmicSea());

    }

    @SubscribeEvent
    public static void registerChunkGenerator(final RegistryEvent.Register<ChunkGeneratorType<?, ?>> event) {
        COSMIC_CHUNK_GENERATOR.setRegistryName("astro:cosmic_chunk_generator");
        event.getRegistry().register(COSMIC_CHUNK_GENERATOR);

    }

    @SubscribeEvent
    public static void registerModDimensions(final RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().register(COSMIC_SEA_MOD_DIMENSION);
    }

    @SubscribeEvent
    public static void registerDimensionTypes(RegisterDimensionsEvent event) {
        COSMIC_SEA_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation("astro:cosmic_sea"), COSMIC_SEA_MOD_DIMENSION, null, true);
    }
}
