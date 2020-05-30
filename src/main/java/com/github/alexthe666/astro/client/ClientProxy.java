package com.github.alexthe666.astro.client;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.client.event.ClientEvents;
import com.github.alexthe666.astro.client.model.TabulaModels;
import com.github.alexthe666.astro.client.render.AstroISTER;
import com.github.alexthe666.astro.client.render.CosmicSkyRenderer;
import com.github.alexthe666.astro.client.render.entity.RenderFallingStar;
import com.github.alexthe666.astro.client.render.entity.RenderSpaceSquid;
import com.github.alexthe666.astro.server.CommonProxy;
import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.block.BlockPlanetoidGas;
import com.github.alexthe666.astro.server.block.BlockPlanetoidRing;
import com.github.alexthe666.astro.server.entity.AstroEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Astronautical.MODID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public static List<UUID> currentSquidRiders = new ArrayList<UUID>();
    private CosmicSkyRenderer skyRenderer;

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> getTEISR() {
        return AstroISTER::new;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onBlockColors(ColorHandlerEvent.Block event) {
        Astronautical.LOGGER.info("loaded in block colorizer");
        event.getBlockColors().register((state, worldIn, pos, colorIn) -> {
            int color = 0;
            Block block = worldIn.getBlockState(pos).getBlock();
            if (block instanceof BlockPlanetoidGas) {
                color = ((BlockPlanetoidGas) block).colorBase;
            }
            double y = Math.sin(Math.PI * ((pos.getY() % 4) / 4F)) + 1.0F;
            int yMod = (int)Math.round((20D * y));
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255 ;
            r -= yMod;
            g -= yMod;
            b -= yMod;
            int col = (0xff << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
            return col;
        }, AstroBlockRegistry.PLANETOID_GAS_BLUE, AstroBlockRegistry.PLANETOID_GAS_GREEN, AstroBlockRegistry.PLANETOID_GAS_ORANGE, AstroBlockRegistry.PLANETOID_GAS_PURPLE, AstroBlockRegistry.PLANETOID_GAS_TEAL, AstroBlockRegistry.PLANETOID_GAS_YELLOW);
        event.getBlockColors().register((state, worldIn, pos, colorIn) -> {
            int color = 0;
            Block block = worldIn.getBlockState(pos).getBlock();
            if (block instanceof BlockPlanetoidRing) {
                color = ((BlockPlanetoidRing) block).colorBase;
            }
            return color;
        }, AstroBlockRegistry.PLANETOID_RING_BLUE, AstroBlockRegistry.PLANETOID_RING_GREEN, AstroBlockRegistry.PLANETOID_RING_ORANGE, AstroBlockRegistry.PLANETOID_RING_PURPLE, AstroBlockRegistry.PLANETOID_RING_TEAL, AstroBlockRegistry.PLANETOID_RING_YELLOW);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> {
            int color = 0;
            if (p_getColor_1_.getItem() instanceof BlockItem) {
                BlockItem itemBlock = (BlockItem) p_getColor_1_.getItem();
                if (itemBlock.getBlock() instanceof BlockPlanetoidGas) {
                    color = ((BlockPlanetoidGas) itemBlock.getBlock()).colorBase;
                }
            }
            return color;
        }, AstroBlockRegistry.PLANETOID_GAS_BLUE, AstroBlockRegistry.PLANETOID_GAS_GREEN, AstroBlockRegistry.PLANETOID_GAS_ORANGE, AstroBlockRegistry.PLANETOID_GAS_PURPLE, AstroBlockRegistry.PLANETOID_GAS_TEAL, AstroBlockRegistry.PLANETOID_GAS_YELLOW);
        event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> {
            int color = 0;
            if (p_getColor_1_.getItem() instanceof BlockItem) {
                BlockItem itemBlock = (BlockItem) p_getColor_1_.getItem();
                if (itemBlock.getBlock() instanceof BlockPlanetoidRing) {
                    color = ((BlockPlanetoidRing) itemBlock.getBlock()).colorBase;
                }
            }
            return color;
        }, AstroBlockRegistry.PLANETOID_RING_BLUE, AstroBlockRegistry.PLANETOID_RING_GREEN, AstroBlockRegistry.PLANETOID_RING_ORANGE, AstroBlockRegistry.PLANETOID_RING_PURPLE, AstroBlockRegistry.PLANETOID_RING_TEAL, AstroBlockRegistry.PLANETOID_RING_YELLOW);

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientProxy::onBlockColors);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientProxy::onItemColors);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient() {
        TabulaModels.loadAll();
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.SQUID_MUCUS, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.BLOCK_OF_SQUID_GOOP, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.PLANETOID_RING_BLUE, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.PLANETOID_RING_GREEN, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.PLANETOID_RING_ORANGE, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.PLANETOID_RING_PURPLE, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.PLANETOID_RING_TEAL, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.PLANETOID_RING_YELLOW, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.BURNT_TORCH, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.WALL_BURNT_TORCH, RenderType.getCutout());
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        RenderingRegistry.registerEntityRenderingHandler(AstroEntityRegistry.SPACE_SQUID, manager -> new RenderSpaceSquid(manager, TabulaModels.SPACE_SQUID, 1));
        RenderingRegistry.registerEntityRenderingHandler(AstroEntityRegistry.FALLING_STAR, manager -> new RenderFallingStar(manager));
    }

    public Item.Properties setupISTER(Item.Properties group) {
        return group.setISTER(ClientProxy::getTEISR);
    }

    @OnlyIn(Dist.CLIENT)
    public Object getSkyRendererForDim() {
        return skyRenderer == null ? skyRenderer = new CosmicSkyRenderer() : skyRenderer;
    }

}
