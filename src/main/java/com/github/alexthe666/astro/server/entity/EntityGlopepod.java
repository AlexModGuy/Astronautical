package com.github.alexthe666.astro.server.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityGlopepod extends AbstractSchoolingSpaceFish {

    protected EntityGlopepod(EntityType type, World world) {
        super(type, world);
    }

    public int getMaxGroupSize() {
        return 8;
    }

    public float getSwimSpeedModifier(){
        return 0.2F;
    }
}
