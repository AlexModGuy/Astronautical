package com.github.alexthe666.astro.client.model;

import com.github.alexthe666.astro.server.entity.EntityStarphin;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelStarphin extends AdvancedEntityModel<EntityStarphin> {
    public AdvancedModelBox body;
    public AdvancedModelBox finDorsal;
    public AdvancedModelBox tail;
    public AdvancedModelBox head;
    public AdvancedModelBox finLeft;
    public AdvancedModelBox finRight;
    public AdvancedModelBox fluke;
    public AdvancedModelBox snout;
    public AdvancedModelBox starTop;
    public AdvancedModelBox starRight;
    public AdvancedModelBox starBottomRight;
    public AdvancedModelBox starBottomLeft;
    public AdvancedModelBox starLeft;
    private ModelAnimator animator;

    public ModelStarphin() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.body = new AdvancedModelBox(this, 22, 0);
        this.body.setRotationPoint(0.0F, 20.5F, -1.0F);
        this.body.addBox(-4.0F, -3.5F, -6.5F, 8.0F, 7.0F, 13.0F, 0.0F, 0.0F, 0.0F);
        this.starTop = new AdvancedModelBox(this, 0, 40);
        this.starTop.setRotationPoint(0.0F, -3.5F, -6.0F);
        this.starTop.addBox(-2.0F, -7.0F, 0.0F, 4.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(starTop, -0.6981317007977318F, 0.0F, 0.0F);
        this.starLeft = new AdvancedModelBox(this, 0, 45);
        this.starLeft.mirror = true;
        this.starLeft.setRotationPoint(4.0F, -1.5F, -6.0F);
        this.starLeft.addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(starLeft, 0.0F, 0.6981317007977318F, 0.0F);
        this.starRight = new AdvancedModelBox(this, 0, 40);
        this.starRight.setRotationPoint(-4.0F, -1.5F, -6.0F);
        this.starRight.addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(starRight, 0.0F, -1.0471975511965976F, 0.0F);
        this.finLeft = new AdvancedModelBox(this, 30, 27);
        this.finLeft.setRotationPoint(-4.0F, 3.0F, -4.5F);
        this.finLeft.addBox(-7.0F, -0.5F, -2.0F, 7.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.starBottomRight = new AdvancedModelBox(this, 10, 57);
        this.starBottomRight.setRotationPoint(-3.0F, 3.5F, -6.0F);
        this.starBottomRight.addBox(-3.0F, 0.0F, 0.0F, 5.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(starBottomRight, 0.6981317007977318F, 0.0F, 0.0F);
        this.finDorsal = new AdvancedModelBox(this, 51, 0);
        this.finDorsal.setRotationPoint(0.0F, -3.0F, -3.0F);
        this.finDorsal.addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(finDorsal, 1.0471975511965976F, 0.0F, 0.0F);
        this.starBottomLeft = new AdvancedModelBox(this, 0, 57);
        this.starBottomLeft.setRotationPoint(3.0F, 3.5F, -6.0F);
        this.starBottomLeft.addBox(-2.0F, 0.0F, 0.0F, 5.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(starBottomLeft, 0.6981317007977318F, 0.0F, 0.0F);
        this.finRight = new AdvancedModelBox(this, 30, 27);
        this.finRight.mirror = true;
        this.finRight.setRotationPoint(4.0F, 3.0F, -4.5F);
        this.finRight.addBox(0.0F, -0.5F, -2.0F, 7.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.head = new AdvancedModelBox(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, -6.5F);
        this.head.addBox(-4.0F, -3.5F, -6.0F, 8.0F, 7.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.tail = new AdvancedModelBox(this, 0, 20);
        this.tail.setRotationPoint(0.0F, 1.0F, 6.5F);
        this.tail.addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 11.0F, 0.0F, 0.0F, 0.0F);
        this.fluke = new AdvancedModelBox(this, 19, 20);
        this.fluke.setRotationPoint(0.0F, 0.0F, 9.0F);
        this.fluke.addBox(-5.0F, -0.5F, 0.0F, 10.0F, 1.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.snout = new AdvancedModelBox(this, 0, 13);
        this.snout.setRotationPoint(0.0F, 2.5F, -6.0F);
        this.snout.addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.head.addChild(this.starTop);
        this.head.addChild(this.starLeft);
        this.head.addChild(this.starRight);
        this.body.addChild(this.finLeft);
        this.head.addChild(this.starBottomRight);
        this.body.addChild(this.finDorsal);
        this.head.addChild(this.starBottomLeft);
        this.body.addChild(this.finRight);
        this.body.addChild(this.head);
        this.body.addChild(this.tail);
        this.tail.addChild(this.fluke);
        this.head.addChild(this.snout);
        this.updateDefaultPose();
        animator = ModelAnimator.create();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body, finDorsal, tail, head, finLeft, finRight, fluke, snout, starTop, starRight, starBottomRight, starBottomLeft, starLeft);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        animator.update(entity);
        animator.setAnimation(EntityStarphin.ANIMATION_SPIN);
        animator.startKeyframe(40);
        float spins = 3;
        this.animator.rotate(body, 0, 0, (float) Math.toRadians(360 * spins));
        animator.endKeyframe();
    }

    private void rotateFrom(AdvancedModelBox renderer, float degX, float degY, float degZ) {
        animator.rotate(renderer, (float) Math.toRadians(degX) - renderer.defaultRotationX, (float) Math.toRadians(degY) - renderer.defaultRotationY, (float) Math.toRadians(degZ) - renderer.defaultRotationZ);
    }

    @Override
    public void setRotationAngles(EntityStarphin entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float swimSpeed = 0.4F;
        float swimDegree = 0.3F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        this.walk(body, swimSpeed, swimDegree * 0.1F, false, 0F, 0F, limbSwing, limbSwingAmount);
        this.walk(tail, swimSpeed, swimDegree, false, 0.4F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(fluke, swimSpeed, swimDegree * 0.5F, false, 0.4F, 0F, limbSwing, limbSwingAmount);
        this.walk(finRight, swimSpeed, swimDegree, false, 1F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(finLeft, swimSpeed, swimDegree, false, 1F, 0.1F, limbSwing, limbSwingAmount);
        this.flap(finRight, swimSpeed, swimDegree, true, 2F, -0.1F, limbSwing, limbSwingAmount);
        this.flap(finLeft, swimSpeed, swimDegree, false, 2F, -0.1F, limbSwing, limbSwingAmount);

    }

}
