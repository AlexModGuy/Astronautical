package com.github.alexthe666.astro.server.block;

import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPlanetoidGas extends BreakableBlock {

    public int colorBase = 0X000000;
    public BlockPlanetoidGas(String color, int colorBase) {
        super(Block.Properties.create(Material.GLASS, MaterialColor.BLUE).hardnessAndResistance(1.0F, 100F).variableOpacity().notSolid().doesNotBlockMovement().sound(SoundType.CLOTH));
        this.setRegistryName("astro:planetoid_gas_" + color);
        this.colorBase = colorBase;
    }


    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.onLivingFall(fallDistance, 0.0F);
    }


    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        entityIn.setMotionMultiplier(state, new Vec3d(0.75D, 0.75D, 0.75D));
    }
}