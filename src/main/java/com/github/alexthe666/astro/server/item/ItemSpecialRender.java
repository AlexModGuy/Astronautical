package com.github.alexthe666.astro.server.item;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.item.Item;

public class ItemSpecialRender extends Item {

    public ItemSpecialRender(Properties properties) {
        super(Astronautical.PROXY.setupISTER(properties));
    }
}
