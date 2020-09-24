package com.github.alexthe666.astro.server.world.feature;

import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeaturePlanetoid extends Feature<NoFeatureConfig> {

    public FeaturePlanetoid(Codec<NoFeatureConfig> p_i231953_1_) {
        super(p_i231953_1_);
    }


    @Override
    public boolean func_241855_a(ISeedReader worldIn, ChunkGenerator p_230362_3_, Random rand, BlockPos pos, NoFeatureConfig p_230362_6_) {
        if(rand.nextFloat() > 0.005D){
            return false;
        }
        Block[] possibleGasses = new Block[]{AstroBlockRegistry.PLANETOID_GAS_BLUE, AstroBlockRegistry.PLANETOID_GAS_GREEN, AstroBlockRegistry.PLANETOID_GAS_ORANGE, AstroBlockRegistry.PLANETOID_GAS_PURPLE, AstroBlockRegistry.PLANETOID_GAS_TEAL, AstroBlockRegistry.PLANETOID_GAS_YELLOW};
        Block[] possibleRings = new Block[]{AstroBlockRegistry.PLANETOID_RING_BLUE, AstroBlockRegistry.PLANETOID_RING_GREEN, AstroBlockRegistry.PLANETOID_RING_ORANGE, AstroBlockRegistry.PLANETOID_RING_PURPLE, AstroBlockRegistry.PLANETOID_RING_TEAL, AstroBlockRegistry.PLANETOID_RING_YELLOW};
        int gasIndex = rand.nextInt(possibleGasses.length - 1);
        int ringIndex = rand.nextInt(possibleRings.length - 1);
        BlockPos core = new BlockPos(pos.getX() + 8, 30 + rand.nextInt(210), pos.getZ()  + 8);
        int radius = 3 + rand.nextInt(4);
        for(int i = -radius; i < radius; i++){
            for(int j = -radius; j < radius; j++){
                for(int k = -radius; k < radius; k++){
                    worldIn.setBlockState(core.add(i, j, k), possibleGasses[gasIndex].getDefaultState(), 2);

                }
            }
        }
        int ringRadius = rand.nextInt(4) + radius * 2;
        int ringWidth = rand.nextInt(2);
        for(int lvt_8_1_ = -ringRadius; lvt_8_1_ <= ringRadius; ++lvt_8_1_) {
            for(int lvt_9_1_ = -ringRadius; lvt_9_1_ <= ringRadius; ++lvt_9_1_) {
                float dist = (float)(lvt_8_1_ * lvt_8_1_ + lvt_9_1_ * lvt_9_1_);
                if (dist <= (ringRadius + 1.0F) * (ringRadius + 1.0F) && dist > (ringRadius * ringRadius - 1.0D)) {
                    worldIn.setBlockState(core.add(lvt_8_1_, 0, lvt_9_1_), possibleRings[ringIndex].getDefaultState(), 2);
                }
            }
        }
        worldIn.setBlockState(core, AstroBlockRegistry.PLANETOID_CORE.getDefaultState(), 2);
        worldIn.setBlockState(core.offset(Direction.UP), AstroBlockRegistry.PLANETOID_CORE.getDefaultState(), 2);
        worldIn.setBlockState(core.offset(Direction.DOWN), AstroBlockRegistry.PLANETOID_CORE.getDefaultState(), 2);
        worldIn.setBlockState(core.offset(Direction.EAST), AstroBlockRegistry.PLANETOID_CORE.getDefaultState(), 2);
        worldIn.setBlockState(core.offset(Direction.WEST), AstroBlockRegistry.PLANETOID_CORE.getDefaultState(), 2);
        worldIn.setBlockState(core.offset(Direction.SOUTH), AstroBlockRegistry.PLANETOID_CORE.getDefaultState(), 2);
        worldIn.setBlockState(core.offset(Direction.NORTH), AstroBlockRegistry.PLANETOID_CORE.getDefaultState(), 2);

        return true;
    }
}
