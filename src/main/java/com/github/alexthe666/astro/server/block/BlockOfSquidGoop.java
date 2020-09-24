package com.github.alexthe666.astro.server.block;

import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
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

    protected static final VoxelShape HONEY_AABB = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public BlockOfSquidGoop() {
        super(Block.Properties.create(Material.GLASS, MaterialColor.BLUE).hardnessAndResistance(5.0F, 10F).variableOpacity().speedFactor(0.4F).jumpFactor(0.5F).notSolid().slipperiness(0.6F).sound(SoundType.field_226947_m_));
        this.setRegistryName("astro:squid_goop_block");
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return HONEY_AABB;
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.onLivingFall(fallDistance, 0.0F);
        entityIn.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
        if (worldIn.isRemote) {
            BlockState blockstate = AstroBlockRegistry.BLOCK_OF_SQUID_GOOP.getDefaultState();

            for(int i = 0; i < 3; ++i) {
                worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate), entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ(), 0.0D, 0.0D, 0.0D);
            }

        }

    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (this.func_226935_a_(pos, entityIn)) {
            this.func_226933_a_(entityIn, pos);
            this.func_226938_d_(entityIn);
            this.func_226934_a_(worldIn, entityIn);
        }

        super.onEntityCollision(state, worldIn, pos, entityIn);
    }

    private void func_226933_a_(Entity p_226933_1_, BlockPos p_226933_2_) {
        if (p_226933_1_ instanceof ServerPlayerEntity && p_226933_1_.world.getGameTime() % 20L == 0L) {
            CriteriaTriggers.field_229864_K_.func_227152_a_((ServerPlayerEntity)p_226933_1_, p_226933_1_.world.getBlockState(p_226933_2_));
        }

    }

    private void func_226938_d_(Entity p_226938_1_) {
        Vector3d vector3d = p_226938_1_.getMotion();
        if (vector3d.y < -0.13D) {
            double d0 = -0.05D / vector3d.y;
            p_226938_1_.setMotion(new Vector3d(vector3d.x * d0, -0.05D, vector3d.z * d0));
        } else {
            p_226938_1_.setMotion(new Vector3d(vector3d.x, -0.05D, vector3d.z));
        }

        p_226938_1_.fallDistance = 0.0F;
    }

    private void func_226934_a_(World p_226934_1_, Entity p_226934_2_) {
        if (func_226937_c_(p_226934_2_) && !(p_226934_2_ instanceof EntitySpaceSquid)) {
            if (p_226934_1_.rand.nextInt(5) == 0) {
                p_226934_2_.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }

            if (p_226934_1_.isRemote && p_226934_1_.rand.nextInt(5) == 0) {
                BlockState blockstate = AstroBlockRegistry.BLOCK_OF_SQUID_GOOP.getDefaultState();

                for(int i = 0; i < 3; ++i) {
                    p_226934_1_.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate), p_226934_2_.getPosX(), p_226934_2_.getPosY(), p_226934_2_.getPosZ(), 0.0D, 0.0D, 0.0D);
                }
            }
        }

    }

    private static boolean func_226937_c_(Entity p_226937_0_) {
        return p_226937_0_ instanceof LivingEntity || p_226937_0_ instanceof AbstractMinecartEntity || p_226937_0_ instanceof TNTEntity || p_226937_0_ instanceof BoatEntity;
    }

    private boolean func_226935_a_(BlockPos p_226935_1_, Entity p_226935_2_) {
        if (p_226935_2_.func_233570_aj_()) {
            return false;
        } else if (p_226935_2_.getPosY() > (double) p_226935_1_.getY() + 0.9375D - 1.0E-7D) {
            return false;
        } else if (p_226935_2_.getMotion().y >= -0.08D) {
            return false;
        } else {
            double d0 = Math.abs((double) p_226935_1_.getX() + 0.5D - p_226935_2_.getPosX());
            double d1 = Math.abs((double) p_226935_1_.getZ() + 0.5D - p_226935_2_.getPosZ());
            double d2 = 0.4375D + (double) (p_226935_2_.getWidth() / 2.0F);
            return d0 + 1.0E-7D > d2 || d1 + 1.0E-7D > d2;
        }
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
        if (entityIn instanceof EntitySpaceSquid) {
            ((EntitySpaceSquid) entityIn).suffocateCounter = 0;
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").func_240699_a_(TextFormatting.GRAY));
    }
}