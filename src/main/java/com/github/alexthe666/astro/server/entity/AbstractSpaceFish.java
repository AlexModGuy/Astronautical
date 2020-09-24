package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.server.entity.ai.SpaceFishMoveHelper;
import com.github.alexthe666.astro.server.world.AstroWorldRegistry;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class AbstractSpaceFish extends AnimalEntity {
    @Nullable
    public Vector3d flightTarget = Vector3d.ZERO;
    public float prevFishPitch;
    private static final DataParameter<Float> FISH_PITCH = EntityDataManager.createKey(AbstractSpaceFish.class, DataSerializers.FLOAT);

    protected AbstractSpaceFish(EntityType type, World world) {
        super(type, world);
        initPathFinding();
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        double x = pos.getX() + 0.5F;
        double y = pos.getY() + 0.5F;
        double z = pos.getZ() + 0.5F;
        RayTraceResult result = this.world.rayTraceBlocks(new RayTraceContext(new Vector3d(this.getPosX(), this.getPosY() + (double) this.getEyeHeight(), this.getPosZ()), new Vector3d(x, y, z), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        double dist = result.getHitVec().squareDistanceTo(x, y, z);
        return dist <= 1.0D || result.getType() == RayTraceResult.Type.MISS;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FISH_PITCH, 0F);
    }

    public void initPathFinding(){
        this.moveController = new SpaceFishMoveHelper(this);
    }

    public void tick() {
        super.tick();
        this.fallDistance = 0.0F;
        this.setAir(200);
        prevFishPitch = this.getFishPitch();
        if(flightTarget == Vector3d.ZERO){
            flightTarget = this.getPositionVec();
        }
        if (!this.onGround && this.getMotion().y < 0.0D) {
           // this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }
        if(!onGround){
            double ydist = (this.prevPosY - this.getPosY());//down 0.4 up -0.38
            float fishDist = (float) ((Math.abs(this.getMotion().getX()) + Math.abs(this.getMotion().getZ())) * 6F) / getPitchSensitivity();
            this.incrementFishPitch((float) (ydist) * 10 * getPitchSensitivity());

            this.setFishPitch(MathHelper.clamp(this.getFishPitch(), -60, 40));
            float plateau = 2;
            if (this.getFishPitch() > plateau) {
                //down
                //this.motionY -= 0.2D;
                this.decrementFishPitch(fishDist * Math.abs(this.getFishPitch()) / 90);
            }
            if (this.getFishPitch() < -plateau) {//-2
                //up
                this.incrementFishPitch(fishDist * Math.abs(this.getFishPitch()) / 90);
            }
            if (this.getFishPitch() > 2F) {
                this.decrementFishPitch(1);
            } else if (this.getFishPitch() < -2F) {
                this.incrementFishPitch(1);
            }
        } else {
            this.setFishPitch(0);
        }
    }

    public boolean hasNoGravity() {
        return true;
    }


    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isPushedByWater() {
        return false;
    }

    public float getFishPitch() {
        return dataManager.get(FISH_PITCH).floatValue();
    }

    public void setFishPitch(float pitch) {
        dataManager.set(FISH_PITCH, pitch);
    }

    public void incrementFishPitch(float pitch) {
        dataManager.set(FISH_PITCH, getFishPitch() + pitch);
    }

    public void decrementFishPitch(float pitch) {
        dataManager.set(FISH_PITCH, getFishPitch() - pitch);
    }

    public float getSwimSpeedModifier(){
        return 0.4F;
    }

    public float getPitchSensitivity(){ return 3F; }

    public static boolean canSpaceFishSpawn(EntityType<? extends AnimalEntity> p_223316_0_, IWorld p_223316_1_, SpawnReason p_223316_2_, BlockPos p_223316_3_, Random p_223316_4_) {
        return true;
    }

}
