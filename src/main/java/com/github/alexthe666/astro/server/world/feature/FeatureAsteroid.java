package com.github.alexthe666.astro.server.world.feature;

import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.block.BlockStarnacle;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        boolean starnacles = rand.nextBoolean();
        BlockPos core = new BlockPos(pos.getX() + 8, 15 + rand.nextInt(240), pos.getZ()  + 8);
        int size = getAsteroidSize(rand);
        int count = 1 + rand.nextInt(3);
        List<BlockPos> starnacleClusterCenters = new ArrayList<>();
        if(starnacles){
            int starnacleSize = size + 3;
            for(int i = 0; i < count; i++){
                starnacleClusterCenters.add(core.add((int) (starnacleSize * rand.nextFloat()) - starnacleSize/2, (int) (starnacleSize * rand.nextFloat()) - starnacleSize / 2, (int) (starnacleSize * rand.nextFloat()) - starnacleSize / 2));
            }
        }
        for (int k = 0; k < count; k++){
            int lvt_8_1_ = (int) (size * rand.nextFloat()) + 1;
            int lvt_9_1_ =  (int) (size * rand.nextFloat()) + 1;
            int lvt_10_1_ =  (int) (size * rand.nextFloat()) + 1;
            float radius = (float)(lvt_8_1_ + lvt_9_1_ + lvt_10_1_) * 0.333F + 0.5F;
            Iterator var12 = BlockPos.getAllInBoxMutable(core.add(-lvt_8_1_ - 1, -lvt_9_1_ - 1, -lvt_10_1_ - 1), core.add(lvt_8_1_ + 1, lvt_9_1_ + 1, lvt_10_1_ + 1)).iterator();

            while(var12.hasNext()) {
                BlockPos lvt_13_1_ = (BlockPos)var12.next();
                if (lvt_13_1_.distanceSq(core) <= (double)(radius * radius)) {
                    double distanceTotal = lvt_13_1_.distanceSq(core) / (double)(radius * radius);
                    if(rand.nextFloat() > 0.3F){
                        BlockState block = AstroBlockRegistry.METEORITE.getDefaultState();
                        if(rand.nextFloat() < 0.01F){
                            block = AstroBlockRegistry.METEORITE_IRON_ORE.getDefaultState();
                        }
                        worldIn.setBlockState(lvt_13_1_, block, 4);
                    }
                }else if(lvt_13_1_.distanceSq(core) - 2 <= (double)(radius * radius)){
                    Direction facing = null;
                    if(worldIn.isAirBlock(lvt_13_1_)){
                        for(Direction direction : Direction.values()){
                            if(worldIn.getBlockState(lvt_13_1_.offset(direction)).isSolid()){
                                facing = direction.getOpposite();
                                break;
                            }
                        }
                        if(facing != null){
                            decorateAsteroid(worldIn, lvt_13_1_, facing);
                        }
                    }
                }
            }
            core = core.add(-(size + 1) + rand.nextInt(2 + size * 2), 0 - rand.nextInt(2), -(size + 1) + rand.nextInt(2 + size * 2));
        }
        if(starnacles){
            for(BlockPos center : starnacleClusterCenters){
                int starnacleSize = rand.nextInt(2);
                int lvt_8_1_ = (int) (starnacleSize * rand.nextFloat()) + 1;
                int lvt_9_1_ =  (int) (starnacleSize * rand.nextFloat()) + 1;
                int lvt_10_1_ =  (int) (starnacleSize * rand.nextFloat()) + 1;
                float radius = (float)(lvt_8_1_ + lvt_9_1_ + lvt_10_1_) * 0.333F + 0.5F;
                Iterator var12 = BlockPos.getAllInBoxMutable(center.add(-lvt_8_1_ - 1, -lvt_9_1_ - 1, -lvt_10_1_ - 1), center.add(lvt_8_1_ + 1, lvt_9_1_ + 1, lvt_10_1_ + 1)).iterator();

                while(var12.hasNext()) {
                    BlockPos lvt_13_1_ = (BlockPos)var12.next();
                    if (lvt_13_1_.distanceSq(center) <= (double)(radius * radius)) {
                        if(worldIn.getBlockState(lvt_13_1_).isSolid()) {
                            for(Direction direction : Direction.values()){
                                if(!worldIn.getBlockState(lvt_13_1_.offset(direction)).isSolid()){
                                    BlockState state = AstroBlockRegistry.STARNACLE.getDefaultState().with(BlockStarnacle.COUNT, 1 + rand.nextInt(3));
                                    worldIn.setBlockState(lvt_13_1_.offset(direction), state.with(BlockStarnacle.FACING, direction), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    protected double getAsteroidChance(){
        return 0.015D;
    }

    protected int getAsteroidSize(Random random){
        return 3 + random.nextInt(7);
    }

    public void decorateAsteroid(IWorld worldIn, BlockPos pos, Direction facing){

    }
}
