package com.github.alexthe666.astro.server.block;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroBlockRegistry {

    public static final Block SQUID_MUCUS = new BlockSquidMucus();
    public static final Block BLOCK_OF_SQUID_GOOP = new BlockOfSquidGoop();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : AstroBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    event.getRegistry().register((Block) obj);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        event.getRegistry().register(block);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : AstroBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    Item.Properties props = new Item.Properties();
                    props.group(Astronautical.TAB);
                    if (obj instanceof IUsesTEISR) {
                        Astronautical.PROXY.setupISTER(props);
                    }
                    BlockItem blockItem = new BlockItem((Block) obj, props);
                    blockItem.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(blockItem);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        Item.Properties props = new Item.Properties();
                        props.group(Astronautical.TAB);
                        if (block instanceof IUsesTEISR) {
                            Astronautical.PROXY.setupISTER(props);
                        }
                        BlockItem blockItem = new BlockItem(block, props);
                        blockItem.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(blockItem);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
