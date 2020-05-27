package com.github.alexthe666.astro.server.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;

public class AstroWorldData extends WorldSavedData {

    private static final String IDENTIFIER = "astro_world_data";
    private World world;
    private int tickCounter;
    private int squidSpawnDelay;
    private int squidSpawnChance;
    private UUID squidID;
    private int starSpawnDelay;
    private int starSpawnChance;

    public AstroWorldData() {
        super(IDENTIFIER);
    }

    public static AstroWorldData get(World world) {
        if (world instanceof ServerWorld) {
            ServerWorld overworld = world.getServer().getWorld(DimensionType.OVERWORLD);

            DimensionSavedDataManager storage = overworld.getSavedData();
            AstroWorldData data = storage.getOrCreate(AstroWorldData::new, IDENTIFIER);
            if(data != null){
                data.world = world;
                data.markDirty();
            }
            return data;
        }
        return null;
    }

    public int getSquidSpawnDelay() {
        return this.squidSpawnDelay;
    }

    public void setSquidSpawnDelay(int delay) {
        this.squidSpawnDelay = delay;
    }

    public int getSquidSpawnChance() {
        return this.squidSpawnChance;
    }

    public void setSquidSpawnChance(int chance) {
        this.squidSpawnChance = chance;
    }

    public void setSquidID(UUID id) {
        this.squidID = id;
    }

    public int getStarSpawnDelay() {
        return this.starSpawnDelay;
    }

    public void setStarSpawnDelay(int delay) {
        this.starSpawnDelay = delay;
    }

    public int getStarSpawnChance() {
        return this.starSpawnChance;
    }

    public void setStarSpawnChance(int chance) {
        this.starSpawnChance = chance;
    }

    public void debug() {
    }


    public void tick() {
        ++this.tickCounter;
    }

    @Override
    public void read(CompoundNBT nbt) {
        if (nbt.contains("SquidSpawnDelay", 99)) {
            this.squidSpawnDelay = nbt.getInt("SquidSpawnDelay");
        }

        if (nbt.contains("SquidSpawnChance", 99)) {
            this.squidSpawnChance = nbt.getInt("SquidSpawnChance");
        }

        if (nbt.contains("SquidId", 8)) {
            this.squidID = UUID.fromString(nbt.getString("SquidId"));
        }

        if (nbt.contains("StarSpawnDelay", 99)) {
            this.starSpawnDelay = nbt.getInt("StarSpawnDelay");
        }

        if (nbt.contains("StarSpawnChance", 99)) {
            this.starSpawnChance = nbt.getInt("StarSpawnChance");
        }


    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("SquidSpawnDelay", this.squidSpawnDelay);
        compound.putInt("SquidSpawnChance", this.squidSpawnChance);
        if (this.squidID != null) {
            compound.putString("SquidId", this.squidID.toString());
        }
        compound.putInt("StarSpawnDelay", this.starSpawnDelay);
        compound.putInt("StarSpawnChance", this.starSpawnChance);

        return compound;
    }
}
