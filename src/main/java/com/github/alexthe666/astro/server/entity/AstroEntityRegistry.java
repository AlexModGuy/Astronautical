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

    public static final EntityType<EntitySpaceSquid> SPACE_SQUID = registerEntity(EntityType.Builder.create(EntitySpaceSquid::new, EntityClassification.CREATURE).size(2.4F, 1.5F), "space_squid");
    public static final EntityType<EntityFallingStar> FALLING_STAR = registerEntity(EntityType.Builder.create(EntityFallingStar::new, EntityClassification.MISC).size(0.9F, 0.9F).setTrackingRange(256).setCustomClientFactory(EntityFallingStar::new), "falling_star");

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
        event.getRegistry().register(new SpawnEggItem(SPACE_SQUID, 0X224D30, 0XFF9000, new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:spawn_egg_space_squid"));

    }

}
