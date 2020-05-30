package com.github.alexthe666.astro.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPlanetoidRing extends BreakableBlock {

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 6.0D, 0.0D, 16.0D, 10.0D, 16.0D);
    public int colorBase = 0X000000;
    public BlockPlanetoidRing(String color, int colorBase) {
        super(Properties.create(Material.GLASS, MaterialColor.BLUE).lightValue(3).hardnessAndResistance(3.0F, 100F).variableOpacity().notSolid().sound(SoundType.GLASS));
        this.setRegistryName("astro:planetoid_ring_" + color);
        this.colorBase = colorBase;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEmissiveRendering(BlockState p_225543_1_) {
        return true;
    }

    public boolean needsPostProcessing(BlockState p_201783_1_, IBlockReader p_201783_2_, BlockPos p_201783_3_) {
        return true;
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.onLivingFall(fallDistance, 0.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState p_200122_1_, BlockState p_200122_2_, Direction p_200122_3_) {
        return p_200122_2_.getBlock() == this  && p_200122_3_.getAxis() != Direction.Axis.Y ? true : false;
    }

    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);
    }
}