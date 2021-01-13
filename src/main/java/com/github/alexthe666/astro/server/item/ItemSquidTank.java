package com.github.alexthe666.astro.server.item;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.entity.AstroEntityRegistry;
import com.github.alexthe666.astro.server.entity.EntityBlockitWorm;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class ItemSquidTank extends Item {

    public ItemSquidTank() {
        super(new Properties().group(Astronautical.TAB));
        this.setRegistryName("astro:squid_tank_item");
    }

    public boolean isValidLoc(IWorld world, BlockPos pos){
        for(int i = -1; i < 1; i++){
            for(int j = 0; j < 4; j++){
                for(int k = -1; k < 1; k++){
                    BlockState state = world.getBlockState(pos.add(i, j, k));
                    if(!state.getMaterial().isReplaceable()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos bottom = context.getPos().offset(context.getFace());
        if(isValidLoc(context.getWorld(), bottom)){
            if(!context.getPlayer().isCreative()){
                context.getItem().shrink(1);
            }
            for(int i = -1; i <= 1; i++) {
                for (int j = 0; j <= 4; j++) {
                    for (int k = -1; k <= 1; k++) {
                        BlockState state = AstroBlockRegistry.SQUID_TANK_GLASS.getDefaultState();
                        if(i == 0 && j == 2 && k == 0){
                            state = AstroBlockRegistry.SQUID_TANK.getDefaultState();
                        }
                        context.getWorld().setBlockState(bottom.add(i, j, k), state);
                    }
                }
            }

        }
        return ActionResultType.PASS;
    }
}
