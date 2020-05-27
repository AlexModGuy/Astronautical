package com.github.alexthe666.astro.server.effect;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;

public class PotionSquidFall extends Effect {

    public PotionSquidFall() {
        super(EffectType.HARMFUL, 0X4E9575);
        this.setRegistryName(Astronautical.MODID, "squidfall");
    }

    public void performEffect(LivingEntity LivingEntityIn, int amplifier) {
    }

    public boolean isReady(int duration, int amplifier) {
        return duration > 0;
    }

    public String getName() {
        return "astro.potion.squid_fall";
    }

    public java.util.List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
