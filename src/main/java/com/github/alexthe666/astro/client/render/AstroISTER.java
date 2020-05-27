package com.github.alexthe666.astro.client.render;

import com.github.alexthe666.astro.client.render.item.FallingStarItemRenderer;
import com.github.alexthe666.astro.server.item.AstroItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class AstroISTER extends ItemStackTileEntityRenderer {

    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(itemStackIn.getItem() == AstroItemRegistry.FALLING_STAR){
            FallingStarItemRenderer.renderFallingStar(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if(itemStackIn.getItem() == AstroItemRegistry.COSMOS_STAR){
            FallingStarItemRenderer.renderCosmosStar(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }
}
