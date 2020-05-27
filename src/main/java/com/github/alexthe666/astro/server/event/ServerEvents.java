package com.github.alexthe666.astro.server.event;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.AstronauticalConfig;
import com.github.alexthe666.astro.server.entity.FallingStarSpawner;
import com.github.alexthe666.astro.server.entity.SquidfallSpawner;
import com.github.alexthe666.astro.server.item.AstroItemRegistry;
import com.github.alexthe666.astro.server.world.AstroWorldRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Astronautical.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void registerDimensionTypes(RegisterDimensionsEvent event) {
        AstroWorldRegistry.COSMIC_SEA_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation("astro:cosmic_sea"), AstroWorldRegistry.COSMIC_SEA_MOD_DIMENSION, null, true);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof ServerPlayerEntity && event.getEntityLiving().getPosY() > 300){
            MinecraftServer server = event.getEntityLiving().world.getServer();
            ServerPlayerEntity thePlayer = (ServerPlayerEntity) event.getEntityLiving();
            if (thePlayer.timeUntilPortal > 0) {
                thePlayer.timeUntilPortal = 10;
            }
            else if (thePlayer.dimension != AstroWorldRegistry.COSMIC_SEA_TYPE) {
                thePlayer.timeUntilPortal = 10;
                ServerWorld dimWorld = server.getWorld(AstroWorldRegistry.COSMIC_SEA_TYPE);
                if(dimWorld != null){

                    teleportEntity(thePlayer, dimWorld, new BlockPos(event.getEntityLiving().getPosX(), 5, event.getEntityLiving().getPosZ()));
                }
            } else {
                thePlayer.timeUntilPortal = 10;
                ServerWorld dimWorld = server.getWorld(DimensionType.getById(AstronauticalConfig.exitDimensionID));
                if(dimWorld != null){
                    teleportEntity(thePlayer, dimWorld,  new BlockPos(event.getEntityLiving().getPosX(), 5, event.getEntityLiving().getPosZ()));
                }
            }
        }
    }

    private static Entity teleportEntity(Entity entity, ServerWorld endpointWorld, BlockPos endpoint) {
        if(entity.dimension == AstroWorldRegistry.COSMIC_SEA_TYPE){
        }else{
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getBedLocation() != null) {
                BlockPos bedPos = ((PlayerEntity) entity).getBedLocation();
                endpoint = bedPos;
                entity.setLocationAndAngles(bedPos.getX() + 0.5D, bedPos.getY() + 1.5D, bedPos.getZ() + 0.5D, 0.0F, 0.0F);
            } else {
                BlockPos height = entity.world.getHeight(Heightmap.Type.WORLD_SURFACE, entity.getPosition());
                endpoint = height;
                entity.setLocationAndAngles(height.getX() + 0.5D, height.getY() + 0.5D, height.getZ() + 0.5D, entity.rotationYaw, 0.0F);
            }
        }
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            player.teleport(endpointWorld, endpoint.getX() + 0.5D, endpoint.getY() + 0.5D, endpoint.getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
            return player;
        }

        entity.detach();
        entity.dimension = endpointWorld.dimension.getType();

        Entity teleportedEntity = entity.getType().create(endpointWorld);
        if (teleportedEntity == null) {
            return entity;
        }
        teleportedEntity.copyDataFromOld(entity);
        teleportedEntity.setLocationAndAngles(endpoint.getX() + 0.5D, endpoint.getY() + 0.5D, endpoint.getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
        teleportedEntity.setRotationYawHead(entity.rotationYaw);
        endpointWorld.func_217460_e(teleportedEntity);
        return teleportedEntity;
    }

    @SubscribeEvent
    public static void onLivingSpawnEvent(LivingSpawnEvent.SpecialSpawn event) {
        if(event.getEntityLiving() instanceof DrownedEntity && event.getEntityLiving().getHeldItemMainhand().isEmpty() && event.getWorld().getRandom().nextInt(50) == 0){
            event.getEntityLiving().setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AstroItemRegistry.STARFISH));
        }
    }

    @SubscribeEvent
    public static void onLootTableGenerated(LootTableLoadEvent event) {
        if (event.getName().equals(LootTables.CHESTS_UNDERWATER_RUIN_BIG) || event.getName().equals(LootTables.CHESTS_UNDERWATER_RUIN_SMALL)
                || event.getName().equals(LootTables.CHESTS_SHIPWRECK_TREASURE)){
            LootEntry.Builder item = ItemLootEntry.builder(AstroItemRegistry.STARFISH).quality(20).weight(1); //new ItemLootEntry(RatsItemRegistry.CONTAMINATED_FOOD, 20, 1, new ILootCondition[0], new ILootFunction[0], "rats:contaminated_food");
            LootPool.Builder builder = new LootPool.Builder().addEntry(item).acceptCondition(RandomChance.builder(0.05f)).rolls(new RandomValueRange(1, 1)).bonusRolls(0, 1);
            event.getTable().addPool(builder.build());
        }
        if (event.getName().equals(LootTables.CHESTS_BURIED_TREASURE)){
            LootEntry.Builder item = ItemLootEntry.builder(AstroItemRegistry.STARFISH).quality(20).weight(100); //new ItemLootEntry(RatsItemRegistry.CONTAMINATED_FOOD, 20, 1, new ILootCondition[0], new ILootFunction[0], "rats:contaminated_food");
            LootPool.Builder builder = new LootPool.Builder().addEntry(item).rolls(new RandomValueRange(1, 1)).bonusRolls(0, 0);
            event.getTable().addPool(builder.build());
        }
    }

    private static final Map<ServerWorld, SquidfallSpawner> SQUIDFALL_SPAWNER_MAP = new HashMap<ServerWorld, SquidfallSpawner>();
    private static final Map<ServerWorld, FallingStarSpawner> STAR_SPAWNER_MAP = new HashMap<ServerWorld, FallingStarSpawner>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isRemote && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            if(SQUIDFALL_SPAWNER_MAP.get(serverWorld) == null){
                SQUIDFALL_SPAWNER_MAP.put(serverWorld, new SquidfallSpawner(serverWorld));
            }
            SquidfallSpawner spawner = SQUIDFALL_SPAWNER_MAP.get(serverWorld);
            spawner.tick();
        }
        if(!tick.world.isRemote && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            if(STAR_SPAWNER_MAP.get(serverWorld) == null){
                STAR_SPAWNER_MAP.put(serverWorld, new FallingStarSpawner(serverWorld));
            }
            FallingStarSpawner spawner = STAR_SPAWNER_MAP.get(serverWorld);
            spawner.tick();
        }
    }

}
