package com.github.alexthe666.astro.server.misc;

import com.github.alexthe666.astro.server.item.AstroItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;

public class AstroBannerRegistry {

    public static BannerPattern SPACE_SQUID_BANNER = addBanner("space_squid", new ItemStack(AstroItemRegistry.SPACE_SQUID_TENTACLE));

    public static BannerPattern addBanner(String name, ItemStack craftingStack) {
        return BannerPattern.create(name.toUpperCase(), name, "astro." + name, craftingStack);
    }


}
