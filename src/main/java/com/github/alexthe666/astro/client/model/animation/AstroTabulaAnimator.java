package com.github.alexthe666.astro.client.model.animation;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.util.math.MathHelper;

public class AstroTabulaAnimator {

    protected TabulaModel baseModel;

    public AstroTabulaAnimator(TabulaModel baseModel) {
        this.baseModel = baseModel;
    }

    public void setRotateAngle(AdvancedModelBox model, float limbSwingAmount, float x, float y, float z) {
        model.rotateAngleX += limbSwingAmount * distance(model.rotateAngleX, x);
        model.rotateAngleY += limbSwingAmount * distance(model.rotateAngleY, y);
        model.rotateAngleZ += limbSwingAmount * distance(model.rotateAngleZ, z);
    }

    public void addToRotateAngle(AdvancedModelBox model, float limbSwingAmount, float x, float y, float z) {
        model.rotateAngleX += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationX, x);
        model.rotateAngleY += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationY, y);
        model.rotateAngleZ += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationZ, z);
    }

    public boolean isPartEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose != null && pose.rotateAngleX == original.defaultRotationX && pose.rotateAngleY == original.defaultRotationY && pose.rotateAngleZ == original.defaultRotationZ;
    }

    public boolean isPositionEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose.rotationPointX == original.defaultPositionX && pose.rotationPointY == original.defaultPositionY && pose.rotationPointZ == original.defaultPositionZ;
    }

    public void transitionTo(AdvancedModelBox from, AdvancedModelBox to, float timer, float maxTime, boolean oldFashioned) {
        if (oldFashioned) {
            from.rotateAngleX += ((to.rotateAngleX - from.rotateAngleX) / maxTime) * timer;
            from.rotateAngleY += ((to.rotateAngleY - from.rotateAngleY) / maxTime) * timer;
            from.rotateAngleZ += ((to.rotateAngleZ - from.rotateAngleZ) / maxTime) * timer;
        } else {
            transitionAngles(from, to, timer, maxTime);
        }
        from.rotationPointX += ((to.rotationPointX - from.rotationPointX) / maxTime) * timer;
        from.rotationPointY += ((to.rotationPointY - from.rotationPointY) / maxTime) * timer;
        from.rotationPointZ += ((to.rotationPointZ - from.rotationPointZ) / maxTime) * timer;
        from.offsetX += ((to.offsetX - from.offsetX) / maxTime) * timer;
        from.offsetY += ((to.offsetY - from.offsetY) / maxTime) * timer;
        from.offsetZ += ((to.offsetZ - from.offsetZ) / maxTime) * timer;

    }

    public void transitionAngles(AdvancedModelBox from, AdvancedModelBox to, float timer, float maxTime) {
        from.rotateAngleX += ((distance(from.rotateAngleX, to.rotateAngleX)) / maxTime) * timer;
        from.rotateAngleY += ((distance(from.rotateAngleY, to.rotateAngleY)) / maxTime) * timer;
        from.rotateAngleZ += ((distance(from.rotateAngleZ, to.rotateAngleZ)) / maxTime) * timer;
    }

    public float distance(float rotateAngleFrom, float rotateAngleTo) {
        return (float) atan2_accurate(MathHelper.sin(rotateAngleTo - rotateAngleFrom), MathHelper.cos(rotateAngleTo - rotateAngleFrom));
    }

    public void rotate(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    public void moveToPose(TabulaModel model, TabulaModel modelTo) {
        for (AdvancedModelBox cube : model.getCubes().values()) {
            if (!isPartEqual(baseModel.getCube(cube.boxName), modelTo.getCube(cube.boxName))) {
                float toX = modelTo.getCube(cube.boxName).rotateAngleX;
                float toY = modelTo.getCube(cube.boxName).rotateAngleY;
                float toZ = modelTo.getCube(cube.boxName).rotateAngleZ;
                model.llibAnimator.rotate(cube, distance(cube.rotateAngleX, toX), distance(cube.rotateAngleY, toY), distance(cube.rotateAngleZ, toZ));
            }
            if (!isPositionEqual(baseModel.getCube(cube.boxName), modelTo.getCube(cube.boxName))) {
                float toX = modelTo.getCube(cube.boxName).rotationPointX;
                float toY = modelTo.getCube(cube.boxName).rotationPointY;
                float toZ = modelTo.getCube(cube.boxName).rotationPointZ;
                model.llibAnimator.move(cube, distance(cube.rotationPointX, toX), distance(cube.rotationPointY, toY), distance(cube.rotationPointZ, toZ));
            }
        }
    }

    private static final double coeff_1 = Math.PI / 4;
    private static final double coeff_2 = coeff_1 * 3;

    public static final double atan2_accurate(double y, double x) {
        double r;
        if (y < 0) {
            y = -y;
            if (x > 0) {
                r = (x - y) / (x + y);
                return -(0.1963 * r * r * r - 0.9817 * r + coeff_1);
            } else {
                r = (x + y) / (y - x);
                return -(0.1963 * r * r * r - 0.9817 * r + coeff_2);
            }
        } else {
            if (y == 0) {
                y = 1.0E-25;
            }
            if (x > 0) {
                r = (x - y) / (x + y);
                return 0.1963 * r * r * r - 0.9817 * r + coeff_1;
            } else {
                r = (x + y) / (y - x);
                return 0.1963 * r * r * r - 0.9817 * r + coeff_2;
            }
        }
    }
}
