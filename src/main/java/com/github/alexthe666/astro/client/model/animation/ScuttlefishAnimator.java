package com.github.alexthe666.astro.client.model.animation;

import com.github.alexthe666.astro.client.model.TabulaModels;
import com.github.alexthe666.astro.server.entity.EntityScuttlefish;
import com.github.alexthe666.astro.server.entity.EntityStaron;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.util.math.MathHelper;

public class ScuttlefishAnimator extends AstroTabulaAnimator implements ITabulaModelAnimator<EntityScuttlefish> {

    public ScuttlefishAnimator() {
        super(null);
    }

    @Override
    public void setRotationAngles(TabulaModel model, EntityScuttlefish scuttlefish, float v, float v1, float v2, float v3, float v4, float v5) {
        model.resetToDefaultPose();
        this.baseModel = TabulaModels.STARON;
        float swimSpeed = 0.4275F;
        float swimDegree = 0.3F;
        model.getCube("mantle").rotateAngleY += Math.toRadians(180);
        float tentacleScale = 1F + 0.35F * (float) MathHelper.cos(v * 0.7F + 3.5F) * v1;
        float armScale = scuttlefish.getHeldItemMainhand().isEmpty() ? tentacleScale : 1.5F;
        model.getCube("tentacles").setScale(1, 1, tentacleScale);
        model.getCube("tentacleRight").setScale(1, 1, armScale);
        model.getCube("tentacleLeft").setScale(1, 1, armScale);
        model.flap(model.getCube("wingLeft"), swimSpeed * 0.5F, swimDegree * 0.95F, false, 3.5F, 0, v2, 1);
        model.flap(model.getCube("wingRight"), swimSpeed * 0.5F, swimDegree * 0.95F, true, 3.5F, 0, v2, 1);
        model.flap(model.getCube("wingLeft"), swimSpeed * 1.5F, swimDegree * 0.95F, false, 3.5F, 0, v, v1);
        model.flap(model.getCube("wingRight"), swimSpeed * 1.5F, swimDegree * 0.95F, true, 3.5F, 0, v, v1);
        float armSpeed = scuttlefish.getHeldItemMainhand().isEmpty() ? swimSpeed : 0.35F;
        float armDegree = scuttlefish.getHeldItemMainhand().isEmpty() ? swimDegree : 0.1F;

        model.swing(model.getCube("tentacleRight"), armSpeed * 0.85F, armDegree * 0.75F, true, 3.5F, -0.1F, v2, 1);
        model.swing(model.getCube("tentacleLeft"), armSpeed * 0.85F, armDegree * 0.75F, false, 3.5F, -0.1F, v2, 1);
    }
}