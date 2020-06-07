package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.misc.AstroTagRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;
import java.util.*;

public class EntityStaron extends AbstractSpaceFish {

    private static final DataParameter<Boolean> NON_HOSTILE = EntityDataManager.createKey(EntityStaron.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ARMORED = EntityDataManager.createKey(EntityStaron.class, DataSerializers.BOOLEAN);
    private float speedModifier = 1F;

    protected EntityStaron(EntityType type, World world) {
        super(type, world);

    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    public float getPitchSensitivity() {
        return 3F;
    }


    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(0, new AITargetItems(this, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, false, false, (p_213619_0_) -> {
            return !EntityStaron.this.isNonHostile() && EntityStaron.isIronEntity(p_213619_0_);
        }));
    }

    public static boolean isIronEntity(LivingEntity entity) {
        Tag<Item> tag = ItemTags.getCollection().getOrCreate(AstroTagRegistry.STARON_AGGRO);
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (tag.contains(entity.getItemStackFromSlot(slot).getItem())) {
                return true;
            }
        }
        return entity instanceof IronGolemEntity;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(NON_HOSTILE, Boolean.valueOf(false));
        this.dataManager.register(ARMORED, Boolean.valueOf(false));
    }

    public boolean isNonHostile() {
        return this.dataManager.get(NON_HOSTILE).booleanValue();
    }

    public void setNonHostile(boolean saddled) {
        this.dataManager.set(NON_HOSTILE, Boolean.valueOf(saddled));
    }

    public boolean isArmored() {
        return this.dataManager.get(ARMORED).booleanValue();
    }

    public void setArmored(boolean armored) {
        this.dataManager.set(ARMORED, Boolean.valueOf(armored));
    }

    public void tick() {
        super.tick();
        speedModifier = (float) Math.sin(this.ticksExisted * 0.1F);
        this.setMotion(this.getMotion().mul(speedModifier, speedModifier, speedModifier));
        if (flightTarget == null || rand.nextFloat() < 0.05F) {
            BlockPos height = world.getHeight(Heightmap.Type.WORLD_SURFACE, this.getPosition());
            int upDistance = world.getMaxHeight() - height.getY();
            BlockPos targetPos = height.up(rand.nextInt(Math.max(1, upDistance))).add(rand.nextInt(16) - 8, 0, rand.nextInt(16) - 8);
            flightTarget = new Vec3d(targetPos);
        }
        if (this.getAttackTarget() != null && this.getAttackTarget().isAlive()) {
            this.flightTarget = this.getAttackTarget().getPositionVec();
        }
    }

    public static boolean canStaronSpawn(EntityType<? extends AnimalEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
        BlockState down = world.getBlockState(pos.down());
        return down.getBlock() == AstroBlockRegistry.METEORITE;
    }

    public boolean shouldEatItem(ItemStack item) {
        Tag<Item> tag = ItemTags.getCollection().getOrCreate(AstroTagRegistry.STARON_FOOD);
        return tag.contains(item.getItem());
    }

    public void onEatItem(ItemStack item){

    }

    private class AITargetItems<T extends ItemEntity> extends TargetGoal {
        protected final AITargetItems.Sorter theNearestAttackableTargetSorter;
        protected final Predicate<? super ItemEntity> targetEntitySelector;
        protected int executionChance;
        protected boolean mustUpdate;
        protected ItemEntity targetEntity;
        private EntityStaron staron;

        public AITargetItems(EntityStaron creature, boolean checkSight) {
            this(creature, checkSight, false);
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public AITargetItems(EntityStaron creature, boolean checkSight, boolean onlyNearby) {
            this(creature, 10, checkSight, onlyNearby, null);
        }

        public AITargetItems(EntityStaron creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
            super(creature, checkSight, onlyNearby);
            this.executionChance = chance;
            this.staron = creature;
            this.theNearestAttackableTargetSorter = new AITargetItems.Sorter(creature);
            this.targetEntitySelector = new Predicate<ItemEntity>() {
                @Override
                public boolean apply(@Nullable ItemEntity input) {
                    return staron.shouldEatItem(input.getItem());
                }
            };
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            if (staron.isArmored()) {
                return false;
            }
            if (!this.mustUpdate) {
                long worldTime = this.goalOwner.world.getGameTime() % 10;
                if (this.staron.getIdleTime() >= 100 && worldTime != 0) {
                    return false;
                }
                if (this.staron.getRNG().nextInt(this.executionChance) != 0 && worldTime != 0) {
                    return false;
                }
            }
            List<ItemEntity> list = this.goalOwner.world.getEntitiesWithinAABB(ItemEntity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
            if (list.isEmpty()) {
                return false;
            } else {
                Collections.sort(list, this.theNearestAttackableTargetSorter);
                this.targetEntity = list.get(0);
                this.mustUpdate = false;
                return true;
            }
        }

        protected double getTargetDistance() {
            return 16D;
        }


        protected AxisAlignedBB getTargetableArea(double targetDistance) {
            return this.goalOwner.getBoundingBox().grow(targetDistance, targetDistance, targetDistance);
        }

        @Override
        public void startExecuting() {
            this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ(), 1);
            this.staron.flightTarget = new Vec3d(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ());
            super.startExecuting();
        }

        @Override
        public void tick() {
            super.tick();
            if (this.targetEntity == null || this.targetEntity != null && !this.targetEntity.isAlive()) {
                this.resetTask();
                this.goalOwner.getNavigator().clearPath();
            }
            if (this.targetEntity != null && this.targetEntity.isAlive() && this.goalOwner.getDistanceSq(this.targetEntity) < 1.5F && staron.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
                EntityStaron staron = (EntityStaron) this.goalOwner;
                ItemStack duplicate = this.targetEntity.getItem().copy();
                duplicate.setCount(1);
                this.staron.playSound(SoundEvents.ENTITY_LLAMA_EAT, 1, 1);
                this.targetEntity.getItem().shrink(1);
                staron.onEatItem(duplicate);
                resetTask();
            }
        }

        public void makeUpdate() {
            this.mustUpdate = true;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !this.goalOwner.getNavigator().noPath();
        }

        class Sorter implements Comparator<Entity> {
            private final Entity theEntity;

            public Sorter(Entity theEntityIn) {
                this.theEntity = theEntityIn;
            }

            public int compare(Entity p_compare_1_, Entity p_compare_2_) {
                double d0 = this.theEntity.getDistanceSq(p_compare_1_);
                double d1 = this.theEntity.getDistanceSq(p_compare_2_);
                return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
            }
        }
    }
}