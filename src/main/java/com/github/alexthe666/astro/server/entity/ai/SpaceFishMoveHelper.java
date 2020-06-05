package com.github.alexthe666.astro.server.entity.ai;

import com.github.alexthe666.astro.server.entity.AbstractSpaceFish;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;

public class SpaceFishMoveHelper extends MovementController {
    private AbstractSpaceFish fish;

    public SpaceFishMoveHelper(AbstractSpaceFish squidBase) {
        super(squidBase);
        this.fish = squidBase;
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
        if (fish.collidedHorizontally) {
            fish.rotationYaw += 180.0F;
            this.speed = 0.1F;
            fish.flightTarget = null;
            return;
        }
        if(fish.flightTarget != null) {
            float distX = (float) (fish.flightTarget.x - fish.getPosX());
            float distY = (float) (fish.flightTarget.y - fish.getPosY());
            float distZ = (float) (fish.flightTarget.z - fish.getPosZ());
            double squidDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
            double yDistMod = 1.0D - (double) MathHelper.abs(distY * 0.7F) / squidDist;
            distX = (float) ((double) distX * yDistMod);
            distZ = (float) ((double) distZ * yDistMod);
            squidDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
            double dist = (double) MathHelper.sqrt(distX * distX + distZ * distZ + distY * distY);
            if (dist > 1.0F) {
                float yawCopy = fish.rotationYaw;
                float atan = (float) MathHelper.atan2((double) distZ, (double) distX);
                float yawTurn = MathHelper.wrapDegrees(fish.rotationYaw + 90);
                float yawTurnAtan = MathHelper.wrapDegrees(atan * 57.295776F);
                fish.rotationYaw = approachDegrees(yawTurn, yawTurnAtan, 4.0F) - 90.0F;
                fish.renderYawOffset = fish.rotationYaw;
                if (degreesDifferenceAbs(yawCopy, fish.rotationYaw) < 3.0F) {
                    speed = approach((float) speed, 1.2F, 0.005F * (1.2F / (float) speed));
                } else {
                    speed = approach((float) speed, 0.2F, 0.025F);
                    if (dist < 100D && fish.getAttackTarget() != null) {
                        speed = speed * (dist / 100D);
                    }
                }
                float finPitch = (float) (-(MathHelper.atan2((double) (-distY), squidDist) * 57.2957763671875D));
                fish.rotationPitch = finPitch;
                float yawTurnHead = fish.rotationYaw + 90.0F;
                double lvt_16_1_ = speed * MathHelper.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
                double lvt_18_1_ = speed * MathHelper.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
                double lvt_20_1_ = speed * MathHelper.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
                double yMotion = (double) distY / dist;
                float moveSpeedCap = 0.05F;
                double cappedX = MathHelper.clamp(lvt_16_1_, -moveSpeedCap, moveSpeedCap);
                double cappedY = MathHelper.clamp(Math.min(0.1D, yMotion) * 0.2D, -moveSpeedCap, moveSpeedCap);
                double cappedZ = MathHelper.clamp(lvt_18_1_, -moveSpeedCap, moveSpeedCap);
                fish.setMotion(fish.getMotion().add(cappedX * 0.4D, Math.min(0.1D, yMotion) * 0.2D, cappedZ * 0.4D));
            }
        }
    }
}