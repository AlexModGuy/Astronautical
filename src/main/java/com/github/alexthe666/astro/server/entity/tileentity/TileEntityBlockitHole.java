package com.github.alexthe666.astro.server.entity.tileentity;

import com.github.alexthe666.astro.server.block.BlockBlockitWormHole;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileEntityBlockitHole extends TileEntity implements ITickableTileEntity {

    public float extendProgress;
    public float prevExtendProgress;
    public float ticksExisted;
    private boolean previouslyActive = false;
    private boolean active = false;
    private boolean jumpOut = false;
    private Direction direction = Direction.UP;

    public TileEntityBlockitHole() {
        super(AstroTileEntityRegistry.BLOCKIT_HOLE);
    }

    @Override
    public void tick() {
        ticksExisted++;
        prevExtendProgress = extendProgress;
        previouslyActive = active;
        if(getBlockState().getBlock() instanceof BlockBlockitWormHole){
            ((BlockBlockitWormHole)getBlockState().getBlock()).updateState(getBlockState(), world, pos, getBlockState().getBlock());
            direction = getBlockState().get(BlockBlockitWormHole.FACING);
            active = getBlockState().get(BlockBlockitWormHole.POWERED);
        }
        if(active && !jumpOut && extendProgress <= 0){
            jumpOut = true;
            if(world.isRemote){
                for(int m = 0; m < 15; m++){
                    float i = this.getPos().getX() + 0.5F;
                    float j = this.getPos().getY() + 0.5F;
                    float k = this.getPos().getZ() + 0.5F;
                    float f1 = 0.6F + 0.4F;
                    float f2 = 1;
                    float f3 = 0;
                    world.addParticle(RedstoneParticleData.REDSTONE_DUST, i + (world.rand.nextDouble() - 0.5D) * 1.2F, j + (world.rand.nextDouble() - 0.5D) * 1.2F, k + (world.rand.nextDouble() - 0.5D) * 1.2F, f1, f2, f3);
                }
            }
        }
        if(!jumpOut && extendProgress >= 0.0F){
            extendProgress -= 0.5F;
        }
        if(jumpOut && extendProgress <= 20F){
            extendProgress += 2F;
            if(extendProgress == 10F && extendProgress > prevExtendProgress){
                breakBlockInFront(1);
            }
            if(extendProgress == 20F && extendProgress > prevExtendProgress){
                breakBlockInFront(2);
                jumpOut = false;
            }
        }

        extendProgress = MathHelper.clamp(extendProgress, 0, 20);
    }

    private void breakBlockInFront(int reach) {
        BlockPos breakPos = this.getPos().offset(direction, reach);
        BlockState state = world.getBlockState(breakPos);
        if (state.getPushReaction() != PushReaction.IGNORE && !state.isAir() && !state.getShape(world, breakPos).isEmpty()) {
            if (WitherEntity.canDestroyBlock(state)) {
                world.destroyBlock(breakPos, true);
            }
        }
        AxisAlignedBB killBox = new AxisAlignedBB(breakPos.getX() - 0.5F, breakPos.getY() - 0.5F, breakPos.getZ() - 0.5F, breakPos.getX() + 0.5F, breakPos.getY() + 0.5F, breakPos.getZ() + 0.5F);
        for (LivingEntity kill : world.getEntitiesWithinAABB(LivingEntity.class, killBox)) {
            kill.attackEntityFrom(DamageSource.DROWN, 2);
            if(!(kill instanceof PlayerEntity) || ((PlayerEntity) kill).isCreative()){
                kill.knockBack(kill, 0.5F, kill.getPosX() - (this.getPos().getX() + 0.5F), kill.getPosZ() - (this.getPos().getZ() + 0.5F));
            }
        }
    }

    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        return new net.minecraft.util.math.AxisAlignedBB(pos.add(-3, -3, -3), pos.add(3, 3, 3));
    }

}
