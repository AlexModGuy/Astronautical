package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroEntityRegistry {

    public static final EntityType<EntitySpaceSquid> SPACE_SQUID = registerEntity(EntityType.Builder.create(EntitySpaceSquid::new, EntityClassification.CREATURE).size(2.4F, 1.5F).setTrackingRange(256), "space_squid");
    public static final EntityType<EntityFallingStar> FALLING_STAR = registerEntity(EntityType.Builder.create(EntityFallingStar::new, EntityClassification.MISC).size(0.9F, 0.9F).setTrackingRange(256).setCustomClientFactory(EntityFallingStar::new), "falling_star");
    public static final EntityType<EntityStarchovy> STARCHOVY = registerEntity(EntityType.Builder.create(EntityStarchovy::new, EntityClassification.CREATURE).size(0.65F, 0.65F).setTrackingRange(256), "starchovy");
    public static final EntityType<EntityGlopepod> GLOPEPOD = registerEntity(EntityType.Builder.create(EntityGlopepod::new, EntityClassification.CREATURE).size(0.35F, 0.35F).setTrackingRange(128), "glopepod");
    public static final EntityType<EntityStaron> STARON = registerEntity(EntityType.Builder.create(EntityStaron::new, EntityClassification.CREATURE).size(0.75F, 1.35F).setTrackingRange(256), "staron");
    public static final EntityType<EntityBlockitWorm> BLOCKIT_WORM = registerEntity(EntityType.Builder.create(EntityBlockitWorm::new, EntityClassification.CREATURE).size(1.0F, 1.0F), "blockit_worm");
    public static final EntityType<EntityScuttlefish> SCUTTLEFISH = registerEntity(EntityType.Builder.create(EntityScuttlefish::new, EntityClassification.CREATURE).size(0.8F, 0.5F), "scuttlefish");
    public static final EntityType<EntityStarphin> STARPHIN = registerEntity(EntityType.Builder.create(EntityStarphin::new, EntityClassification.CREATURE).size(0.9F, 0.9F), "starphin");

    static {
        EntitySpawnPlacementRegistry.register(SPACE_SQUID, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntitySpaceSquid::canSpaceFishSpawn);
        EntitySpawnPlacementRegistry.register(STARCHOVY, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractSpaceFish::canSpaceFishSpawn);
        EntitySpawnPlacementRegistry.register(GLOPEPOD, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractSpaceFish::canSpaceFishSpawn);
        EntitySpawnPlacementRegistry.register(STARON, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityStaron::canStaronSpawn);
        EntitySpawnPlacementRegistry.register(STARPHIN, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractSpaceFish::canSpaceFishSpawn);
    }

    private static final EntityType registerEntity(EntityType.Builder builder, String entityName) {
        ResourceLocation nameLoc = new ResourceLocation(Astronautical.MODID, entityName);
        return (EntityType) builder.build(entityName).setRegistryName(nameLoc);
    }

    public static void initializeAttributes() {
        GlobalEntityTypeAttributes.put(SPACE_SQUID, EntitySpaceSquid.buildAttributes().func_233813_a_());
        GlobalEntityTypeAttributes.put(STARCHOVY, EntityStarchovy.buildAttributes().func_233813_a_());
        GlobalEntityTypeAttributes.put(GLOPEPOD, EntityGlopepod.buildAttributes().func_233813_a_());
        GlobalEntityTypeAttributes.put(STARON, EntityStaron.buildAttributes().func_233813_a_());
        GlobalEntityTypeAttributes.put(BLOCKIT_WORM, EntityBlockitWorm.buildAttributes().func_233813_a_());
        GlobalEntityTypeAttributes.put(SCUTTLEFISH, EntityScuttlefish.buildAttributes().func_233813_a_());
        GlobalEntityTypeAttributes.put(STARPHIN, EntityScuttlefish.buildAttributes().func_233813_a_());
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        try {
            for (Field f : AstroEntityRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof EntityType) {
                    event.getRegistry().register((EntityType) obj);
                } else if (obj instanceof EntityType[]) {
                    for (EntityType type : (EntityType[]) obj) {
                        event.getRegistry().register(type);

                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        initializeAttributes();
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new SpawnEggItem(SPACE_SQUID, 0X88E1BB, 0X376951, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_space_squid"));
        event.getRegistry().register(new SpawnEggItem(STARCHOVY, 0X43BAB4, 0XB7FFFF, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_starchovy"));
        event.getRegistry().register(new SpawnEggItem(GLOPEPOD, 0X99ECEF, 0XC1FCFF, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_glopepod"));
        event.getRegistry().register(new SpawnEggItem(STARON, 0X4C2941, 0X8D566C, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_staron"));
        event.getRegistry().register(new SpawnEggItem(BLOCKIT_WORM, 0X4C4227, 0X5F6837, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_blockit_worm"));
        event.getRegistry().register(new SpawnEggItem(SCUTTLEFISH, 0XA1BA9C, 0XFF6A00, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_scuttlefish"));
        event.getRegistry().register(new SpawnEggItem(STARPHIN, 0X5A6CA6, 0XFFEE83, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_starphin"));

    }
}
