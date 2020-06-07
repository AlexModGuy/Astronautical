package com.github.alexthe666.astro.server.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityGlopepod extends AbstractSchoolingSpaceFish {

    protected EntityGlopepod(EntityType type, World world) {
        super(type, world);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
    }


    public int getMaxGroupSize() {
        return 8;
    }

    public float getSwimSpeedModifier(){
        return 0.2F;
    }
}
