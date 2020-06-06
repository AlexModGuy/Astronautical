package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.server.entity.ai.FollowSpaceFishSchoolLeader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractSchoolingSpaceFish extends AbstractSpaceFish {
    private AbstractSchoolingSpaceFish groupLeader;
    private int groupSize = 1;
    private BlockPos circlingPosition;

    protected AbstractSchoolingSpaceFish(EntityType type, World world) {
        super(type, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new FollowSpaceFishSchoolLeader(this));
    }

    public void leaveGroup() {
        this.groupLeader.decreaseGroupSize();
        this.groupLeader = null;
    }

    public int getMaxSpawnedInChunk() {
        return super.getMaxSpawnedInChunk();
    }

    public int getMaxGroupSize() {
        return super.getMaxSpawnedInChunk();
    }

    protected boolean hasNoLeader() {
        return !this.hasGroupLeader();
    }

    public boolean hasGroupLeader() {
        return this.groupLeader != null && this.groupLeader.isAlive();
    }

    private void increaseGroupSize() {
        ++this.groupSize;
    }

    private void decreaseGroupSize() {
        --this.groupSize;
    }

    public boolean canGroupGrow() {
        return this.isGroupLeader() && this.groupSize < this.getMaxGroupSize();
    }

    public void tick() {
        super.tick();
        if (this.isGroupLeader() && this.world.rand.nextInt(200) == 1) {
            List<AbstractSchoolingSpaceFish> lvt_1_1_ = this.world.getEntitiesWithinAABB(this.getClass(), this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
            if (lvt_1_1_.size() <= 1) {
                this.groupSize = 1;
            }
        }
        if(this.isGroupLeader() || groupLeader == null){
            if(flightTarget == null || circlingPosition == null || rand.nextFloat() < 0.05F){
                if(circlingPosition == null || rand.nextFloat() < 0.25F){
                    BlockPos height = world.getHeight(Heightmap.Type.WORLD_SURFACE, this.getPosition());
                    int upDistance = world.getMaxHeight() - height.getY();
                    circlingPosition = height.up(rand.nextInt(Math.max(1, upDistance))).add(rand.nextInt(16) - 8, 0, rand.nextInt(16) - 8);
                }
                flightTarget = new Vec3d(circlingPosition);
            }
        }

    }


    private BlockPos getCirclingPosition() {
        return circlingPosition;
    }

    private Vec3d getBlockInViewCircling() {
        float radius = 12;
        float neg = this.getRNG().nextBoolean() ? 1 : -1;
        float renderYawOffset = this.renderYawOffset;
        BlockPos circlingPos = this.getCirclingPosition();
        BlockPos ground = this.world.getHeight(Heightmap.Type.WORLD_SURFACE, circlingPos);
        int distFromGround = circlingPos.getY() - ground.getY();
        int fromHome = 30;
        for (int i = 0; i < 10; i++) {
            BlockPos pos = new BlockPos(circlingPos.getX() + this.getRNG().nextInt(fromHome) - fromHome / 2,
                    (distFromGround > 16 ? circlingPos.getY() : circlingPos.getY() + 5 + this.getRNG().nextInt(26)),
                    (circlingPos.getZ() + this.getRNG().nextInt(fromHome) - fromHome / 2));
            if (canBlockPosBeSeen(pos) && this.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 6) {
                return new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
            }
        }
        return null;
    }

    public boolean isGroupLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.getDistanceSq(this.groupLeader) <= 121.0D;
    }

    public void moveToGroupLeader() {
        if (this.hasGroupLeader()) {
            this.getNavigator().tryMoveToEntityLiving(this.groupLeader, 1.0D);
            this.flightTarget = this.groupLeader.getPositionVec();
        }

    }

    public AbstractSchoolingSpaceFish createAndSetLeader(AbstractSchoolingSpaceFish leader) {
        this.groupLeader = leader;
        leader.increaseGroupSize();
        return leader;
    }


    public void createFromStream(Stream<AbstractSchoolingSpaceFish> stream) {
        stream.limit((long)(this.getMaxGroupSize() - this.groupSize)).filter((fishe) -> {
            return fishe != this;
        }).forEach((fishe) -> {
            fishe.createAndSetLeader(this);
        });
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        super.onInitialSpawn(p_213386_1_, p_213386_2_, p_213386_3_, (ILivingEntityData)p_213386_4_, p_213386_5_);
        if (p_213386_4_ == null) {
            p_213386_4_ = new AbstractSchoolingSpaceFish.GroupData(this);
        } else {
            this.createAndSetLeader(((AbstractSchoolingSpaceFish.GroupData)p_213386_4_).groupLeader);
        }

        return (ILivingEntityData)p_213386_4_;
    }

    public static class GroupData implements ILivingEntityData {
        public final AbstractSchoolingSpaceFish groupLeader;

        public GroupData(AbstractSchoolingSpaceFish p_i49858_1_) {
            this.groupLeader = p_i49858_1_;
        }
    }
}
