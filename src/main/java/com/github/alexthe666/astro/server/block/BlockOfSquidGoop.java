package com.github.alexthe666.astro.server.block;

import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

public class BlockOfSquidGoop extends BreakableBlock {

    public BlockOfSquidGoop() {
        super(Block.Properties.create(Material.GLASS, MaterialColor.BLUE).variableOpacity().notSolid().slipperiness(0.6F).sound(SoundType.SLIME));
        this.setRegistryName("astro:squid_goop_block");
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.onLivingFall(fallDistance, 0.0F);
    }

    public boolean canStickTo(BlockState state, BlockState other) {
        if (state.getBlock() == this && other.getBlock() == this) return false;
        return state.isStickyBlock() || other.isStickyBlock();
    }

    /**
     * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
     * on its own
     */
    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);

    }

    public boolean isStickyBlock(BlockState state) {
        return true;
    }

    /**
     * Called when the given entity walks on this Block
     */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        double d0 = Math.abs(entityIn.getMotion().y);
        if (d0 < 0.1D && !entityIn.isSteppingCarefully()) {
            double d1 = 0.1D + d0 * 0.2D;
            entityIn.setMotion(entityIn.getMotion().mul(d1, d1, d1));
        }
        if(entityIn instanceof EntitySpaceSquid){
            ((EntitySpaceSquid) entityIn).suffocateCounter = 0;
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").applyTextStyle(TextFormatting.GRAY));
    }
}