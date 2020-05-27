package com.github.alexthe666.astro.server.item;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.misc.AstroBannerRegistry;
import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroItemRegistry {

    public static CustomArmorMaterial GLASS_HELMET_MATERIAL = new CustomArmorMaterial("GlassHelmet", 15, new int[]{2, 0, 0, 0}, 10, SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, 0);
    public static final Item STARFISH = new Item(new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:starfish");
    public static final Item FALLING_STAR_RENDER = new Item(new Item.Properties()).setRegistryName("astro:falling_star_render");
    public static final Item FALLING_STAR = new ItemSpecialRender(new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:falling_star");
    public static final Item COSMOS_STAR_RENDER = new Item(new Item.Properties()).setRegistryName("astro:cosmos_star_render");
    public static final Item COSMOS_STAR = new ItemSpecialRender(new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:cosmos_star");
    public static final Item GLASS_HELMET = new ItemGlassHelmet();
    public static final Item SQUID_SPAWNER = new ItemSquidSpawner();
    public static final Item SPACE_SQUID_TENTACLE = new Item(new Item.Properties().group(Astronautical.TAB)).setRegistryName("astro:space_squid_tentacle");
    public static final Item SPACE_CALAMARI = new Item(new Item.Properties().group(Astronautical.TAB).food((new Food.Builder()).hunger(5).saturation(0.6F).meat().build())).setRegistryName("astro:space_calamari");

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : AstroItemRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item && ((Item) obj).getRegistryName() != null) {
                    event.getRegistry().register((Item) obj);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        event.getRegistry().register(new BannerPatternItem(AstroBannerRegistry.SPACE_SQUID_BANNER, (new Item.Properties()).maxStackSize(1).group(Astronautical.TAB)).setRegistryName("astro:space_squid_banner_pattern"));

    }

}
