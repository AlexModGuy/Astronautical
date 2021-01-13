package com.github.alexthe666.astro.server.block;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.entity.tileentity.TileEntityBlockitHole;
import com.github.alexthe666.astro.server.entity.tileentity.TileEntitySquidTank;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockSquidTank extends ContainerBlock implements INoTab{

    public BlockSquidTank() {
        super(Properties.create(Material.GLASS).func_235838_a_((p_235470_0_) -> { return 10; }).func_235827_a_((a, b, c, d) -> false).sound(SoundType.GLASS).notSolid().variableOpacity().hardnessAndResistance(4.0F, 0.0F));
        this.setRegistryName(Astronautical.MODID, "squid_tank");
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return (adjacentBlockState.getBlock() == this || adjacentBlockState.getBlock() == AstroBlockRegistry.SQUID_TANK_GLASS) || super.isSideInvisible(state, adjacentBlockState, side);
    }

    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(worldIn.getTileEntity(pos) instanceof TileEntitySquidTank){
            ((TileEntitySquidTank) worldIn.getTileEntity(pos)).checkAndBreak();
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntitySquidTank();
    }

}
