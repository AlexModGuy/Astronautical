package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.block.BlockBlockitWormHole;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityBlockitWorm extends MonsterEntity {

    protected static final DataParameter<Boolean> JUMP_OUT = EntityDataManager.createKey(EntityBlockitWorm.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Direction> ATTACHED_FACE = EntityDataManager.createKey(EntityBlockitWorm.class, DataSerializers.DIRECTION);
    protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS = EntityDataManager.createKey(EntityBlockitWorm.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public float extendProgress;
    public float prevExtendProgress;
    private BlockPos currentAttachmentPosition;
    private int clientSideTeleportInterpolation;
    private int grabCooldown = 0;

    public EntityBlockitWorm(EntityType type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute buildAttributes() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 30.0D);
    }


    protected BodyController createBodyController() {
        return new EntityBlockitWorm.BodyHelperController(this);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(ATTACHED_FACE, Direction.DOWN);
        this.dataManager.register(ATTACHED_BLOCK_POS, Optional.empty());
        this.dataManager.register(JUMP_OUT, false);
    }

    public boolean isCustomNameVisible() {
        return false;
    }

    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        if (this.dataManager != null && this.ticksExisted != 0) {
            Optional<BlockPos> optional = this.dataManager.get(ATTACHED_BLOCK_POS);
            Optional<BlockPos> optional1 = Optional.of(new BlockPos(x, y, z));
            if (!optional1.equals(optional)) {
                this.dataManager.set(ATTACHED_BLOCK_POS, optional1);
                this.isAirBorne = true;
            }

        }
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isPushedByWater() {
        return false;
    }

    public void tick() {
        super.tick();
        this.fallDistance = 0.0F;
        this.setAir(200);
        this.prevExtendProgress = extendProgress;
        if(dataManager.get(JUMP_OUT) && extendProgress < 20F){
            extendProgress += 2F;
        }
        if(!dataManager.get(JUMP_OUT) && extendProgress > 0F){
            extendProgress -= 0.5F;
        }
        if(grabCooldown > 0){
            grabCooldown--;
        }
        List<Entity> possibleExtendTargets = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getMaximumExtendTargets());
        boolean hasPossiblePrey = false;
        if (!possibleExtendTargets.isEmpty()) {
            for(Entity entity : possibleExtendTargets) {
                if (entity instanceof LivingEntity &&  !(entity instanceof EntityBlockitWorm) && !entity.isPassenger()) {
                    if (EntityPredicates.CAN_AI_TARGET.test(entity)) {
                        hasPossiblePrey = true;
                    }

                }
            }
        }
        dataManager.set(JUMP_OUT, hasPossiblePrey);

        BlockPos blockpos = this.dataManager.get(ATTACHED_BLOCK_POS).orElse((BlockPos)null);
        if (blockpos == null && !this.world.isRemote) {
            blockpos = new BlockPos(this.getPositionVec());
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
        }

        if (this.isPassenger()) {
            blockpos = null;
            float f = this.getRidingEntity().rotationYaw;
            this.rotationYaw = f;
            this.renderYawOffset = f;
            this.prevRenderYawOffset = f;
            this.clientSideTeleportInterpolation = 0;
        } else if (!this.world.isRemote) {
            BlockState blockstate = this.world.getBlockState(blockpos);
            if (!blockstate.isAir(this.world, blockpos)) {
                if (blockstate.getBlock() == Blocks.MOVING_PISTON) {
                    Direction direction = blockstate.get(PistonBlock.FACING);
                    if (this.world.isAirBlock(blockpos.offset(direction))) {
                        blockpos = blockpos.offset(direction);
                        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
                    }
                } else if (blockstate.getBlock() == Blocks.PISTON_HEAD) {
                    Direction direction2 = blockstate.get(PistonHeadBlock.FACING);
                    if (this.world.isAirBlock(blockpos.offset(direction2))) {
                        blockpos = blockpos.offset(direction2);
                        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
                    }
                }
            }

            BlockPos blockpos1 = blockpos.offset(this.getAttachmentFacing().getOpposite());
            if (!this.world.isAirBlock(blockpos1)) {
                boolean flag = false;

                for(Direction direction1 : Direction.values()) {
                    blockpos1 = blockpos.offset(direction1);
                    if (this.world.isAirBlock(blockpos1)) {
                        this.dataManager.set(ATTACHED_FACE, direction1.getOpposite());
                        flag = true;
                        break;
                    }
                }
            }

            BlockPos blockpos2 = blockpos.offset(this.getAttachmentFacing().getOpposite());
            if (this.world.isTopSolid(blockpos2, this)) {
            }
        }

        if (blockpos != null) {
            if (this.world.isRemote) {
                if (this.clientSideTeleportInterpolation > 0 && this.currentAttachmentPosition != null) {
                    --this.clientSideTeleportInterpolation;
                } else {
                    this.currentAttachmentPosition = blockpos;
                }
            }

            this.forceSetPosition((double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D);
            double d0 = extendProgress / 20D * 3.5D;
            double d1 = d0;
            Direction direction3 = this.getAttachmentFacing().getOpposite();
            if (this.isAddedToWorld() && this.world instanceof net.minecraft.world.server.ServerWorld) ((net.minecraft.world.server.ServerWorld)this.world).chunkCheck(this); // Forge - Process chunk registration after moving.
            this.setBoundingBox((new AxisAlignedBB(this.getPosX() - 0.5D, this.getPosY(), this.getPosZ() - 0.5D, this.getPosX() + 0.5D, this.getPosY() + 1.0D, this.getPosZ() + 0.5D)).expand((double)direction3.getXOffset() * d0, (double)direction3.getYOffset() * d0, (double)direction3.getZOffset() * d0));
            double d2 = d0 - d1;
            if (d2 > 0.0D) {
                List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox());
                if (!list.isEmpty()) {
                    for(Entity entity : list) {
                        if (!(entity instanceof EntityBlockitWorm) && !entity.noClip) {
                            entity.move(MoverType.SHULKER, new Vector3d(d2 * (double)direction3.getXOffset(), d2 * (double)direction3.getYOffset(), d2 * (double)direction3.getZOffset()));

                        }
                    }
                }

            }
            for(BlockPos breakPos : BlockPos.getAllInBox(this.getPosition(), this.getPosition().add(d0 * direction3.getXOffset(), d0 * direction3.getYOffset(), d0 * direction3.getZOffset())).collect(Collectors.toList())){
                BlockState state = world.getBlockState(breakPos);
                if (state.getPushReaction() != PushReaction.IGNORE) {
                    if (WitherEntity.canDestroyBlock(state) && state.canEntityDestroy(world, breakPos, this)) {
                        world.destroyBlock(breakPos, true);
                    }
                }
            }
        }

        if(this.getAttackTarget() != null){
            if(!this.getAttackTarget().isPassenger() || !this.getAttackTarget().isAlive() || grabCooldown > 0){
                this.setAttackTarget(null);
            }
        }

    }

    private BlockPos getPosition() {
        return new BlockPos(this.getPositionVec());
    }

    private AxisAlignedBB getMaximumExtendTargets() {
        double d0 = 3.6D;
        Direction direction3 = this.getAttachmentFacing().getOpposite();
        return new AxisAlignedBB(this.getPosX() - 0.5D, this.getPosY(), this.getPosZ() - 0.5D, this.getPosX() + 0.5D, this.getPosY() + 1.0D, this.getPosZ() + 0.5D).expand((double)direction3.getXOffset() * d0, (double)direction3.getYOffset() * d0, (double)direction3.getZOffset() * d0);

    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(ATTACHED_FACE, Direction.byIndex(compound.getByte("AttachFace")));
        if (compound.contains("APX")) {
            int i = compound.getInt("APX");
            int j = compound.getInt("APY");
            int k = compound.getInt("APZ");
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(new BlockPos(i, j, k)));
        } else {
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.empty());
        }
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("AttachFace", (byte) this.dataManager.get(ATTACHED_FACE).getIndex());
        BlockPos blockpos = this.getAttachmentPos();
        if (blockpos != null) {
            compound.putInt("APX", blockpos.getX());
            compound.putInt("APY", blockpos.getY());
            compound.putInt("APZ", blockpos.getZ());
        }

    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.newPosRotationIncrements = 0;
    }

    private boolean isClosed() {
        return !this.dataManager.get(JUMP_OUT);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.isAlive() ? this.getBoundingBox() : null;
    }

    public Direction getAttachmentFacing() {
        return this.dataManager.get(ATTACHED_FACE);
    }

    public void setAttachmentFacing(Direction direction) {
        this.dataManager.set(ATTACHED_FACE, direction);
    }


    @Nullable
    public BlockPos getAttachmentPos() {
        return this.dataManager.get(ATTACHED_BLOCK_POS).orElse((BlockPos)null);
    }

    public void setAttachmentPos(@Nullable BlockPos pos) {
        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.ofNullable(pos));
    }


    @OnlyIn(Dist.CLIENT)
    public float getClientPeekAmount(float p_184688_1_) {
        return MathHelper.lerp(p_184688_1_, this.prevExtendProgress, this.extendProgress);
    }

    @OnlyIn(Dist.CLIENT)
    public int getClientTeleportInterp() {
        return this.clientSideTeleportInterpolation;
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getOldAttachPos() {
        return this.currentAttachmentPosition;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 0.5F;
    }

    public int getVerticalFaceSpeed() {
        return 180;
    }

    public int getHorizontalFaceSpeed() {
        return 180;
    }

    public void applyEntityCollision(Entity entityIn) {
        if(this.getAttackTarget() == null && entityIn instanceof LivingEntity && EntityPredicates.CAN_AI_TARGET.test(entityIn) && grabCooldown == 0){
            this.setAttackTarget((LivingEntity)entityIn);
        }
        if(this.getAttackTarget() != null && this.getAttackTarget().isEntityEqual(entityIn)){
            entityIn.startRiding(this);
            grabCooldown = 15;
        }
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        Direction direction3 = this.getAttachmentFacing().getOpposite();
        double d0 = extendProgress / 20D * 3.95D + 0.5D + passenger.getWidth();
        float height = 0;
        passenger.setPosition(this.getPosX() + (double)direction3.getXOffset() * d0, this.getPosY() + height + (double)direction3.getYOffset() * d0, this.getPosZ() + (double)direction3.getZOffset() * d0);
        if(passenger instanceof LivingEntity){
            passenger.attackEntityFrom(DamageSource.causeMobDamage(this), 4);
        }
    }

    public boolean shouldRiderSit() {
        return false;
    }

    public float getCollisionBorderSize() {
        return 0.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isAttachedToBlock() {
        return this.currentAttachmentPosition != null && this.getAttachmentPos() != null;
    }


    public void livingTick() {
        super.livingTick();
        this.setMotion(Vector3d.ZERO);
        this.prevRenderYawOffset = 180.0F;
        this.renderYawOffset = 180.0F;
        this.rotationYaw = 180.0F;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.renderYawOffset = 180.0F;
        this.prevRenderYawOffset = 180.0F;
        this.rotationYaw = 180.0F;
        this.prevRotationYaw = 180.0F;
        this.rotationYawHead = 180.0F;
        this.prevRotationYawHead = 180.0F;
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public BlockState getMeteoriteState() {
        return AstroBlockRegistry.BLOCKIT_WORM_HOLE.getDefaultState().with(BlockBlockitWormHole.FACING, this.dataManager.get(ATTACHED_FACE).getOpposite());
    }

    class BodyHelperController extends BodyController {
        public BodyHelperController(MobEntity p_i50612_2_) {
            super(p_i50612_2_);
        }

        /**
         * Update the Head and Body rendenring angles
         */
        public void updateRenderAngles() {
        }
    }
}
