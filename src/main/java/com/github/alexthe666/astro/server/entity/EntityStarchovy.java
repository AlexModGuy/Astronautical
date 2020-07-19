package com.github.alexthe666.astro.server.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class EntityStarchovy extends AbstractSchoolingSpaceFish {

    protected EntityStarchovy(EntityType type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute buildAttributes() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 5.0D)            //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.25D)           //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 1.0D);            //ATTACK
    }

    public int getMaxGroupSize() {
        return 32;
    }

}
