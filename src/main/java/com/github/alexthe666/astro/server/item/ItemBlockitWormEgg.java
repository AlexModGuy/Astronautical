package com.github.alexthe666.astro.server.item;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.block.BlockBlockitWormHole;
import com.github.alexthe666.astro.server.entity.AstroEntityRegistry;
import com.github.alexthe666.astro.server.entity.EntityBlockitWorm;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class ItemBlockitWormEgg extends Item {

    public ItemBlockitWormEgg() {
        super(new Properties().group(Astronautical.TAB));
        this.setRegistryName("astro:blockit_worm_egg");
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        if(context.getWorld().getBlockState(context.getPos()).getBlock() == AstroBlockRegistry.METEORITE){
            if(!context.getPlayer().isCreative()){
                context.getItem().shrink(1);
            }
            context.getWorld().destroyBlock(context.getPos(), false);
            EntityBlockitWorm blockitWorm = new EntityBlockitWorm(AstroEntityRegistry.BLOCKIT_WORM, context.getWorld());
            blockitWorm.setPosition(context.getPos().getX() + 0.5D, context.getPos().getY(), context.getPos().getZ() + 0.5D);
            blockitWorm.setAttachmentFacing(context.getFace().getOpposite());
            context.getWorld().addEntity(blockitWorm);
        }
        return ActionResultType.PASS;
    }


}
