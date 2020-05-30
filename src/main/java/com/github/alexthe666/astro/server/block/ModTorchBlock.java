package com.github.alexthe666.astro.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ModTorchBlock extends TorchBlock implements IWallAndFloor{

    public ModTorchBlock(Properties p_i48308_1_) {
        super(p_i48308_1_);
    }

    @Override
    public Block wallBlock() {
        return AstroBlockRegistry.WALL_BURNT_TORCH;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
    }
}
