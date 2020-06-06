package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
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

    private static final EntityType registerEntity(EntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(Astronautical.MODID, entityName);
        return (EntityType) builder.build(entityName).setRegistryName(nameLoc);
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
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new SpawnEggItem(SPACE_SQUID, 0X88E1BB, 0X376951, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_space_squid"));
        event.getRegistry().register(new SpawnEggItem(STARCHOVY, 0X43BAB4, 0XB7FFFF, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_starchovy"));
        event.getRegistry().register(new SpawnEggItem(GLOPEPOD, 0X99ECEF, 0XC1FCFF, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_glopepod"));

    }

}
