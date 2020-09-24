package com.github.alexthe666.astro.server.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class EntityGlopepod extends AbstractSchoolingSpaceFish {

    protected EntityGlopepod(EntityType type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute buildAttributes() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 1.0D)            //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.15D)           //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 1.0D);            //ATTACK
    }

    public int getMaxGroupSize() {
        return 8;
    }

    public float getSwimSpeedModifier(){
        return 0.2F;
    }
}
