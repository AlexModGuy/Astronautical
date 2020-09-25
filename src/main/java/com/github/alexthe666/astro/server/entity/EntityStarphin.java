package com.github.alexthe666.astro.server.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class EntityStarphin extends AbstractSpaceFish {

    private float speedModifier = 1F;

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
                .func_233815_a_(Attributes.field_233823_f_, 64.0D);            //FOLLOW RANGE
    }


    public void tick() {
        super.tick();
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
    }

}
