package com.github.alexthe666.astro.server.item;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.server.entity.AstroEntityRegistry;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSquidSpawner extends Item {

    public ItemSquidSpawner() {
        super(new Properties().group(Astronautical.TAB));
        this.setRegistryName("astro:squid_spawner");
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        EntitySpaceSquid spaceSquid = new EntitySpaceSquid(AstroEntityRegistry.SPACE_SQUID, context.getWorld());
        BlockPos upPos = new BlockPos(context.getPos().getX(), 300, context.getPos().getZ());
        spaceSquid.setLocationAndAngles(upPos.getX() + 0.5D, upPos.getY() + 0.5D, upPos.getZ() + 0.5D, random.nextFloat() * 360 - 180F, 0);
        spaceSquid.onInitialSpawn(context.getWorld(), context.getWorld().getDifficultyForLocation(context.getPos()), SpawnReason.MOB_SUMMONED, null, null);
        spaceSquid.setFallingFromSky(true);
        spaceSquid.fallingProgress = 20F;
        context.getWorld().addEntity(spaceSquid);
        if(!context.getPlayer().isCreative()){
            context.getItem().shrink(1);
        }
        return ActionResultType.SUCCESS;
    }


    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").func_240699_a_(TextFormatting.GRAY));
    }
}
