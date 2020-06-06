package com.github.alexthe666.astro.server.entity.ai;

import com.github.alexthe666.astro.server.entity.AbstractSchoolingSpaceFish;
import net.minecraft.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.Predicate;

public class FollowSpaceFishSchoolLeader  extends Goal {
    private final AbstractSchoolingSpaceFish taskOwner;
    private int navigateTimer;
    private int groupTimer;

    public FollowSpaceFishSchoolLeader(AbstractSchoolingSpaceFish fish) {
        this.taskOwner = fish;
        this.groupTimer = this.resetTimer(fish);
    }

    protected int resetTimer(AbstractSchoolingSpaceFish fish) {
        return 200 + fish.getRNG().nextInt(200) % 20;
    }

    public boolean shouldExecute() {
        if (this.taskOwner.isGroupLeader()) {
            return false;
        } else if (this.taskOwner.hasGroupLeader()) {
            return true;
        } else if (this.groupTimer > 0) {
            --this.groupTimer;
            return false;
        } else {
            this.groupTimer = this.resetTimer(this.taskOwner);
            Predicate<AbstractSchoolingSpaceFish> lvt_1_1_ = (fish) -> {
                return fish.canGroupGrow() || !fish.hasGroupLeader();
            };
            double dist = 100D;
            List<AbstractSchoolingSpaceFish> lvt_2_1_ = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().grow(dist, dist, dist), lvt_1_1_);
            AbstractSchoolingSpaceFish lvt_3_1_ = (AbstractSchoolingSpaceFish)lvt_2_1_.stream().filter(AbstractSchoolingSpaceFish::canGroupGrow).findAny().orElse(this.taskOwner);
            lvt_3_1_.createFromStream(lvt_2_1_.stream().filter((fish) -> {
                return !fish.hasGroupLeader();
            }));
            return this.taskOwner.hasGroupLeader();
        }
    }

    public boolean shouldContinueExecuting() {
        return this.taskOwner.hasGroupLeader() && this.taskOwner.inRangeOfGroupLeader();
    }

    public void startExecuting() {
        this.navigateTimer = 0;
    }

    public void resetTask() {
        this.taskOwner.leaveGroup();
    }

    public void tick() {
        if (--this.navigateTimer <= 0) {
            this.navigateTimer = 10;
            this.taskOwner.moveToGroupLeader();
        }
    }
}
