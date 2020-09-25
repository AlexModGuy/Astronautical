package com.github.alexthe666.astro.server.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockPlanetoidCore extends Block {

    protected static final VoxelShape HONEY_AABB = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);


    public BlockPlanetoidCore() {
        super(Block.Properties.create(Material.ROCK).func_235838_a_((p_235470_0_) -> { return 3; }).hardnessAndResistance(25, 100).harvestTool(ToolType.PICKAXE).harvestLevel(2));
        this.setRegistryName("astro:planetoid_core");
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return HONEY_AABB;
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.func_230279_az_() && entityIn instanceof LivingEntity) {
            entityIn.setFire(2);
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 5.0F);
        }
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.func_230279_az_() && entityIn instanceof LivingEntity) {
            entityIn.setFire(5);
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 5.0F);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
