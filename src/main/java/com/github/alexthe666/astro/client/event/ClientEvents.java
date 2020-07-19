package com.github.alexthe666.astro.client.event;

import com.github.alexthe666.astro.AstronauticalConfig;
import com.github.alexthe666.astro.client.ClientProxy;
import com.github.alexthe666.astro.client.render.CosmicSeaRenderInfo;
import com.github.alexthe666.astro.client.render.CosmicSkyRenderer;
import com.github.alexthe666.astro.server.effect.AstroEffectRegistry;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmBackupScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    private static Random random = new Random();
    private MatrixStack lastMatrixStack = null;
    private CosmicSkyRenderer skyRenderer = new CosmicSkyRenderer();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onWorldLoad(WorldEvent.Load event) {
        if(event.getWorld() instanceof ClientWorld){
            ClientWorld world = (ClientWorld)event.getWorld();
            if(world.func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")){
                world.field_239131_x_ = new CosmicSeaRenderInfo();
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogColors(EntityViewRenderEvent.FogColors event) {
        ClientWorld world = Minecraft.getInstance().world;
        if(world.func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")){
            FluidState fluidstate = event.getInfo().getFluidState();
            if (!fluidstate.isTagged(FluidTags.WATER)) {
                event.setRed(0);
                event.setGreen(0);
                event.setBlue(0);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogRender(EntityViewRenderEvent.RenderFogEvent event) {
        //Called right before sky rendered
        World world = Minecraft.getInstance().world;
        if(world.func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")) {
            skyRenderer.renderSky(new MatrixStack(), (float) event.getRenderPartialTicks());
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        //Called right before objects rendered
        World world = Minecraft.getInstance().world;
        if(world.func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")) {
            event.setDensity(-1);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onOpenGui(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (AstronauticalConfig.skipExperimentalSettingsGUI) {
            if (event.getGui() instanceof ConfirmBackupScreen) {
                ConfirmBackupScreen confirmBackupScreen = (ConfirmBackupScreen) event.getGui();
                String name = "";
                if (confirmBackupScreen.func_231171_q_() instanceof TranslationTextComponent) {
                    name = ((TranslationTextComponent) confirmBackupScreen.func_231171_q_()).getKey();
                }
                if (name.equals("selectWorld.backupQuestion.experimental")) {
                    confirmBackupScreen.callback.proceed(false, true);
                }
            }
            if (event.getGui() instanceof ConfirmScreen) {
                ConfirmScreen confirmScreen = (ConfirmScreen) event.getGui();
                String testAgainst = "selectWorld.backupQuestion.experimental";
                String name = "";
                if (confirmScreen.func_231171_q_() instanceof TranslationTextComponent) {
                    name = ((TranslationTextComponent) confirmScreen.func_231171_q_()).getKey();
                }
                if (name.equals(testAgainst)) {
                    confirmScreen.callbackFunction.accept(true);
                    System.out.println("BASED");
                }
            }
        }
    }


    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player.isPotionActive(AstroEffectRegistry.SQUIDFALL_EFFECT) && player.getActivePotionEffect(AstroEffectRegistry.SQUIDFALL_EFFECT).getDuration() > 5) {
            event.setRoll(random.nextFloat() * 4 - 2);
        }

    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if(event.getEntity().world.func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")){
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
        if(event.getEntity().world.func_234923_W_().func_240901_a_().getPath().equals("cosmic_sea")){
            if (event.getEntity().getPose() == Pose.SWIMMING) {
                event.getEntity().inWater = false;
            }
        }
    }
}