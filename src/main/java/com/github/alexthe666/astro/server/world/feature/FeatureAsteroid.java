package com.github.alexthe666.astro.server.world.feature;

import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

public class FeatureAsteroid extends Feature<NoFeatureConfig> {

    public FeatureAsteroid(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49873_1_) {
        super(p_i49873_1_);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if(rand.nextFloat() > getAsteroidChance()){
            return false;
        }
        BlockPos core = new BlockPos(pos.getX() + 8, 15 + rand.nextInt(240), pos.getZ()  + 8);
        int lvt_6_2_ = getAsteroidSize(rand);

        for (int k = 0; k < 1 + rand.nextInt(3); k++){
            int lvt_8_1_ = (int) (lvt_6_2_ * rand.nextFloat());
            int lvt_9_1_ =  (int) (lvt_6_2_ * rand.nextFloat());
            int lvt_10_1_ =  (int) (lvt_6_2_ * rand.nextFloat());
            float lvt_11_1_ = (float)(lvt_8_1_ + lvt_9_1_ + lvt_10_1_) * 0.333F + 0.5F;
            Iterator var12 = BlockPos.getAllInBoxMutable(core.add(-lvt_8_1_, -lvt_9_1_, -lvt_10_1_), core.add(lvt_8_1_, lvt_9_1_, lvt_10_1_)).iterator();

            while(var12.hasNext()) {
                BlockPos lvt_13_1_ = (BlockPos)var12.next();
                if (lvt_13_1_.distanceSq(core) <= (double)(lvt_11_1_ * lvt_11_1_)) {
                    double distanceTotal = lvt_13_1_.distanceSq(core) / (double)(lvt_11_1_ * lvt_11_1_);
                    if(rand.nextFloat() > 0.3F){
                        worldIn.setBlockState(lvt_13_1_, AstroBlockRegistry.METEORITE.getDefaultState(), 4);
                    }
                }
            }

            core = core.add(-(lvt_6_2_ + 1) + rand.nextInt(2 + lvt_6_2_ * 2), 0 - rand.nextInt(2), -(lvt_6_2_ + 1) + rand.nextInt(2 + lvt_6_2_ * 2));
        }
        return true;
    }

    protected double getAsteroidChance(){
        return 0.015D;
    }

    protected int getAsteroidSize(Random random){
        return 3 + random.nextInt(7);
    }
}
