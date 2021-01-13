package com.github.alexthe666.astro.server.entity.tileentity;

import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.misc.AstroParticleRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class TileEntitySquidTank extends TileEntity implements ITickableTileEntity {

    public int ticksExisted = 0;

    public TileEntitySquidTank() {
        super(AstroTileEntityRegistry.SQUID_TANK);
    }


    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        return new net.minecraft.util.math.AxisAlignedBB(pos.add(-3, -4, -3), pos.add(3, 4, 3));
    }

    private boolean doesBlockEqual(BlockPos pos, Block block) {
        return world.getBlockState(pos).getBlock() == block;
    }

    public void tick() {
        ticksExisted++;
        Random rand = world.rand;
        if(world.isRemote){
            double particleX = this.pos.getX() + 0.5D - 1.4F + rand.nextFloat() * 2.4F;
            double particleY = this.pos.getY() + 0.5D - 2F + rand.nextFloat() * 2F;
            double particleZ = this.pos.getZ() + 0.5D - 1.4F + rand.nextFloat() * 2.4F;
            world.addParticle(AstroParticleRegistry.SQUID_BUBBLE, particleX, particleY, particleZ, 0, 0, 0);
        }
    }

    public void checkAndBreak() {
        if (!isValidLoc()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        BlockState state = world.getBlockState(pos.add(i, j, k));
                        if (state.getBlock() == AstroBlockRegistry.SQUID_TANK || state.getBlock() == AstroBlockRegistry.SQUID_TANK_GLASS) {
                            world.destroyBlock(pos.add(i, j, k), false);
                        }

                    }
                }
            }
        }
    }

    public boolean isValidLoc() {
        for (int i = -1; i < 1; i++) {
            for (int j = -2; j < 2; j++) {
                for (int k = -1; k < 1; k++) {
                    BlockState state = world.getBlockState(pos.add(i, j, k));
                    if (state.getBlock() != AstroBlockRegistry.SQUID_TANK && state.getBlock() != AstroBlockRegistry.SQUID_TANK_GLASS) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
