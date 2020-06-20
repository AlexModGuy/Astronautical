package com.github.alexthe666.astro.server.entity.tileentity;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroTileEntityRegistry {

    public static TileEntityType<TileEntityBlockitHole> BLOCKIT_HOLE = registerTileEntity(TileEntityType.Builder.create(TileEntityBlockitHole::new, AstroBlockRegistry.BLOCKIT_WORM_HOLE), "blockit_worm_hole");


    public static TileEntityType registerTileEntity(TileEntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(Astronautical.MODID, entityName);
        return (TileEntityType) builder.build(null).setRegistryName(nameLoc);
    }

    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
        try {
            for (Field f : AstroTileEntityRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof TileEntityType) {
                    event.getRegistry().register((TileEntityType) obj);
                } else if (obj instanceof TileEntityType[]) {
                    for (TileEntityType te : (TileEntityType[]) obj) {
                        event.getRegistry().register(te);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
