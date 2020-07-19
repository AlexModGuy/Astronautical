package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.AstronauticalConfig;
import com.github.alexthe666.astro.server.world.AstroWorldData;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestManager.Status;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

public class FallingStarSpawner {
    private final Random random = new Random();
    private final ServerWorld world;
    private int timer;
    private int delay;
    private int chance;

    public FallingStarSpawner(ServerWorld p_i50177_1_) {
        this.world = p_i50177_1_;
        this.timer = 500;
        AstroWorldData worldinfo = AstroWorldData.get(p_i50177_1_);
        this.timer = worldinfo.getStarSpawnDelay();
        this.chance = worldinfo.getStarSpawnChance();
        if (this.timer == 0 && this.chance == 0) {
            this.timer = 100;
            worldinfo.setStarSpawnDelay(this.timer);
            this.chance = AstronauticalConfig.fallingStarChance;
            worldinfo.setStarSpawnChance(this.chance);
        }

    }

    public void tick() {
        long roundedTime = this.world.getDayTime() % 24000;
        boolean night = roundedTime >= 13000 && roundedTime <= 22000;
        if (AstronauticalConfig.fallingStarEnabled && night && --this.timer <= 0) {
            this.timer = 590;
            AstroWorldData worldinfo = AstroWorldData.get(world);
            worldinfo.setStarSpawnDelay(this.timer);
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                this.chance = MathHelper.clamp(this.chance + AstronauticalConfig.fallingStarChance, 5, 100);
                worldinfo.setStarSpawnChance(this.chance);
                if (this.func_221245_b()) {
                    this.chance = AstronauticalConfig.fallingStarChance;
                }
            }
        }

    }

    private boolean func_221245_b() {
        PlayerEntity playerentity = this.world.getRandomPlayer();
        if (playerentity == null) {
            return true;
        } else if (this.random.nextInt(AstronauticalConfig.fallingStarRandomRoll) != 0) {
            return false;
        } else {
            BlockPos blockpos2 = new BlockPos(playerentity.getPositionVec());
            if (blockpos2 != null) {
                BlockPos upPos = new BlockPos(blockpos2.getX(), 300, blockpos2.getZ());
                EntityFallingStar spaceStar = new EntityFallingStar(AstroEntityRegistry.FALLING_STAR, world);
                spaceStar.setLocationAndAngles(upPos.getX() + 0.5D, upPos.getY() + 0.5D, upPos.getZ() + 0.5D, random.nextFloat() * 360 - 180F, 0);
                world.addEntity(spaceStar);
            }

            return false;
        }
    }


    @Nullable
    private BlockPos func_221244_a(BlockPos p_221244_1_, int p_221244_2_) {
        BlockPos blockpos = null;

        for(int i = 0; i < 10; ++i) {
            int j = p_221244_1_.getX() + this.random.nextInt(p_221244_2_ * 2) - p_221244_2_;
            int k = p_221244_1_.getZ() + this.random.nextInt(p_221244_2_ * 2) - p_221244_2_;
            int l = this.world.getHeight(Type.WORLD_SURFACE, j, k);
            BlockPos blockpos1 = new BlockPos(j, l, k);
            if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(PlacementType.ON_GROUND, this.world, blockpos1, EntityType.WANDERING_TRADER)) {
                blockpos = blockpos1;
                break;
            }
        }

        return blockpos;
    }

    private boolean func_226559_a_(BlockPos p_226559_1_) {
        Iterator var2 = BlockPos.getAllInBoxMutable(p_226559_1_, p_226559_1_.add(1, 2, 1)).iterator();

        BlockPos blockpos;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            blockpos = (BlockPos)var2.next();
        } while(this.world.getBlockState(blockpos).getCollisionShape(this.world, blockpos).isEmpty());

        return false;
    }
}
