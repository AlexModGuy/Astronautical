package com.github.alexthe666.astro.client.model.animation;

import com.github.alexthe666.astro.client.model.TabulaModels;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class SpaceSquidAnimator extends AstroTabulaAnimator implements ITabulaModelAnimator<EntitySpaceSquid> {

    public SpaceSquidAnimator() {
        super(null);
    }

    @Override
    public void setRotationAngles(TabulaModel model, EntitySpaceSquid entity, float limbSwing, float limbSwingAmount, float ticksExisted, float v3, float v4, float v5) {
        model.resetToDefaultPose();
        this.baseModel = TabulaModels.SPACE_SQUID;
        model.llibAnimator.update(entity);
        boolean swimming = !entity.isFallen();
        float swimSpeed = 0.4275F;
        float swimDegree = 0.3F;
        float tentacle = (float)Math.max(MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), entity.lastTentacleAngle, entity.tentacleAngle), 0);
        if (swimming) {
            model.walk(model.getCube("tenticleA1"), swimSpeed, swimDegree, false, 0, -0.3F, tentacle, 1);
            model.walk(model.getCube("tenticleB1"), swimSpeed, swimDegree * 0.5F, false, 0, -0.1F, tentacle, 1);
            model.walk(model.getCube("tenticleA2"), swimSpeed, swimDegree * 0.75F, false, 0, -0.3F, tentacle, 1);
            model.walk(model.getCube("tenticleB2Big"), swimSpeed, swimDegree * 0.75F, false, 1, -0.0F, tentacle, 1);
            model.walk(model.getCube("tenticleC2Big"), swimSpeed, swimDegree * 0.75F, false, 2, -0.0F, tentacle, 1);
            model.walk(model.getCube("suckerD2"), swimSpeed, swimDegree * 0.75F, false, 1, -0.15F, tentacle, 1);
            model.walk(model.getCube("tenticleA3"), swimSpeed, swimDegree, false, 0, -0.3F, tentacle, 1);
            model.walk(model.getCube("tenticleB3"), swimSpeed, swimDegree * 0.5F, false, 0, -0.1F, tentacle, 1);
            model.walk(model.getCube("tenticleA4"), swimSpeed, swimDegree, false, 0, -0.3F, tentacle, 1);
            model.walk(model.getCube("tenticleB4"), swimSpeed, swimDegree * 0.5F, false, 0, -0.1F, tentacle, 1);
            model.walk(model.getCube("tenticleA5"), swimSpeed, swimDegree, false, 0, -0.3F, tentacle, 1);
            model.walk(model.getCube("tenticleB5"), swimSpeed, swimDegree * 0.5F, false, 0, -0.1F, tentacle, 1);
            model.walk(model.getCube("tenticleA6"), swimSpeed, swimDegree, false, 0, -0.3F, tentacle, 1);
            model.walk(model.getCube("tenticleB6"), swimSpeed, swimDegree * 0.5F, false, 0, -0.1F, tentacle, 1);
            model.walk(model.getCube("tenticleA7"), swimSpeed, swimDegree * 0.75F, false, 0, -0.3F, tentacle, 1);
            model.walk(model.getCube("tenticleB7Big"), swimSpeed, swimDegree * 0.75F, false, 1, -0.0F, tentacle, 1);
            model.walk(model.getCube("tenticleC7Big"), swimSpeed, swimDegree * 0.75F, false, 2, -0.0F, tentacle, 1);
            model.walk(model.getCube("suckerD7"), swimSpeed, swimDegree * 0.75F, false, 1, -0.15F, tentacle, 1);
            model.walk(model.getCube("tenticleA8"), swimSpeed, swimDegree, false, 0, -0.3F, tentacle, 1);
            model.walk(model.getCube("tenticleB8"), swimSpeed, swimDegree * 0.5F, false, 0, -0.1F, tentacle, 1);

            model.walk(model.getCube("body1"), swimSpeed * 0.5F, swimDegree * 0.15F, false, 3.5F, 0.1F, ticksExisted, 1);
            model.walk(model.getCube("body2"), swimSpeed * 0.5F, swimDegree * 0.15F, false, 3.0F, 0.1F, ticksExisted, 1);
            model.flap(model.getCube("fringeLeft"), swimSpeed * 0.5F, swimDegree * 0.95F, false, 3.5F, 0, ticksExisted, 1);
            model.flap(model.getCube("fringeRight"), swimSpeed * 0.5F, swimDegree * 0.95F, true, 3.5F, 0, ticksExisted, 1);
            float bodyScale = 1 + 0.05F * (float) Math.sin(tentacle * swimSpeed + 3.5F);
            model.getCube("body1").setShouldScaleChildren(false);
            model.getCube("body1").setScale(bodyScale, bodyScale, bodyScale);
            model.getCube("body2").rotationPointZ -= bodyScale * 2;
            model.getCube("body2").setScale(bodyScale, bodyScale, bodyScale);
            model.getCube("body3").rotationPointZ -= bodyScale * 2;
            float siphonScale = 1 + 0.3F * (float) Math.sin(ticksExisted * swimSpeed  * 0.5F + 2.5F);
            model.getCube("siphon").setScale(siphonScale, siphonScale, siphonScale);
        }
        for (AdvancedModelBox cube : model.getCubes().values()) {
            if (entity.fallingProgress > 0.0F) {
                if (!isPartEqual(cube, TabulaModels.SPACE_SQUID_FALLING.getCube(cube.boxName))) {
                    transitionTo(cube, TabulaModels.SPACE_SQUID_FALLING.getCube(cube.boxName), entity.fallingProgress, 20, false);
                }
            }
            if (entity.injuredProgress > 0.0F) {
                if (!isPartEqual(cube, TabulaModels.SPACE_SQUID_INJURED.getCube(cube.boxName))) {
                    transitionTo(cube, TabulaModels.SPACE_SQUID_INJURED.getCube(cube.boxName), entity.injuredProgress, 20, false);
                }
            }
        }
        float deadSpeed = 0.015F;
        if (entity.injuredProgress > 0.0F) {
            model.swing(model.getCube("tenticleA1"), deadSpeed, 0.1F, false, 0, -0.1F, ticksExisted, 1);
            model.swing(model.getCube("tenticleB1"), deadSpeed, 0.1F, false, 1, 0, ticksExisted, 1);
            model.swing(model.getCube("tenticleA2"), deadSpeed, 0.1F, false, 1, 0.1F, ticksExisted, 1);
            model.walk(model.getCube("tenticleA2"), deadSpeed, 0.1F, false, 1, -0.1F, ticksExisted, 1);
            model.walk(model.getCube("tenticleB2Big"), deadSpeed, 0.05F, false, 0, -0.1F, ticksExisted, 1);
            model.walk(model.getCube("tenticleC2Big"), deadSpeed, 0.1F, true, 1, -0.2F, ticksExisted, 1);
            model.walk(model.getCube("suckerD2"), deadSpeed, 0.1F, true, 1, 0.2F, ticksExisted, 1);
            model.walk(model.getCube("tenticleA3"), deadSpeed, 0.1F, false, -3, -0.1F, ticksExisted, 1);
            model.walk(model.getCube("tenticleB3"), deadSpeed, 0.1F, false, -2, 0, ticksExisted, 1);
            model.swing(model.getCube("tenticleA4"), deadSpeed, 0.1F, false, -2, 0.1F, ticksExisted, 1);
            model.swing(model.getCube("tenticleB4"), deadSpeed, 0.1F, false, -3, 0, ticksExisted, 1);
            model.swing(model.getCube("tenticleA5"), deadSpeed, 0.1F, false, -1, -0.2F, ticksExisted, 1);
            model.swing(model.getCube("tenticleB5"), deadSpeed, 0.1F, false, -1, 0, ticksExisted, 1);
            model.swing(model.getCube("tenticleA6"), deadSpeed, 0.1F, false, -1, -0.0F, ticksExisted, 1);
            model.swing(model.getCube("tenticleB6"), deadSpeed, 0.1F, false, -3, -0.1F, ticksExisted, 1);
            model.walk(model.getCube("tenticleA7"), deadSpeed, 0.1F, false, 1, 0.1F, ticksExisted, 1);
            model.walk(model.getCube("tenticleB7Big"), deadSpeed, 0.1F, false, 0, -0.1F, ticksExisted, 1);
            model.walk(model.getCube("tenticleC7Big"), deadSpeed, 0.1F, false, -1, -0.1F, ticksExisted, 1);
            model.swing(model.getCube("tenticleA8"), deadSpeed, 0.1F, false, 3, -0.2F, ticksExisted, 1);
            model.swing(model.getCube("tenticleB8"), deadSpeed, 0.1F, false, 2, 0, ticksExisted, 1);
            model.swing(model.getCube("siphon"), deadSpeed, 0.3F, false, 1, -0.2F, ticksExisted, 1);
            float bodyScale = 1 + 0.05F * (float) Math.sin(ticksExisted * deadSpeed * 2 + 3.5F);
            model.getCube("body1").setShouldScaleChildren(false);
            model.getCube("body1").setScale(bodyScale, bodyScale, bodyScale);
            model.getCube("body2").rotationPointZ -= bodyScale * 2;
            model.getCube("body2").setScale(bodyScale, bodyScale, bodyScale);
            model.getCube("body3").rotationPointZ -= bodyScale * 2;
            float siphonScale = 1 + 0.25F * (float) Math.sin(ticksExisted * deadSpeed * 2 + 2.5F);
            model.getCube("siphon").setScale(siphonScale, siphonScale, siphonScale);
        }
        if (entity.fallingProgress > 0.0F) {
            model.getCube("body1").rotateAngleY = ticksExisted * 0.1F;
            model.walk(model.getCube("body1"), 0.15F, 0.45F, false, 0, 1, ticksExisted, 1);
            float bodyScale = 1 + 0.05F * (float) Math.sin(ticksExisted * deadSpeed * 2 + 3.5F);
            model.getCube("body1").setShouldScaleChildren(false);
            model.getCube("body1").setScale(bodyScale, bodyScale, bodyScale);
            model.getCube("body2").rotationPointZ -= bodyScale * 2;
            model.getCube("body2").setScale(bodyScale, bodyScale, bodyScale);
            model.getCube("body3").rotationPointZ -= bodyScale * 2;
            float siphonScale = 1 + 0.25F * (float) Math.sin(ticksExisted * deadSpeed * 2 + 2.5F);
            model.getCube("siphon").setScale(siphonScale, siphonScale, siphonScale);
        }
        float rotateAroundProgress = (1F - Math.max(entity.injuredProgress, entity.fallingProgress) / 20F) * (float)Math.PI;
        float mantleY = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), entity.prevSquidPitch, entity.getSquidPitch());
        model.getCube("body1").rotateAngleX += Math.toRadians(-mantleY);
        model.getCube("body1").rotateAngleY -= rotateAroundProgress;


    }
}
