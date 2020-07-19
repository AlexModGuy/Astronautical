package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.AstronauticalConfig;
import com.github.alexthe666.astro.server.world.AstroWorldData;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
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

public class SquidfallSpawner {
    private final Random random = new Random();
    private final ServerWorld world;
    private int timer;
    private int delay;
    private int chance;

    public SquidfallSpawner(ServerWorld p_i50177_1_) {
        this.world = p_i50177_1_;
        this.timer = 1200;
        AstroWorldData worldinfo = AstroWorldData.get(p_i50177_1_);
        this.delay = worldinfo.getSquidSpawnDelay();
        this.chance = worldinfo.getSquidSpawnChance();
        if (this.delay == 0 && this.chance == 0) {
            this.delay = 24000;
            worldinfo.setSquidSpawnDelay(this.delay);
            this.chance = AstronauticalConfig.squidfallChance;
            worldinfo.setSquidSpawnChance(this.chance);
        }

    }

    public void tick() {
        if (AstronauticalConfig.squidfallEnabled && --this.timer <= 0) {
            this.timer = 1200;
            AstroWorldData worldinfo = AstroWorldData.get(world);
            this.delay -= 1200;
            worldinfo.setSquidSpawnDelay(this.delay);
            if (this.delay <= 0) {
                this.delay = 24000;
                if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                    int i = this.chance;
                    this.chance = MathHelper.clamp(this.chance + AstronauticalConfig.squidfallChance, 5, 100);
                    worldinfo.setSquidSpawnChance(this.chance);
                    if (this.random.nextInt(100) <= i && this.func_221245_b() || true) {
                        this.chance = AstronauticalConfig.squidfallChance;
                    }
                }
            }
        }

    }

    private boolean func_221245_b() {
        PlayerEntity playerentity = this.world.getRandomPlayer();
        if (playerentity == null) {
            return true;
        } else if (this.random.nextInt(AstronauticalConfig.squidfallRandomRoll) != 0) {
            return false;
        } else {
            BlockPos blockpos = new BlockPos(playerentity.getPositionVec());
            PointOfInterestManager pointofinterestmanager = this.world.getPointOfInterestManager();
            Optional<BlockPos> optional = pointofinterestmanager.find(PointOfInterestType.HOME.getPredicate(), (p_221241_0_) -> {
                return true;
            }, blockpos, 48, Status.ANY);
            BlockPos blockpos1 = (BlockPos)optional.orElse(blockpos);
            BlockPos blockpos2 = this.func_221244_a(blockpos1, 48);
            if (blockpos2 != null && this.func_226559_a_(blockpos2)) {
                BlockPos upPos = new BlockPos(blockpos2.getX(), 300, blockpos2.getZ());
                EntitySpaceSquid spaceSquid = new EntitySpaceSquid(AstroEntityRegistry.SPACE_SQUID, world);
                spaceSquid.setLocationAndAngles(upPos.getX() + 0.5D, upPos.getY() + 0.5D, upPos.getZ() + 0.5D, random.nextFloat() * 360 - 180F, 0);
                spaceSquid.onInitialSpawn(world, world.getDifficultyForLocation(upPos), SpawnReason.MOB_SUMMONED, null, null);
                spaceSquid.setFallingFromSky(true);
                spaceSquid.fallingProgress = 20F;
                AstroWorldData worldinfo = AstroWorldData.get(world);

                worldinfo.setSquidID(spaceSquid.getUniqueID());
                spaceSquid.setHomePosAndDistance(blockpos1, 16);
                world.addEntity(spaceSquid);
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
