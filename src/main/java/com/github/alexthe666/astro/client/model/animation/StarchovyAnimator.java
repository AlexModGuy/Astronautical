package com.github.alexthe666.astro.client.model.animation;

import com.github.alexthe666.astro.client.model.TabulaModels;
import com.github.alexthe666.astro.server.entity.EntityStarchovy;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class StarchovyAnimator extends AstroTabulaAnimator implements ITabulaModelAnimator<EntityStarchovy> {

    public StarchovyAnimator() {
        super(null);
    }

    @Override
    public void setRotationAngles(TabulaModel model, EntityStarchovy entity, float v, float v1, float v2, float v3, float v4, float v5) {
        model.resetToDefaultPose();
        this.baseModel = TabulaModels.SPACE_SQUID;
        float swimSpeed = 0.4275F;
        float swimDegree = 0.2F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.2F;
        model.swing(model.getCube("body1"), swimSpeed, swimDegree, false, 0, 0, v, v1);
        model.swing(model.getCube("body2"), swimSpeed, swimDegree, false, 0, 0, v, v1);
        model.swing(model.getCube("tail"), swimSpeed, swimDegree, false, 0.5F, 0, v, v1);
        model.swing(model.getCube("head"), swimSpeed, swimDegree * 0.5F, true, 0, 0, v, v1);
        model.swing(model.getCube("leftFin"), swimSpeed, swimDegree, false, 0, 0, v, v1);
        model.swing(model.getCube("rightFin"), swimSpeed, swimDegree, false, 0, 0, v, v1);

        model.flap(model.getCube("leftFin"), swimSpeed, swimDegree, false, 3, 0.2F, v, v1);
        model.flap(model.getCube("rightFin"), swimSpeed, swimDegree, false, 3, -0.2F, v, v1);

        model.swing(model.getCube("leftFin"), idleSpeed, idleDegree, false, 0, 0, v2, 1);
        model.swing(model.getCube("rightFin"), idleSpeed, idleDegree, false, 0, 0, v2, 1);
        model.walk(model.getCube("topFin"), idleSpeed, idleDegree * 0.3F, false, 1, -0.1F, v2, 1);
        model.walk(model.getCube("mouth"), idleSpeed, idleDegree * 0.3F, false, 1, 0.1F, v2, 1);

        float rotY = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), entity.prevFishPitch, entity.getFishPitch());
        model.getCube("body1").rotateAngleX += Math.toRadians(rotY);

    }
}
