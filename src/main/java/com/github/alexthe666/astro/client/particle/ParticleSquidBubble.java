package com.github.alexthe666.astro.client.particle;

import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class ParticleSquidBubble extends SpriteTexturedParticle {

    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("astro:textures/particle/squid_bubble.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("astro:textures/particle/squid_bubble_alt.png");
    private boolean type;

    public ParticleSquidBubble(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double colorR, double colorG, double colorB) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, colorR, colorG, colorB);
        this.motionX *= 0.800000011920929D;
        this.motionY *= 0.800000011920929D;
        this.motionZ *= 0.800000011920929D;
        this.motionY = (double) (this.rand.nextFloat() * 0.1F + 0.1F);
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        canCollide = false;
        this.particleScale *= this.rand.nextFloat() * 0.1F + 0.8F;
        this.maxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
        type = worldIn.rand.nextBoolean();
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        if (age > this.getMaxAge()) {
            this.setExpired();
        }

        Vector3d vec3d = renderInfo.getProjectedView();
        float f = (float)(MathHelper.lerp((double)partialTicks, this.prevPosX, this.posX) - vec3d.getX());
        float f1 = (float)(MathHelper.lerp((double)partialTicks, this.prevPosY, this.posY) - vec3d.getY());
        float f2 = (float)(MathHelper.lerp((double)partialTicks, this.prevPosZ, this.posZ) - vec3d.getZ());
        Quaternion quaternion;
        if (this.particleAngle == 0.0F) {
            quaternion = renderInfo.getRotation();
        } else {
            quaternion = new Quaternion(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
            quaternion.multiply(Vector3f.ZP.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getScale(partialTicks);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        if (type) {
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_0);
        } else {
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_1);
        }
        int j = this.getBrightnessForRender(partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        IVertexBuilder vertexbuffer = tessellator.getBuffer().getVertexBuilder();
        BufferBuilder buffer2 = tessellator.getBuffer();
        buffer2.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        vertexbuffer.pos((double)avector3f[0].getX(), (double)avector3f[0].getY(), (double)avector3f[0].getZ()).tex(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        vertexbuffer.pos((double)avector3f[1].getX(), (double)avector3f[1].getY(), (double)avector3f[1].getZ()).tex(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        vertexbuffer.pos((double)avector3f[2].getX(), (double)avector3f[2].getY(), (double)avector3f[2].getZ()).tex(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        vertexbuffer.pos((double)avector3f[3].getX(), (double)avector3f[3].getY(), (double)avector3f[3].getZ()).tex(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        RenderSystem.disableBlend();
        Tessellator.getInstance().draw();
        RenderSystem.disableBlend();
    }

    public void setParticleTextureIndex(int particleTextureIndex) {
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    public void tick() {
        super.tick();
        this.motionY += 0.0001D;
        this.motionX *= 0.6D;
        this.motionY *= 0.9990000128746033D;
        this.motionZ *= 0.6D;
        Block block = this.world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getBlock();
        if (block != AstroBlockRegistry.SQUID_TANK && block != AstroBlockRegistry.SQUID_TANK_GLASS) {
            this.setExpired();
        }

    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        public Factory() {
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleSquidBubble(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}