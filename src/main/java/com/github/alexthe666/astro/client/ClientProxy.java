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
import com.github.alexthe666.astro.server.entity.AstroEntityRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Astronautical.MODID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public static List<UUID> currentSquidRiders = new ArrayList<UUID>();

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient() {
        TabulaModels.loadAll();
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.SQUID_MUCUS, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(AstroBlockRegistry.BLOCK_OF_SQUID_GOOP, RenderType.getTranslucent());
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        RenderingRegistry.registerEntityRenderingHandler(AstroEntityRegistry.SPACE_SQUID, manager -> new RenderSpaceSquid(manager, TabulaModels.SPACE_SQUID, 1));
        RenderingRegistry.registerEntityRenderingHandler(AstroEntityRegistry.FALLING_STAR, manager -> new RenderFallingStar(manager));
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> getTEISR() {
        return AstroISTER::new;
    }

    public Item.Properties setupISTER(Item.Properties group) {
        return group.setISTER(ClientProxy::getTEISR);
    }


    @OnlyIn(Dist.CLIENT)
    public Object getSkyRendererForDim(){
        return new CosmicSkyRenderer();
    }
}
