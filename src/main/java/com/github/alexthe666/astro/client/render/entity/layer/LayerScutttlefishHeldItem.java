package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.render.entity.RenderScuttlefish;
import com.github.alexthe666.astro.server.entity.EntityScuttlefish;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class LayerScutttlefishHeldItem extends LayerRenderer<EntityScuttlefish, SegmentedModel<EntityScuttlefish>> {

    RenderScuttlefish renderer;

    public LayerScutttlefishHeldItem(RenderScuttlefish renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityScuttlefish entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entity.getHeldItem(Hand.MAIN_HAND);
        if (!itemstack.isEmpty()) {
            matrixStackIn.push();
            matrixStackIn.translate(0, 1.5F, 0.9F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
            Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
    }
}