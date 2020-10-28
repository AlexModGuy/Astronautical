package com.github.alexthe666.astro.client.render.entity.layer;

import com.github.alexthe666.astro.client.model.ModelStarphin;
import com.github.alexthe666.astro.client.render.entity.RenderStarphin;
import com.github.alexthe666.astro.server.entity.EntityScuttlefish;
import com.github.alexthe666.astro.server.entity.EntityStarphin;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class LayerStarphinHeldItem extends LayerRenderer<EntityStarphin,  SegmentedModel<EntityStarphin>> {

    RenderStarphin renderer;

    public LayerStarphinHeldItem(RenderStarphin renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityStarphin entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entity.getHeldItem(Hand.MAIN_HAND);
        if (!itemstack.isEmpty()) {
            matrixStackIn.push();
            translateToBody(matrixStackIn);
            matrixStackIn.translate(-0.15F, 0, -0.45F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 90, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, -45, true));
            matrixStackIn.scale(1.2F, 1.2F, 1.2F);
            Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
    }

    protected void translateToBody(MatrixStack matrixStackIn) {
        ((ModelStarphin) this.renderer.getEntityModel()).body.translateRotate(matrixStackIn);
        ((ModelStarphin) this.renderer.getEntityModel()).head.translateRotate(matrixStackIn);
        ((ModelStarphin) this.renderer.getEntityModel()).snout.translateRotate(matrixStackIn);
    }
}