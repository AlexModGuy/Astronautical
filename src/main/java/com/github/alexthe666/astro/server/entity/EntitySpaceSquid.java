package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.AstronauticalConfig;
import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.effect.AstroEffectRegistry;
import com.github.alexthe666.astro.server.item.AstroItemRegistry;
import com.github.alexthe666.astro.server.world.AstroWorldRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EntitySpaceSquid extends TameableEntity implements IAnimatedEntity {

    private static final DataParameter<Boolean> FALLING_FROM_SKY = EntityDataManager.createKey(EntitySpaceSquid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FALLEN = EntityDataManager.createKey(EntitySpaceSquid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DIRTY = EntityDataManager.createKey(EntitySpaceSquid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntitySpaceSquid.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> INTERESTED = EntityDataManager.createKey(EntitySpaceSquid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(EntitySpaceSquid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> SQUID_PITCH = EntityDataManager.createKey(EntitySpaceSquid.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> SPACE_BOUND = EntityDataManager.createKey(EntitySpaceSquid.class, DataSerializers.BOOLEAN);
    public float fallingProgress;
    public float injuredProgress;
    public int suffocateCounter = -1;
    @Nullable
    private Vec3d flightTarget;
    private BlockPos circlingPosition = null;
    public float prevSquidRotation;
    private float randomMotionSpeed;
    public float prevSquidPitch;
    public float tentacleAngle;
    public float lastTentacleAngle;
    public float squidRotation;
    private float rotationVelocity;
    private int slowDownTicks = 0;

    public EntitySpaceSquid(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new FlightMoveHelper(this);
        this.experienceValue = 10;
        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FALLING_FROM_SKY, Boolean.valueOf(false));
        this.dataManager.register(FALLEN, Boolean.valueOf(false));
        this.dataManager.register(DIRTY, Boolean.valueOf(true));
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
        this.dataManager.register(INTERESTED, Boolean.valueOf(false));
        this.dataManager.register(SADDLED, Boolean.valueOf(false));
        this.dataManager.register(SPACE_BOUND, Boolean.valueOf(false));
        this.dataManager.register(SQUID_PITCH, 0F);
    }

    public void tick(){
        super.tick();
        if(this.isSpaceBound() && this.getPosY() > 300){
        }
        float extraMotionSlow = 1.0F;
        float extraMotionSlowY = 1.0F;
        if(slowDownTicks > 0){
            slowDownTicks--;
            extraMotionSlow = 0.33F;
            extraMotionSlowY = 0.1F;
        }
        prevSquidPitch = this.getSquidPitch();
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;
        if ((double)this.squidRotation > (Math.PI * 2D)) {
            if (this.world.isRemote) {
                this.squidRotation = ((float)Math.PI * 2F);
            } else {
                this.squidRotation = (float)((double)this.squidRotation - (Math.PI * 2D));
                if (this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
                }
                this.world.setEntityState(this, (byte)19);
            }
        }
        if (isFallingFromSky() && fallingProgress < 20.0F) {
            fallingProgress += 5F;
        } else if (!isFallingFromSky() && fallingProgress > 0.0F) {
            fallingProgress -= 5F;
        }
        if (isFallen() && injuredProgress < 20.0F) {
            injuredProgress += 2F;
        } else if (!isFallen() && injuredProgress > 0.0F) {
            injuredProgress -= 2F;
        }
        if(this.isFallingFromSky()){
            this.fallDistance = 0.0F;
            float f = -MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F));
            float f2 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F));
            this.setMotion(this.getMotion().add(f * 0.01F, 0, f2 * 0.01F));
            for (BlockPos pos : BlockPos.getAllInBox(this.getPosition().add(-3, -3, -3), this.getPosition().add(3, 3, 3)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                if(BlockTags.LEAVES.contains(world.getBlockState(pos).getBlock())){
                    world.destroyBlock(pos, true);
                }
            }
        }else if(!this.isFallen()){
            if (this.squidRotation < (float)Math.PI) {
                float f = this.squidRotation / (float) Math.PI;
                this.tentacleAngle = MathHelper.sin(f * f * (float) Math.PI) * 4.275F;
                if ((double) f > 0.75D) {
                    this.randomMotionSpeed = 1.0F;
                } else {
                    randomMotionSpeed = 0.01F;
                }
            }
            if (!this.world.isRemote) {
                this.setMotion((double)(this.getMotion().x * this.randomMotionSpeed * extraMotionSlow), (double)(this.getMotion().y * this.randomMotionSpeed * extraMotionSlowY), (double)(this.getMotion().z * this.randomMotionSpeed * extraMotionSlow));
            }
            if (!this.onGround && this.getMotion().y < 0.0D) {
                this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
            }
            this.setMotion(this.getMotion().x, this.getMotion().y + 0.08D, this.getMotion().z);
            if(this.getAttackTarget() == null || this.flightTarget == null  || this.getDistanceSq(flightTarget.x, flightTarget.y, flightTarget.z) < 9 || !this.world.isAirBlock(new BlockPos(flightTarget))){
                if(circlingPosition == null){
                    if(world.getDimension().getType() == AstroWorldRegistry.COSMIC_SEA_TYPE){
                        BlockPos height = world.getHeight(Heightmap.Type.WORLD_SURFACE, this.getPosition());
                        int upDistance = world.getMaxHeight() - height.getY();
                        BlockPos targetPos = this.getPosition().add(rand.nextInt(16) - 8, MathHelper.clamp(rand.nextInt(15) - 8, 0,world.getMaxHeight()), rand.nextInt(16) - 8);
                        if (this.canBlockPosBeSeen(targetPos)) {
                            circlingPosition = targetPos;
                        }
                    }else{
                        circlingPosition = world.getHeight(Heightmap.Type.WORLD_SURFACE, this.getPosition()).up(20 + rand.nextInt(10));
                        if(isSpaceBound() && world.getDimension().getType() == DimensionType.OVERWORLD){
                            circlingPosition = new BlockPos(circlingPosition.getX(), 350, circlingPosition.getZ());
                        }
                    }
                }
                flightTarget = getBlockInViewCircling();
            }
            if(!onGround){
                double ydist = (this.prevPosY - this.getPosY()) * 1 / extraMotionSlowY;//down 0.4 up -0.38
                float squidDist = (float) ((Math.abs(this.getMotion().getX()) + Math.abs(this.getMotion().getZ())) * 6F);
                this.incrementSquidPitch((float) (ydist) * 10);

                this.setSquidPitch(MathHelper.clamp(this.getSquidPitch(), -60, 40));
                float plateau = 2;
                if (this.getSquidPitch() > plateau) {
                    //down
                    //this.motionY -= 0.2D;
                    this.decrementSquidPitch(squidDist * Math.abs(this.getSquidPitch()) / 90);
                }
                if (this.getSquidPitch() < -plateau) {//-2
                    //up
                    this.incrementSquidPitch(squidDist * Math.abs(this.getSquidPitch()) / 90);
                }
                if (this.getSquidPitch() > 2F) {
                    this.decrementSquidPitch(1);
                } else if (this.getSquidPitch() < -2F) {
                    this.incrementSquidPitch(1);
                }
            } else {
                this.setSquidPitch(0);
            }
        }
        this.setAir(200);
        BlockState downState = world.getBlockState(this.getPosition().down());
        if(this.isFallingFromSky() && onGround){
            this.attackEntityFrom(DamageSource.GENERIC, this.getMaxHealth() / 2F);
            this.playFallSound();
            this.setFallingFromSky(false);
            this.setFallen(true);
            this.setDirty(true);
            double d0 = 150;
            double i = this.getPosX();
            double j = this.getPosY();
            double k = this.getPosZ();
            this.suffocateCounter = 1;
            SquidExplosion explosion = new SquidExplosion(world, this, i, j, k, 4F, true, Explosion.Mode.DESTROY);
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
                player.addPotionEffect(new EffectInstance(AstroEffectRegistry.SQUIDFALL_EFFECT, 15, 0, true, false));
                player.sendStatusMessage(new TranslationTextComponent("message.astro.squidfall").applyTextStyle(TextFormatting.DARK_PURPLE), false);
            }
        }
        if(this.isFallen()){
            double d0 = 7;
            double i = this.getPosX();
            double j = this.getPosY();
            double k = this.getPosZ();
            boolean interested = false;
            for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
                if(player.getHeldItem(Hand.MAIN_HAND).getItem() == AstroItemRegistry.COSMOS_STAR || player.getHeldItem(Hand.OFF_HAND).getItem() == AstroItemRegistry.COSMOS_STAR){
                    interested = true;
                }
            }
            this.setInterested(interested);
            if(suffocateCounter++ > 72000){
                this.onKillCommand();
            }
        }
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

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 19) {
            this.squidRotation = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }

    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        Vec3d vec3d = new Vec3d(this.getPosX(), this.getPosYEye(), this.getPosZ());
        Vec3d vec3d1 = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() == RayTraceResult.Type.MISS;
    }

    private BlockPos getCirclingPosition() {
        return circlingPosition;
    }


    public boolean onLivingFall(float distance, float damageMultiplier) {
        return isFallingFromSky() ? false : super.onLivingFall(distance, damageMultiplier);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isPushedByWater() {
        return false;
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        float pitch = -this.getSquidPitch() * 1.9F / 90;
        float radius = (float) 0.35F - pitch;
        float angle = (0.01745329251F * this.renderYawOffset);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        double extraY = 0.75F - Math.max(getSquidPitch() , 0)  / 90;
        passenger.setPosition(this.getPosX() + extraX, this.getPosY() + extraY, this.getPosZ() + extraZ);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity) passenger).renderYawOffset = this.renderYawOffset;
            ((LivingEntity) passenger).rotationYaw = this.renderYawOffset;
            ((LivingEntity) passenger).rotationYawHead = this.renderYawOffset;
        }
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("FallingFromSky", isFallingFromSky());
        compound.putBoolean("Fallen", isFallen());
        compound.putBoolean("Dirty", isDirty());
        compound.putInt("ColorVariant", getColorVariant());
        compound.putInt("SuffocateCounter", suffocateCounter);
        compound.putInt("SlowDownTicks", slowDownTicks);
        compound.putBoolean("Interested", isInterested());
        compound.putBoolean("Saddled", isSaddled());
        compound.putBoolean("SpaceBound", isSpaceBound());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setFallingFromSky(compound.getBoolean("FallingFromSky"));
        this.setFallen(compound.getBoolean("Fallen"));
        this.setDirty(compound.getBoolean("Dirty"));
        this.setColorVariant(compound.getInt("ColorVariant"));
        this.setInterested(compound.getBoolean("Interested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.suffocateCounter = compound.getInt("SuffocateCounter");
        this.slowDownTicks = compound.getInt("SlowDownTicks");
        this.setSpaceBound(compound.getBoolean("SpaceBound"));
    }

    public float getSquidPitch() {
        return dataManager.get(SQUID_PITCH).floatValue();
    }

    public void setSquidPitch(float pitch) {
        dataManager.set(SQUID_PITCH, pitch);
    }

    public void incrementSquidPitch(float pitch) {
        dataManager.set(SQUID_PITCH, getSquidPitch() + pitch);
    }

    public void decrementSquidPitch(float pitch) {
        dataManager.set(SQUID_PITCH, getSquidPitch() - pitch);
    }


    public boolean isSaddled() {
        return this.dataManager.get(SADDLED).booleanValue();
    }

    public void setSaddled(boolean saddled) {
        this.dataManager.set(SADDLED, Boolean.valueOf(saddled));
    }

    public boolean isSpaceBound() {
        return this.dataManager.get(SPACE_BOUND).booleanValue();
    }

    public void setSpaceBound(boolean spaceBound) {
        this.dataManager.set(SPACE_BOUND, Boolean.valueOf(spaceBound));
    }

    public boolean isDirty() {
        return this.dataManager.get(DIRTY).booleanValue();
    }

    public void setDirty(boolean dirty) {
        this.dataManager.set(DIRTY, Boolean.valueOf(dirty));
    }

    public boolean isInterested() {
        return this.dataManager.get(INTERESTED).booleanValue();
    }

    public void setInterested(boolean interested) {
        this.dataManager.set(INTERESTED, Boolean.valueOf(interested));
    }

    public boolean isFallingFromSky() {
        return this.dataManager.get(FALLING_FROM_SKY).booleanValue();
    }

    public void setFallingFromSky(boolean fallen) {
        this.dataManager.set(FALLING_FROM_SKY, Boolean.valueOf(fallen));
    }

    public boolean isFallen() {
        return this.dataManager.get(FALLEN).booleanValue();
    }

    public void setFallen(boolean fallen) {
        this.dataManager.set(FALLEN, Boolean.valueOf(fallen));
    }

    public int getColorVariant() {
        return Integer.valueOf(this.dataManager.get(COLOR_VARIANT).intValue());
    }

    public void setColorVariant(int radius) {
        this.dataManager.set(COLOR_VARIANT, Integer.valueOf(radius));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        setColorVariant(rand.nextInt(4));
        setFallingFromSky(false);
        setFallen(false);
        setDirty(false);
        return spawnDataIn;
    }

    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if(!super.processInteract(player, hand)) {
            if (itemstack.getItem() == AstroItemRegistry.COSMOS_STAR && this.isFallen()) {
                this.setDirty(false);
                this.setFallen(false);
                this.setInterested(false);
                this.setSpaceBound(true);
                this.setFallingFromSky(false);
                slowDownTicks = 300;
                this.heal(this.getMaxHealth());
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return true;
            }
            if (itemstack.getItem() == Items.SADDLE && !this.isSaddled()) {
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.setSaddled(true);
                return true;
            }
            if(this.isSaddled() && !this.isBeingRidden()){
                player.startRiding(this);
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    @Override
    public int getAnimationTick() {
        return 0;
    }

    @Override
    public void setAnimationTick(int i) {

    }

    @Override
    public Animation getAnimation() {
        return null;
    }

    @Override
    public void setAnimation(Animation animation) {

    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }

    private static class FlightMoveHelper extends MovementController {

        private EntitySpaceSquid squid;

        protected FlightMoveHelper(EntitySpaceSquid squidBase) {
            super(squidBase);
            this.squid = squidBase;
        }

        public static float approach(float number, float max, float min) {
            min = Math.abs(min);
            return number < max ? MathHelper.clamp(number + min, number, max) : MathHelper.clamp(number - min, max, number);
        }

        public static float approachDegrees(float number, float max, float min) {
            float add = MathHelper.wrapDegrees(max - number);
            return approach(number, number + add, min);
        }

        public static float degreesDifferenceAbs(float f1, float f2) {
            return Math.abs(MathHelper.wrapDegrees(f2 - f1));
        }

        public void tick() {
            if (squid.collidedHorizontally) {
                squid.rotationYaw += 180.0F;
                this.speed = 0.1F;
                squid.flightTarget = null;
                return;
            }
            if(squid.flightTarget != null) {
                float distX = (float) (squid.flightTarget.x - squid.getPosX());
                float distY = (float) (squid.flightTarget.y - squid.getPosY());
                float distZ = (float) (squid.flightTarget.z - squid.getPosZ());
                double squidDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
                double yDistMod = 1.0D - (double) MathHelper.abs(distY * 0.7F) / squidDist;
                distX = (float) ((double) distX * yDistMod);
                distZ = (float) ((double) distZ * yDistMod);
                squidDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
                double dist = (double) MathHelper.sqrt(distX * distX + distZ * distZ + distY * distY);
                if (dist > 1.0F) {
                    float yawCopy = squid.rotationYaw;
                    float atan = (float) MathHelper.atan2((double) distZ, (double) distX);
                    float yawTurn = MathHelper.wrapDegrees(squid.rotationYaw + 90);
                    float yawTurnAtan = MathHelper.wrapDegrees(atan * 57.295776F);
                    squid.rotationYaw = approachDegrees(yawTurn, yawTurnAtan, 4.0F) - 90.0F;
                    squid.renderYawOffset = squid.rotationYaw;
                    if (degreesDifferenceAbs(yawCopy, squid.rotationYaw) < 3.0F) {
                        speed = approach((float) speed, 1.2F, 0.005F * (1.2F / (float) speed));
                    } else {
                        speed = approach((float) speed, 0.2F, 0.025F);
                        if (dist < 100D && squid.getAttackTarget() != null) {
                            speed = speed * (dist / 100D);
                        }
                    }
                    float finPitch = (float) (-(MathHelper.atan2((double) (-distY), squidDist) * 57.2957763671875D));
                    squid.rotationPitch = finPitch;
                    float yawTurnHead = squid.rotationYaw + 90.0F;
                    double lvt_16_1_ = speed * MathHelper.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
                    double lvt_18_1_ = speed * MathHelper.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
                    double lvt_20_1_ = speed * MathHelper.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
                    double yMotion = (double) distY / dist;
                    squid.setMotion(squid.getMotion().add(lvt_16_1_ * 0.4D, Math.min(0.1D, yMotion) * 0.2D, lvt_18_1_ * 0.4D));
                }
            }
        }
    }


    public static boolean canSpaceFishSpawn(EntityType<? extends AnimalEntity> p_223316_0_, IWorld p_223316_1_, SpawnReason p_223316_2_, BlockPos p_223316_3_, Random p_223316_4_) {
        return true;
    }
}
