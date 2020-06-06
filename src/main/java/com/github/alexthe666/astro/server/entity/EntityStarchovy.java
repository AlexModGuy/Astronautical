package com.github.alexthe666.astro.server.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
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

    public int getMaxGroupSize() {
        return 32;
    }

}
