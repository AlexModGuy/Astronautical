package com.github.alexthe666.astro.server.item;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGlassHelmet extends ArmorItem {

    public ItemGlassHelmet() {
        super(AstroItemRegistry.GLASS_HELMET_MATERIAL, EquipmentSlotType.HEAD, new Item.Properties().group(Astronautical.TAB));
        this.setRegistryName(Astronautical.MODID, "glass_helmet");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "astro:textures/model/armor/glass_helmet_0.png";
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").func_240699_a_(TextFormatting.GRAY));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
