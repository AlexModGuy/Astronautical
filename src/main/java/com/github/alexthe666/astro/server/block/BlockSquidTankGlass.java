package com.github.alexthe666.astro.server.block;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.entity.tileentity.TileEntitySquidTank;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSquidTankGlass extends Block implements INoTab{

    public BlockSquidTankGlass() {
        super(Properties.create(Material.GLASS).func_235838_a_((p_235470_0_) -> { return 2; }).func_235827_a_((a, b, c, d) -> false).sound(SoundType.GLASS).tickRandomly().notSolid().variableOpacity().hardnessAndResistance(4.0F, 0.0F));
        this.setRegistryName(Astronautical.MODID, "squid_tank_glass");
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        this.tick(state, worldIn, pos, random);
    }

    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        checkAndAlertTanks(worldIn, pos);
    }


    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return (adjacentBlockState.getBlock() == this || adjacentBlockState.getBlock() == AstroBlockRegistry.SQUID_TANK) || super.isSideInvisible(state, adjacentBlockState, side);
    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
    }

    @Deprecated
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        checkAndAlertTanks(worldIn, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        checkAndAlertTanks(worldIn, pos);
        for(Direction dir : Direction.values()){
            if(worldIn.getBlockState(pos.offset(dir)).getBlock() == AstroBlockRegistry.SQUID_TANK_GLASS){
                worldIn.destroyBlock(pos.offset(dir), false);
                checkAndAlertTanks(worldIn, pos.offset(dir));
            }
        }
    }

    public void checkAndAlertTanks(World world, BlockPos pos){
        for(int i = -1; i <= 1; i++){
            for(int j = -4; j <= 4; j++){
                for(int k = -1; k <= 1; k++){
                    BlockPos add = pos.add(i, j, k);
                    TileEntity te = world.getTileEntity(add);
                    if(te instanceof TileEntitySquidTank){
                        ((TileEntitySquidTank) te).checkAndBreak();
                    }
                }
            }
        }
    }


    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }



}
