package com.github.alexthe666.astro.server.effect;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroEffectRegistry {
    public static final Effect SQUIDFALL_EFFECT = new PotionSquidFall();

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Effect> event) {
        event.getRegistry().registerAll(SQUIDFALL_EFFECT);
    }
}
