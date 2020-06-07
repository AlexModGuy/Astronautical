package com.github.alexthe666.astro.client.model;

import com.github.alexthe666.astro.client.model.animation.AstroTabulaAnimator;
import com.github.alexthe666.astro.server.entity.EntityGlopepod;
import com.github.alexthe666.astro.server.entity.EntityStaron;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class StaronAnimator extends AstroTabulaAnimator implements ITabulaModelAnimator<EntityStaron> {

    public StaronAnimator() {
        super(null);
    }

    @Override
    public void setRotationAngles(TabulaModel model, EntityStaron entity, float v, float v1, float v2, float v3, float v4, float v5) {
        model.resetToDefaultPose();
        this.baseModel = TabulaModels.STARON;
        float swimSpeed = 0.7F;
        float swimDegree = 0.8F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float rotY = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), entity.prevFishPitch, entity.getFishPitch());
        model.getCube("center").rotateAngleX += Math.toRadians(rotY);
        model.walk(model.getCube("arm1A"), swimSpeed, swimDegree, false, 0, -0.3F, v, v1);
        model.walk(model.getCube("arm2A"), swimSpeed, swimDegree, false, 0, -0.3F, v, v1);
        model.walk(model.getCube("arm3A"), swimSpeed, swimDegree, false, 0, -0.3F, v, v1);
        model.walk(model.getCube("arm4A"), swimSpeed, swimDegree, false, 0, -0.3F, v, v1);
        model.walk(model.getCube("arm5A"), swimSpeed, swimDegree, false, 0, -0.3F, v, v1);

        model.walk(model.getCube("arm1B"), swimSpeed, swimDegree, false, -0.1F, 0, v, v1);
        model.walk(model.getCube("arm2B"), swimSpeed, swimDegree, false, -0.1F, 0, v, v1);
        model.walk(model.getCube("arm3B"), swimSpeed, swimDegree, false, -0.1F, 0, v, v1);
        model.walk(model.getCube("arm4B"), swimSpeed, swimDegree, false, -0.1F, 0, v, v1);
        model.walk(model.getCube("arm5B"), swimSpeed, swimDegree, false, -0.1F, 0, v, v1);
    }
}
