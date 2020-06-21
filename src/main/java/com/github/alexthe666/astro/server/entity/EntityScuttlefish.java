package com.github.alexthe666.astro.server.entity;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;
import java.util.*;

public class EntityScuttlefish extends AbstractSpaceFish {

    protected static final DataParameter<Boolean> HAS_ENOUGH_ITEMS_FOR_RECIPE = EntityDataManager.createKey(EntityScuttlefish.class, DataSerializers.BOOLEAN);
    private static List<IRecipe> EMPTY_LIST = new ArrayList<>();
    public int retargetItemsCooldown = 0;
    private int uncraftingProgress = 0;
    private IRecipe currentHeldRecipe = null;

    protected EntityScuttlefish(EntityType type, World world) {
        super(type, world);
    }

    private static final List<IRecipe> findMatchingRecipesFor(ItemStack stack, World world) {
        List<IRecipe> matchingRecipes = EMPTY_LIST;
        if (!stack.isEmpty()) {
            matchingRecipes = new ArrayList<>();
            RecipeManager manager = world.getRecipeManager();
            for (IRecipe<?> irecipe : manager.getRecipes()) {
                if (irecipe.getType() == IRecipeType.CRAFTING && irecipe.canFit(3, 3) && irecipe.getRecipeOutput().isItemEqual(stack)) {
                    matchingRecipes.add(irecipe);
                }
            }
        }
        return matchingRecipes;
    }

    public float getSwimSpeedModifier() {
        return 0.25F;
    }

    public float getPitchSensitivity() {
        return 5F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(0, new EntityScuttlefish.AITargetItems(this, false));
    }

    public boolean hasRecipe(){
        return currentHeldRecipe != null;
    }

