package com.github.alexthe666.astro.client.event;

import com.github.alexthe666.astro.Astronautical;
import com.github.alexthe666.astro.client.ClientProxy;
import com.github.alexthe666.astro.client.render.CosmicSkyRenderer;
import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.block.BlockPlanetoidGas;
import com.github.alexthe666.astro.server.effect.AstroEffectRegistry;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import com.github.alexthe666.astro.server.world.AstroWorldRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    private static Random random = new Random();
    private MatrixStack lastMatrixStack = null;


    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player.isPotionActive(AstroEffectRegistry.SQUIDFALL_EFFECT) && player.getActivePotionEffect(AstroEffectRegistry.SQUIDFALL_EFFECT).getDuration() > 5) {
            event.setRoll(random.nextFloat() * 4 - 2);
        }

    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player.dimension == AstroWorldRegistry.COSMIC_SEA_TYPE) {
            MatrixStack matrixstack = event.getMatrixStack();
            ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
            Vec3d viewPosition = activerenderinfo.getProjectedView();
            double px = viewPosition.x;
            double py = viewPosition.y;
            double pz = viewPosition.z;
            matrixstack.push();
            //  GlStateManager.enableBlend();
            //  GlStateManager.disableCull();
            //  GlStateManager.depthMask(false);
            //   CosmicSkyRenderer.renderSky(matrixstack, (float)Minecraft.getInstance().getRenderPartialTicks());
            //  GlStateManager.depthMask(true);
            //  GlStateManager.enableCull();
            //  GlStateManager.disableBlend();
            matrixstack.pop();
            /*ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
            Vec3d viewPosition = activerenderinfo.getProjectedView();
            double px = viewPosition.x;
            double py = viewPosition.y;
            double pz = viewPosition.z;
            matrixstack.push();
            CosmicSkyRenderer.renderSky(matrixstack, (float)Minecraft.getInstance().getRenderPartialTicks());
            matrixstack.pop();*/
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if (event.getEntity().dimension == AstroWorldRegistry.COSMIC_SEA_TYPE) {
            boolean prevInWater = event.getEntity().inWater;
            event.getEntity().inWater = true;
        }
        if (event.getEntity().getRidingEntity() != null && event.getEntity().getRidingEntity() instanceof EntitySpaceSquid) {
            if (ClientProxy.currentSquidRiders.contains(event.getEntity().getUniqueID()) || event.getEntity() == Minecraft.getInstance().player && Minecraft.getInstance().gameSettings.thirdPersonView == 0) {
                event.setCanceled(true);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post(event.getEntity(), event.getRenderer(), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight()));
            }
        }
    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        if (event.getEntity().dimension == AstroWorldRegistry.COSMIC_SEA_TYPE) {
            if (event.getEntity().getPose() == Pose.SWIMMING) {
                event.getEntity().inWater = false;
            }
        }
    }
}