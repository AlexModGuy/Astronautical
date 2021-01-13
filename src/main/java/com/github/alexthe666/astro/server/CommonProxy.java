package com.github.alexthe666.astro.server;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.event.ServerEvents;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    public void setup() {
    }

    public void setupClient() {
    }

    public void setupParticles() {
    }

    public Item.Properties setupISTER(Item.Properties group) {
        return group;
    }

    public Object getSkyRendererForDim(){
        return null;
    }
}
