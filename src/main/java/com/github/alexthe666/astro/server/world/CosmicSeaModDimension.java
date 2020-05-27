package com.github.alexthe666.astro.server.world;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

import java.util.function.BiFunction;

public class CosmicSeaModDimension extends ModDimension {
    private BiFunction<World, DimensionType, ? extends Dimension> factory;

    public CosmicSeaModDimension(BiFunction<World, DimensionType, ? extends Dimension> factory) {
        this.factory = factory;
    }

    @Override
    public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
        return this.factory;
    }
}