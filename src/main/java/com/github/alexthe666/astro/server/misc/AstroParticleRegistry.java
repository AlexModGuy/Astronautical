package com.github.alexthe666.astro.server.misc;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.entity.AstroEntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroParticleRegistry {
   // public static final BasicParticleType STARPHIN_STAR = (BasicParticleType) new BasicParticleType(false).setRegistryName("astro:starphin_star");
    public static final BasicParticleType SQUID_BUBBLE = (BasicParticleType) new BasicParticleType(false).setRegistryName("astro:squid_bubble");

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        try {
            for (Field f : AstroParticleRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof ParticleType) {
                    event.getRegistry().register((ParticleType) obj);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
