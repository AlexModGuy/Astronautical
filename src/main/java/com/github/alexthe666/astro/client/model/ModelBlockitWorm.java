package com.github.alexthe666.astro.client.model;

import com.github.alexthe666.astro.server.entity.EntityBlockitWorm;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBlockitWorm extends AdvancedEntityModel<EntityBlockitWorm> {
    public AdvancedModelBox bodySegment1;
    public AdvancedModelBox segmentArms1;
    public AdvancedModelBox bodySegment2;
    public AdvancedModelBox segmentArms2;
    public AdvancedModelBox bodySegment3;
    public AdvancedModelBox segmentArms3;
    public AdvancedModelBox bodySegment4;
    public AdvancedModelBox segmentArms4;
    public AdvancedModelBox head;
    public AdvancedModelBox head2;
    public AdvancedModelBox teethLeft;
    public AdvancedModelBox teethRight;

    public ModelBlockitWorm() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.segmentArms4 = new AdvancedModelBox(this, 16, 0);
        this.segmentArms4.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.segmentArms4.addBox(-4.0F, -8.0F, 0.0F, 8, 16, 1, 0.0F);
        this.bodySegment2 = new AdvancedModelBox(this, 0, 0);
        this.bodySegment2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodySegment2.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
        this.segmentArms3 = new AdvancedModelBox(this, 16, 0);
        this.segmentArms3.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.segmentArms3.addBox(-4.0F, -8.0F, 0.0F, 8, 16, 1, 0.0F);
        this.bodySegment4 = new AdvancedModelBox(this, 0, 0);
        this.bodySegment4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodySegment4.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
        this.teethRight = new AdvancedModelBox(this, 34, 0);
        this.teethRight.mirror = true;
        this.teethRight.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.teethRight.addBox(-9.0F, -4.81F, -1.51F, 9, 5, 3, 0.0F);
        this.bodySegment3 = new AdvancedModelBox(this, 0, 0);
        this.bodySegment3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bodySegment3.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
        this.head = new AdvancedModelBox(this, 0, 20);
        this.head.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.head.addBox(-4.0F, -3.0F, -2.5F, 8, 3, 5, 0.0F);
        this.head2 = new AdvancedModelBox(this, 26, 24);
        this.head2.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.head2.addBox(-2.5F, -1.0F, -1.5F, 5, 1, 3, 0.0F);
        this.bodySegment1 = new AdvancedModelBox(this, 0, 0);
        this.bodySegment1.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bodySegment1.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
        this.segmentArms1 = new AdvancedModelBox(this, 16, 0);
        this.segmentArms1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.segmentArms1.addBox(-4.0F, -8.0F, 0.0F, 8, 16, 1, 0.0F);
        this.segmentArms2 = new AdvancedModelBox(this, 16, 0);
        this.segmentArms2.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.segmentArms2.addBox(-4.0F, -8.0F, 0.0F, 8, 16, 1, 0.0F);
        this.teethLeft = new AdvancedModelBox(this, 34, 0);
        this.teethLeft.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.teethLeft.addBox(0.0F, -4.81F, -1.51F, 9, 5, 3, 0.0F);
        this.bodySegment4.addChild(this.segmentArms4);
        this.bodySegment1.addChild(this.bodySegment2);
        this.bodySegment3.addChild(this.segmentArms3);
        this.bodySegment3.addChild(this.bodySegment4);
        this.head2.addChild(this.teethRight);
        this.bodySegment2.addChild(this.bodySegment3);
        this.bodySegment4.addChild(this.head);
        this.head.addChild(this.head2);
        this.bodySegment1.addChild(this.segmentArms1);
        this.bodySegment2.addChild(this.segmentArms2);
        this.head2.addChild(this.teethLeft);
        this.updateDefaultPose();
    }

    @Override
    public void setRotationAngles(EntityBlockitWorm entity, float v, float v1, float v2, float v3, float v4) {
        float lvt_7_1_ = v2 - (float)entity.ticksExisted;
        this.resetToDefaultPose();
        float progress = entity.getClientPeekAmount(lvt_7_1_);
        float swimSpeed = 0.4275F;
        float swimDegree = 0.3F;
        float extend = progress * 1;
        this.flap(teethRight, swimSpeed * 0.1F, swimDegree * 0.4F, false, 0, 0, v2, 1);
        this.flap(teethLeft, swimSpeed * 0.1F, swimDegree * 0.4F, true, 0, 0, v2, 1);
        float close = extend;
        if(!entity.getPassengers().isEmpty()){
            close = 20F;
        }
        if(extend > 0){
            this.flap(segmentArms1, swimSpeed, swimDegree * 0.1F, false, 0, 0, v2, 1);
            this.flap(segmentArms2, swimSpeed, swimDegree * 0.1F, false, 0, 0F, v2, 1);
            this.flap(segmentArms3, swimSpeed, swimDegree * 0.1F, false, 0, 0F, v2, 1);
            this.flap(segmentArms4, swimSpeed, swimDegree * 0.1F, false, 0, 0F, v2, 1);
            this.swing(segmentArms1, swimSpeed, swimDegree, false, 0, 0, v2, 1);
            this.swing(segmentArms2, swimSpeed, swimDegree, false, 0, 0F, v2, 1);
            this.swing(segmentArms3, swimSpeed, swimDegree, false, 0, 0F, v2, 1);
            this.swing(segmentArms4, swimSpeed, swimDegree, false, 0, 0F, v2, 1);

        }
        progressRotation(teethRight, close, 0, 0, (float)Math.toRadians(70F), 20F);
        progressRotation(teethLeft, close, 0, 0, (float)Math.toRadians(-70F), 20F);
        progressPosition(teethRight, close, -1.5F, 0, 0, 20F);
        progressPosition(teethLeft, close, 1.5F, 0, 0, 20F);
        bodySegment2.setScale(0.99F, 0.99F, 0.99F);
        bodySegment2.scaleChildren = false;
        bodySegment3.setScale(0.98F, 0.98F, 0.98F);
        bodySegment3.scaleChildren = false;
        bodySegment4.setScale(0.97F, 0.97F, 0.97F);
        bodySegment4.scaleChildren = false;
        progressPosition(bodySegment2, extend, 0, -15.5F, 0, 20F);
        progressPosition(bodySegment3, extend, 0, -15.5F, 0, 20F);
        progressPosition(bodySegment4, extend, 0, -15.5F, 0, 20F);
        AdvancedModelBox[] segments = new AdvancedModelBox[]{bodySegment1, bodySegment2, bodySegment3, bodySegment4};
        if(close > 0){
            this.chainSwing(segments, swimSpeed, swimDegree * 1, 3, v2 , 1 );
            this.chainFlap(segments, swimSpeed, swimDegree * 0.3F, 2, v2 , 1 );
        }
    }

    public void setRotationAnglesForBlock(float extend, float ticksExisted) {
        this.resetToDefaultPose();
        float swimSpeed = 0.4275F;
        float swimDegree = 0.3F;
        this.flap(segmentArms1, swimSpeed, swimDegree * 0.1F, false, 0, 0, ticksExisted, 1);
        this.flap(segmentArms2, swimSpeed, swimDegree * 0.1F, false, 0, 0F, ticksExisted, 1);
        this.flap(segmentArms3, swimSpeed, swimDegree * 0.1F, false, 0, 0F, ticksExisted, 1);
        this.flap(segmentArms4, swimSpeed, swimDegree * 0.1F, false, 0, 0F, ticksExisted, 1);
        this.swing(segmentArms1, swimSpeed, swimDegree, false, 0, 0, ticksExisted, 1);
        this.swing(segmentArms2, swimSpeed, swimDegree, false, 0, 0F, ticksExisted, 1);
        this.swing(segmentArms3, swimSpeed, swimDegree, false, 0, 0F, ticksExisted, 1);
        this.swing(segmentArms4, swimSpeed, swimDegree, false, 0, 0F, ticksExisted, 1);
        this.flap(teethRight, swimSpeed * 0.1F, swimDegree * 0.4F, false, 0, 0, ticksExisted, 1);
        this.flap(teethLeft, swimSpeed * 0.1F, swimDegree * 0.4F, true, 0, 0, ticksExisted, 1);
        progressRotation(teethRight, extend, 0, 0, (float)Math.toRadians(70F), 20F);
        progressRotation(teethLeft, extend, 0, 0, (float)Math.toRadians(-70F), 20F);
        progressPosition(teethRight, extend, -1.5F, 0, 0, 20F);
        progressPosition(teethLeft, extend, 1.5F, 0, 0, 20F);
        AdvancedModelBox[] segments = new AdvancedModelBox[]{bodySegment1};
        if(extend > 0){
            this.chainSwing(segments, swimSpeed, swimDegree * 1, 3, ticksExisted , 1 );
            this.chainFlap(segments, swimSpeed, swimDegree * 0.2F, 2, ticksExisted , 1 );
        }


     /*
       bodySegment2.setScale(0.99F, 0.99F, 0.99F);
        bodySegment2.scaleChildren = false;
        bodySegment3.setScale(0.98F, 0.98F, 0.98F);
        bodySegment3.scaleChildren = false;
        bodySegment4.setScale(0.97F, 0.97F, 0.97F);
        bodySegment4.scaleChildren = false;
        progressPosition(bodySegment2, extend, 0, -15.5F, 0, 20F);
        progressPosition(bodySegment3, extend, 0, -15.5F, 0, 20F);
        progressPosition(bodySegment4, extend, 0, -15.5F, 0, 20F);

        this.flap(segmentArms1, swimSpeed, swimDegree * 0.1F, false, 0, 0, ticksExisted, 1);
        this.flap(segmentArms2, swimSpeed, swimDegree * 0.1F, false, 1, 0F, ticksExisted, 1);
        this.flap(segmentArms3, swimSpeed, swimDegree * 0.1F, false, 2, 0F, ticksExisted, 1);
        this.flap(segmentArms4, swimSpeed, swimDegree * 0.1F, false, 3, 0F, ticksExisted, 1);
        this.swing(segmentArms1, swimSpeed, swimDegree, false, 0, 0, ticksExisted, 1);
        this.swing(segmentArms2, swimSpeed, swimDegree, false, 1, 0F, ticksExisted, 1);
        this.swing(segmentArms3, swimSpeed, swimDegree, false, 2, 0F, ticksExisted, 1);
        this.swing(segmentArms4, swimSpeed, swimDegree, false, 3, 0F, ticksExisted, 1);

         */
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(bodySegment1);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(bodySegment1, bodySegment2, bodySegment3, bodySegment4, segmentArms1, segmentArms2, segmentArms3, segmentArms4, head, head2, teethLeft, teethRight);
    }
}
