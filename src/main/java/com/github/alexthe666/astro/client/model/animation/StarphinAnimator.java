package com.github.alexthe666.astro.client.model.animation;

import com.github.alexthe666.astro.client.model.TabulaModels;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import com.github.alexthe666.astro.server.entity.EntityStarphin;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class StarphinAnimator  extends AstroTabulaAnimator implements ITabulaModelAnimator<EntityStarphin> {

    public StarphinAnimator() {
        super(null);
    }

    @Override
    public void setRotationAngles(TabulaModel model, EntityStarphin entity, float v, float v1, float v2, float v3, float v4, float v5) {
        model.resetToDefaultPose();
        this.baseModel = TabulaModels.STARPHIN;

        float rotY = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), entity.prevFishPitch, entity.getFishPitch());
        model.getCube("body1").rotateAngleX += Math.toRadians(rotY);

    }
}
