package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.server.item.AstroItemRegistry;
import com.github.alexthe666.astro.server.misc.AstroTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;

public class EntityStarphin extends AbstractSpaceFish implements IAnimatedEntity  {

    private float speedModifier = 1F;
    public static final Animation ANIMATION_SPIN = Animation.create(40);
    public static final Animation ANIMATION_ECHO = Animation.create(20);
    private int animationTick;
    private Animation currentAnimation;
    private int eatingTicks = 0;

    protected EntityStarphin(EntityType type, World world) {
        super(type, world);
    }

    public float getPitchSensitivity() {
        return 3F;
    }

    public static AttributeModifierMap.MutableAttribute buildAttributes() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 40.0D)            //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.4D)           //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 5.0D)            //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 64.0D);            //FOLLOW RANGE
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<EntityStarchovy>(this, EntityStarchovy.class, false, false));
    }

    public boolean shouldEatItem(ItemStack item) {
        ITag<Item> tag = ItemTags.getCollection().func_241834_b(AstroTagRegistry.STARPHIN_FOOD);
        return tag.func_230235_a_(item.getItem());
    }

    public float getSwimSpeedModifier(){
        return this.getAnimation() == ANIMATION_SPIN ? 1.5F : 0.4F;
    }

    public void tick() {
        super.tick();
        boolean flag = true;
        if(!this.getHeldItemMainhand().isEmpty() && this.shouldEatItem(this.getHeldItemMainhand())){
            flag = false;
            this.speedModifier = 0.6F;
            this.eatingTicks++;
        }
        if(eatingTicks >= 100 && shouldEatItem(this.getHeldItemMainhand())){
            this.getHeldItemMainhand().shrink(1);
        }
        if(flag && this.rand.nextInt(250 ) == 0 && this.getAnimation() == NO_ANIMATION){
            this.setAnimation(ANIMATION_SPIN);
        }
        if(this.getAnimation() == ANIMATION_ECHO){
            this.speedModifier = 0.03F;
        }

        if(this.getAnimation() == ANIMATION_SPIN){
            speedModifier = 1F;
            if(!this.world.isRemote){
                Vector3d speenVec;
                float multi = 2;
                if(this.getAnimationTick() <= 10){
                    Vector3d vector3d = this.getPositionVec().add(this.getLookVec().mul(multi, 1, multi));
                    speenVec = new Vector3d(vector3d.getX(), this.getPosY() + 2, vector3d.getZ());
                }else{
                    Vector3d vector3d = this.getPositionVec().add(this.getLookVec().mul(multi, 1, multi));
                    speenVec = new Vector3d(vector3d.getX(), this.getPosY() - 2, vector3d.getZ());
                }
                flightTarget = speenVec;
            }
        }
        this.setMotion(this.getMotion().mul(speedModifier, speedModifier, speedModifier));
        if (flightTarget == null || rand.nextFloat() < 0.05F) {
            BlockPos height = world.getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(this.getPositionVec()));
            int upDistance = 256 - height.getY();
            BlockPos targetPos =  new BlockPos(this.getPositionVec()).add(rand.nextInt(16) - 8, MathHelper.clamp(rand.nextInt(15) - 8, 0, 256), rand.nextInt(16) - 8);
            if (this.canBlockPosBeSeen(targetPos)) {
                flightTarget = new Vector3d(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);
            }
        }
        if (this.getAttackTarget() != null && this.getAttackTarget().isAlive()) {
            this.flightTarget = this.getAttackTarget().getPositionVec();
        }
        if(this.getAttackTarget() != null && this.getAnimation() != ANIMATION_ECHO){
            if(this.getDistanceSq(this.getAttackTarget()) < 2F){
                this.attackEntityAsMob(this.getAttackTarget());
                if(this.getAttackTarget() instanceof EntityStarchovy && this.getHeldItemMainhand().isEmpty()){
                    if (this.getAttackTarget().getHealth() <= 0.0F || rand.nextInt(2) == 0) {
                        this.getAttackTarget().remove();
                        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(AstroItemRegistry.STARCHOVY));
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_SPIN, ANIMATION_ECHO};
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        return spawnDataIn;
    }

}
