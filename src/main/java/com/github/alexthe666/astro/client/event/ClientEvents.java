package com.github.alexthe666.astro.client.event;

import com.github.alexthe666.astro.client.ClientProxy;
import com.github.alexthe666.astro.server.effect.AstroEffectRegistry;
import com.github.alexthe666.astro.server.entity.EntitySpaceSquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    private static Random random = new Random();

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player.isPotionActive(AstroEffectRegistry.SQUIDFALL_EFFECT) && player.getActivePotionEffect(AstroEffectRegistry.SQUIDFALL_EFFECT).getDuration() > 5){
            event.setRoll(random.nextFloat() * 4 - 2);
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if (event.getEntity().getRidingEntity() != null && event.getEntity().getRidingEntity() instanceof EntitySpaceSquid) {
            if (ClientProxy.currentSquidRiders.contains(event.getEntity().getUniqueID()) || event.getEntity() == Minecraft.getInstance().player && Minecraft.getInstance().gameSettings.thirdPersonView == 0) {
                event.setCanceled(true);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post(event.getEntity(), event.getRenderer(), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight()));
            }
        }
    }
}