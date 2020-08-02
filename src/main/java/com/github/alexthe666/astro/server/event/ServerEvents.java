package com.github.alexthe666.astro.server.event;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.AstronauticalConfig;
import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.block.ModWallTorchBlock;
import com.github.alexthe666.astro.server.entity.FallingStarSpawner;
import com.github.alexthe666.astro.server.entity.SquidfallSpawner;
import com.github.alexthe666.astro.server.item.AstroItemRegistry;
import com.github.alexthe666.astro.server.world.AstroWorldRegistry;
import net.minecraft.advancements.criterion.PlacedBlockTrigger;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Astronautical.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onRightClickWithBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")) {
            if(event.getUseItem() == Event.Result.DEFAULT){
                if(event.getItemStack().getItem() == Items.FIRE_CHARGE || event.getItemStack().getItem() == Items.FLINT_AND_STEEL){
                    event.getPlayer().swingArm(event.getHand());
                    event.setCanceled(true);
                }
                if(event.getItemStack().getItem() instanceof BlockItem){
                    Block block = ((BlockItem)event.getItemStack().getItem()).getBlock();
                    BlockState prevState = event.getWorld().getBlockState(event.getPos().offset(event.getFace()));
                    boolean flag = false;
                    if(block == Blocks.CAMPFIRE || block == Blocks.field_235367_mf_){
                        BlockState state = block.getDefaultState().with(CampfireBlock.LIT, false);
                        event.getWorld().setBlockState(event.getPos().offset(event.getFace()), state);
                        event.setUseItem(Event.Result.ALLOW);
                        flag = true;
                    }
                    if(event.getItemStack().getItem() == Items.TORCH || event.getItemStack().getItem() == Items.field_234737_dp_){
                        if(Block.hasEnoughSolidSide(event.getWorld(), event.getPos(), event.getFace()) && event.getFace() != Direction.DOWN && prevState.isAir()){
                            BlockState groundState = AstroBlockRegistry.BURNT_TORCH.getDefaultState();
                            if(event.getFace().getAxis() == Direction.Axis.Y){
                                event.getWorld().setBlockState(event.getPos().offset(event.getFace()), groundState);
                            }else{
                                BlockState wallState = AstroBlockRegistry.WALL_BURNT_TORCH.getDefaultState().with(ModWallTorchBlock.HORIZONTAL_FACING, event.getFace());
                                event.getWorld().setBlockState(event.getPos().offset(event.getFace()), wallState);

                            }
                            event.setUseItem(Event.Result.ALLOW);
                            flag = true;
                        }else {
                            event.setCanceled(true);
                        }
                    }

                    if(flag){
                        event.getPlayer().swingArm(event.getHand());
                        if(!event.getPlayer().isCreative()){
                            event.getItemStack().shrink(1);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){
        if(!(event.getEntity() instanceof LivingEntity)){
            if(event.getEntity().getEntityWorld().func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")){
                event.getEntity().setNoGravity(true);
            }
        }
    }
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        boolean cosmicDimension = event.getEntity().getEntityWorld().func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea");
        boolean overworld = event.getEntity().getEntityWorld().func_234923_W_().func_240901_a_().getPath().equals("overworld");
        if (cosmicDimension) {
            LivingEntity entity = event.getEntityLiving();
            entity.setSwimming(true);
            boolean flying = false;
            boolean creative = false;
            if(entity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity)entity;
                flying = player.abilities.isFlying;
                creative = player.isCreative();
            }
            if(entity.isSprinting() && !entity.func_233570_aj_() && !flying){
                entity.setPose(Pose.SWIMMING);
            }

            boolean swimming = entity.getPose() == Pose.SWIMMING;
            Vector3d Vector3d = entity.getMotion();
            entity.fallDistance = 1;
            if (!entity.func_233570_aj_() && Vector3d.y < 0.0D) {
                entity.setMotion(Vector3d.mul(1.0D, 0.6D, 1.0D));
            }
            double upAlready = 0;
            Vector3d Vector3d1 = entity.getMotion();
            if(!entity.func_233570_aj_() && swimming){
                double d3 = entity.getLookVec().y;
                double d4 = d3 < -0.2D ? 0.1D : 0.09D;
                upAlready = (d3 - Vector3d1.y) * d4;
                entity.setMotion(Vector3d1.add(0.0D, (d3 - Vector3d1.y) * d4, 0.0D));
            }
            if(entity.isSneaking()){

                entity.setMotion(Vector3d1.add(0.0D, creative ? -0.18D : -0.08D, 0.0D));

            }
            if(entity.isJumping){
                entity.setMotion(entity.getMotion().add(0.0D, Math.max(upAlready, 0.08D), 0.0D));
            }
            if(entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() != AstroItemRegistry.GLASS_HELMET){
                int airLeft = entity.getAir();
                entity.setAir(airLeft - 5);
                if(airLeft <= 0){
                    if(entity.ticksExisted % 10 == 0){
                        entity.attackEntityFrom(DamageSource.DROWN, 2);
                    }
                }
            }else{
                entity.setAir(Math.min(entity.getMaxAir(), entity.getAir() + 2));
            }

        }
        if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof ServerPlayerEntity && event.getEntityLiving().getPosY() > 300 && overworld){
            MinecraftServer server = event.getEntityLiving().world.getServer();
            ServerPlayerEntity thePlayer = (ServerPlayerEntity) event.getEntityLiving();
            if (thePlayer.timeUntilPortal > 0) {
                thePlayer.timeUntilPortal = 10;
            }
            if (!cosmicDimension) {
                thePlayer.timeUntilPortal = 10;
                ServerWorld dimWorld = server.getWorld(getCosmicDimension());
                if(dimWorld != null){

                    teleportEntity(thePlayer, dimWorld, new BlockPos(event.getEntityLiving().getPosX(), 5, event.getEntityLiving().getPosZ()));
                }
            }
        }
        if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof ServerPlayerEntity && cosmicDimension && event.getEntityLiving().getPosY() < -50){
            MinecraftServer server = event.getEntityLiving().world.getServer();
            ServerPlayerEntity thePlayer = (ServerPlayerEntity) event.getEntityLiving();
            if (thePlayer.timeUntilPortal > 0) {
                thePlayer.timeUntilPortal = 10;
            }
            else {
                thePlayer.timeUntilPortal = 10;
                ServerWorld dimWorld = server.getWorld(World.field_234918_g_);
                if (dimWorld != null) {
                    teleportEntity(thePlayer, dimWorld, new BlockPos(event.getEntityLiving().getPosX(), 295, event.getEntityLiving().getPosZ()));
                }
            }
        }
    }

    public static RegistryKey<World> getCosmicDimension(){
        ResourceLocation resourcelocation = new ResourceLocation("astro:cosmic_sea");
        RegistryKey<World> registrykey = RegistryKey.func_240903_a_(Registry.field_239699_ae_, resourcelocation);
        return registrykey;
    }


    private static Entity teleportEntity(Entity entity, ServerWorld endpointWorld, BlockPos endpoint) {
        if(entity.getEntityWorld().func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")){
        }else{
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getBedPosition().isPresent()) {
                BlockPos bedPos = ((PlayerEntity) entity).getBedPosition().get();
                endpoint = bedPos;
                entity.setLocationAndAngles(bedPos.getX() + 0.5D, bedPos.getY() + 1.5D, bedPos.getZ() + 0.5D, 0.0F, 0.0F);
            } else {
                BlockPos height = entity.world.getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(entity.getPositionVec()));
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
        entity.func_241206_a_(endpointWorld);
        Entity teleportedEntity = entity.getType().create(endpointWorld);
        if (teleportedEntity == null) {
            return entity;
        }
        teleportedEntity.copyDataFromOld(entity);
        teleportedEntity.setLocationAndAngles(endpoint.getX() + 0.5D, endpoint.getY() + 0.5D, endpoint.getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
        teleportedEntity.setRotationYawHead(entity.rotationYaw);
        endpointWorld.addFromAnotherDimension(teleportedEntity);
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