    public void tick() {
        super.tick();
        if (flightTarget == null || rand.nextFloat() < 0.05F) {
            BlockPos height = world.getHeight(Heightmap.Type.WORLD_SURFACE, this.getPosition());
            int upDistance = world.getMaxHeight() - height.getY();
            BlockPos targetPos = this.getPosition().add(rand.nextInt(16) - 8, MathHelper.clamp(rand.nextInt(15) - 8, 0,world.getMaxHeight()), rand.nextInt(16) - 8);
            if (this.canBlockPosBeSeen(targetPos)) {
                flightTarget = new Vec3d(targetPos);
            }
        }
        if (this.getAttackTarget() != null && this.getAttackTarget().isAlive()) {
            this.flightTarget = this.getAttackTarget().getPositionVec();
        }
        if (!this.getHeldItemMainhand().isEmpty()) {
            if (currentHeldRecipe == null) {
                List<IRecipe> possibles = findMatchingRecipesFor(this.getHeldItemMainhand(), world);
                if (!possibles.isEmpty()) {
                    currentHeldRecipe = possibles.get(possibles.size() > 1 ? rand.nextInt(possibles.size() - 1) : 0);
                }
                this.setEnoughItemForRecipe(true);
            } else {
                this.setEnoughItemForRecipe(currentHeldRecipe.getRecipeOutput().getCount() <= this.getHeldItemMainhand().getCount());
                if(this.hasEnoughItemForRecipe()){
                    uncraftingProgress++;
                    if (uncraftingProgress >= 100) {
                        NonNullList<Ingredient> ingredients = currentHeldRecipe.getIngredients();
                        int recipeSize = ingredients.size();
                        if (!ingredients.isEmpty()) {
                            List<Ingredient> shuffledIngredients = new ArrayList<>(ingredients);
                            Collections.shuffle(shuffledIngredients, rand);
                            for (int i = 0; i < Math.min(recipeSize, 1 + rand.nextInt(3)); i++) {
                                Ingredient randomIngredient = shuffledIngredients.get(i);
                                ItemStack[] match = randomIngredient.getMatchingStacks();
                                if (match.length > 0) {
                                    ItemStack randomMatch = match[match.length > 1 ? rand.nextInt(match.length - 1) : 0].copy();
                                    this.entityDropItem(randomMatch);
                                }
                            }
                        }
                        this.uncraftingProgress = 0;
                        this.retargetItemsCooldown = 200;
                        this.currentHeldRecipe = null;
                        this.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                        this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1, 1);
                    }
                }
            }
        } else {
            currentHeldRecipe = null;
        }
        if (retargetItemsCooldown > 0) {
            retargetItemsCooldown--;
        }
    }

    public boolean shouldTargetItem(ItemStack item) {
        if(item.isDamaged() || item.isEnchanted()){
            return false;
        }
        if(!this.getHeldItemMainhand().isEmpty() && !this.hasEnoughItemForRecipe()){
            return item.isItemEqual(this.getHeldItemMainhand());
        }
        if (!item.isEmpty()) {
            List<IRecipe> list = findMatchingRecipesFor(item, this.world);
            return !list.isEmpty();
        }
        return false;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(HAS_ENOUGH_ITEMS_FOR_RECIPE, Boolean.valueOf(true));
    }

    public boolean hasEnoughItemForRecipe() {
        return this.dataManager.get(HAS_ENOUGH_ITEMS_FOR_RECIPE).booleanValue();
    }

    public void setEnoughItemForRecipe(boolean enough) {
        this.dataManager.set(HAS_ENOUGH_ITEMS_FOR_RECIPE, Boolean.valueOf(enough));
    }


    private class AITargetItems<T extends ItemEntity> extends TargetGoal {
        protected final EntityScuttlefish.AITargetItems.Sorter theNearestAttackableTargetSorter;
        protected final Predicate<? super ItemEntity> targetEntitySelector;
        protected int executionChance;
        protected boolean mustUpdate;
        protected ItemEntity targetEntity;
        private EntityScuttlefish scuttler;

        public AITargetItems(EntityScuttlefish creature, boolean checkSight) {
            this(creature, checkSight, false);
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public AITargetItems(EntityScuttlefish creature, boolean checkSight, boolean onlyNearby) {
            this(creature, 10, checkSight, onlyNearby, null);
        }

        public AITargetItems(EntityScuttlefish creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
            super(creature, checkSight, onlyNearby);
            this.executionChance = chance;
            this.scuttler = creature;
            this.theNearestAttackableTargetSorter = new EntityScuttlefish.AITargetItems.Sorter(creature);
            this.targetEntitySelector = new Predicate<ItemEntity>() {
                @Override
                public boolean apply(@Nullable ItemEntity input) {
                    return scuttler.shouldTargetItem(input.getItem());
                }
            };
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            if (!scuttler.getHeldItemMainhand().isEmpty() && scuttler.hasEnoughItemForRecipe() || scuttler.retargetItemsCooldown > 0) {
                return false;
            }
            if (!this.mustUpdate) {
                long worldTime = this.goalOwner.world.getGameTime() % 10;
                if (this.scuttler.getIdleTime() >= 100 && worldTime != 0) {
                    return false;
                }
                if (this.scuttler.getRNG().nextInt(this.executionChance) != 0 && worldTime != 0) {
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
            return 32D;
        }


        protected AxisAlignedBB getTargetableArea(double targetDistance) {
            return this.goalOwner.getBoundingBox().grow(targetDistance, targetDistance, targetDistance);
        }

        @Override
        public void startExecuting() {
            this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ(), 1);
            this.scuttler.flightTarget = new Vec3d(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ());
            super.startExecuting();
        }

        @Override
        public void tick() {
            super.tick();
            if (this.targetEntity == null || this.targetEntity != null && !this.targetEntity.isAlive() || !scuttler.getHeldItemMainhand().isEmpty() && scuttler.hasEnoughItemForRecipe()) {
                this.resetTask();
                this.goalOwner.getNavigator().clearPath();
            }
            if (this.targetEntity != null && this.targetEntity.isAlive() && this.goalOwner.getDistanceSq(this.targetEntity) < 1.5F && (scuttler.getHeldItem(Hand.MAIN_HAND).isEmpty() || !scuttler.hasEnoughItemForRecipe())) {
                EntityScuttlefish staron = (EntityScuttlefish) this.goalOwner;
                ItemStack duplicate = this.targetEntity.getItem().copy();
                duplicate.setCount(1);
                this.scuttler.playSound(SoundEvents.ENTITY_LLAMA_EAT, 1, 1);
                if(this.scuttler.getHeldItemMainhand().isEmpty()){
                    this.scuttler.setHeldItem(Hand.MAIN_HAND, duplicate);
                    this.targetEntity.getItem().shrink(1);
                }else if(this.scuttler.getHeldItemMainhand().isItemEqual(duplicate)){
                    this.scuttler.getHeldItemMainhand().grow(1);
                    this.targetEntity.getItem().shrink(1);
                }
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
