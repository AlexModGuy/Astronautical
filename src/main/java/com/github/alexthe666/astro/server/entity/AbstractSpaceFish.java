package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.server.entity.ai.SpaceFishMoveHelper;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractSpaceFish extends AnimalEntity {
    @Nullable
    public Vec3d flightTarget = Vec3d.ZERO;
    public float prevFishPitch;
    private static final DataParameter<Float> FISH_PITCH = EntityDataManager.createKey(AbstractSpaceFish.class, DataSerializers.FLOAT);

    protected AbstractSpaceFish(EntityType type, World world) {
        super(type, world);
        initPathFinding();
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        Vec3d vec3d = new Vec3d(this.getPosX(), this.getPosYEye(), this.getPosZ());
        Vec3d vec3d1 = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() == RayTraceResult.Type.MISS;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FISH_PITCH, 0F);
    }

    public void initPathFinding(){
        this.moveController = new SpaceFishMoveHelper(this);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) {
        return null;
    }

    public void tick() {
        super.tick();
        this.fallDistance = 0.0F;
        this.setAir(200);
        prevFishPitch = this.getFishPitch();
        if(flightTarget == Vec3d.ZERO){
            flightTarget = this.getPositionVec();
        }
        if (!this.onGround && this.getMotion().y < 0.0D) {
            this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }
        this.setMotion(this.getMotion().x, this.getMotion().y + 0.08D, this.getMotion().z);
        if(!onGround){
            double ydist = (this.prevPosY - this.getPosY());//down 0.4 up -0.38
            float fishDist = (float) ((Math.abs(this.getMotion().getX()) + Math.abs(this.getMotion().getZ())) * 6F);
            this.incrementFishPitch((float) (ydist) * 10);

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
}
