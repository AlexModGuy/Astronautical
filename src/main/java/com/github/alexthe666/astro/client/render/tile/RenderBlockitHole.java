package com.github.alexthe666.astro.client.render.tile;

import com.github.alexthe666.astro.client.model.ModelBlockitWorm;
import com.github.alexthe666.astro.server.block.BlockBlockitWormHole;
import com.github.alexthe666.astro.server.entity.tileentity.TileEntityBlockitHole;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderBlockitHole  extends TileEntityRenderer<TileEntityBlockitHole> {
    private static final ModelBlockitWorm WORM_MODEL = new ModelBlockitWorm();
    private static final RenderType WORM_TEXTURE = RenderType.getEntityCutoutNoCull(new ResourceLocation("astro:textures/entity/blockit_worm.png"));

    public RenderBlockitHole(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityBlockitHole te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn){
        float rotation = 0;
        float ticksExisted = Minecraft.getInstance().player.ticksExisted + partialTicks;
        Vector3d offset = Vector3d.ZERO;
        float xRot = 0;
        float extend = 0;
        if (te != null && te.getWorld() != null && te.getWorld().getBlockState(te.getPos()).getBlock() instanceof BlockBlockitWormHole) {
            Direction facing = te.getWorld().getBlockState(te.getPos()).get(BlockBlockitWormHole.FACING);
            offset = new Vector3d(facing.getDirectionVec().getX(), facing.getDirectionVec().getY(), facing.getDirectionVec().getZ());
            if(facing.getAxis() == Direction.Axis.Z){
                xRot = -90;
            }
            if(facing.getAxis() == Direction.Axis.X){
                xRot = 90;
            }
            if(facing == Direction.UP){
                xRot = 180;
            }
            rotation = facing.getHorizontalAngle();
            ticksExisted = te.ticksExisted + partialTicks;
            extend = te.prevExtendProgress + (te.extendProgress - te.prevExtendProgress) * partialTicks;
        }
        float extendMod = (extend / 20F) + 1F;
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(WORM_TEXTURE);

        matrixStackIn.push();
        matrixStackIn.translate(0.5F, 0.5F, 0.5F);
        matrixStackIn.translate(offset.getX() * extendMod, offset.getY() * extendMod, offset.getZ() * extendMod);
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, rotation, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, xRot, true));
        matrixStackIn.push();
        WORM_MODEL.setRotationAnglesForBlock(extend, ticksExisted);
        WORM_MODEL.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        matrixStackIn.pop();

    }
}
