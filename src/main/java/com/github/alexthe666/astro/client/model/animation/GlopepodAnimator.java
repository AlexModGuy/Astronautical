package com.github.alexthe666.astro.client.model.animation;

import com.github.alexthe666.astro.client.model.TabulaModels;
import com.github.alexthe666.astro.server.entity.EntityGlopepod;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class GlopepodAnimator extends AstroTabulaAnimator implements ITabulaModelAnimator<EntityGlopepod> {

    public GlopepodAnimator() {
        super(null);
    }

    @Override
    public void setRotationAngles(TabulaModel model, EntityGlopepod entity, float v, float v1, float v2, float v3, float v4, float v5) {
        model.resetToDefaultPose();
        this.baseModel = TabulaModels.GLOPEPOD;
        float swimSpeed = 0.7F;
        float swimDegree = 0.2F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float rotY = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), entity.prevFishPitch, entity.getFishPitch());
        model.getCube("body1").rotateAngleX += Math.toRadians(rotY);
        model.walk(model.getCube("body1"), swimSpeed, swimDegree, false, 0, 0, v, v1);
        model.walk(model.getCube("body2"), swimSpeed, swimDegree, false, 0, 0, v, v1);
        model.swing(model.getCube("legRight"), swimSpeed, swimDegree, true, 0, 0, v, v1);
        model.swing(model.getCube("legLeft"), swimSpeed, swimDegree, false, 0, 0, v, v1);
        model.swing(model.getCube("antenna"), idleSpeed, idleDegree, false, 0, 0, v2, 1);
        model.swing(model.getCube("legRight"), idleSpeed, idleDegree, false, 3, 0, v2, 1);
        model.swing(model.getCube("legLeft"), idleSpeed, idleDegree, true, 3, 0, v2, 1);
        model.swing(model.getCube("antenna"), idleSpeed, idleDegree, false, 1, 0, v2, 1);

    }
}
