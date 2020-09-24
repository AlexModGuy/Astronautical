package com.github.alexthe666.astro.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.IRenderHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class CosmicSkyRenderer implements IRenderHandler {

    private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation("astro:textures/environment/cosmic_sky.png");
    private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation EARTH_TEXTURES = new ResourceLocation("astro:textures/environment/earth.png");
    private static final ResourceLocation EARTH_CLOUD_TEXTURES = new ResourceLocation("astro:textures/environment/earth_clouds.png");
    private static final ResourceLocation STAR_TEXTURES = new ResourceLocation("astro:textures/environment/stars_twinkle.png");
    private static final int[] POSSIBLE_STAR_COLORS = new int[]{
            0XFFFFFF, 0XFFCECE, 0XC9FFFF, 0XFFF6CC
    };
    private static final int STAR_TWINKLE_FRAMES = 8;
    private static int STAR_TYPE_COUNT = 8;
    private static int STAR_TEXTURE_SIZE_PX = 256;
    private final VertexFormat skyVertexFormat = DefaultVertexFormats.POSITION_COLOR_TEX;
    private boolean hasGeneratedStars = false;
    @Nullable
    private VertexBuffer[] starVBO;

    public CosmicSkyRenderer() {
    }

    private void generateStars() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.starVBO = new VertexBuffer[STAR_TWINKLE_FRAMES];
        hasGeneratedStars = true;
        for (int i = 0; i < STAR_TWINKLE_FRAMES; i++) {
            if (this.starVBO[i] != null) {
                this.starVBO[i].close();
            }
            this.starVBO[i] = new VertexBuffer(this.skyVertexFormat);
            this.renderStars(bufferbuilder, i);
            bufferbuilder.finishDrawing();
            this.starVBO[i].upload(bufferbuilder);
        }
    }

    private void renderStars(BufferBuilder bufferBuilderIn, int starFrame) {
        Random random = new Random(2L);
        bufferBuilderIn.begin(1, DefaultVertexFormats.POSITION_COLOR_TEX);
        int starColorsCount = POSSIBLE_STAR_COLORS.length;
        int starCount = 3000;

        for (int i = 0; i < starCount; ++i) {
            int starStartFrame = random.nextInt(STAR_TWINKLE_FRAMES);
            int actualStarFame = (starStartFrame + starFrame) % STAR_TWINKLE_FRAMES;
            double d0 = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.15F + random.nextFloat() * 0.1F;
            int color = POSSIBLE_STAR_COLORS[random.nextInt(starColorsCount)];
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0D && d4 > 0.1D) {
                int texture = random.nextInt(STAR_TYPE_COUNT);
                float randomScale = random.nextFloat() * 10 + 10;
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);
                int starBrightness = 100 + random.nextInt(155);
                if (Math.abs(d6) < 99) {
                    for (int j = 0; j < 4; ++j) {
                        double d17 = 0.0D;
                        double d18 = (double) ((j & 2) - 1) * d3;
                        double d19 = (double) ((j + 1 & 2) - 1) * d3;
                        double d20 = 0.0D;
                        double d21 = d18 * d16 - d19 * d15;
                        double d22 = d19 * d16 + d18 * d15;
                        double d23 = d21 * d12 + 0.0D * d13;
                        double d24 = 0.0D * d12 - d21 * d13;
                        double d25 = d24 * d9 - d22 * d10;
                        double d26 = d22 * d9 + d24 * d10;
                        float u = 0;
                        float v = 0;
                        float textureOffset = texture * 32F / STAR_TEXTURE_SIZE_PX;
                        float vStart = actualStarFame * 32F / STAR_TEXTURE_SIZE_PX;
                        if (j == 0) {
                            u = textureOffset;
                            v = vStart;
                        }
                        if (j == 1) {
                            u = textureOffset;
                            v = vStart + 32F / STAR_TEXTURE_SIZE_PX;
                        }
                        if (j == 2) {
                            u = textureOffset + 32F / STAR_TEXTURE_SIZE_PX;
                            v = vStart + 32F / STAR_TEXTURE_SIZE_PX;
                        }
                        if (j == 3) {
                            u = textureOffset + 32F / STAR_TEXTURE_SIZE_PX;
                            v = vStart;
                        }
                        bufferBuilderIn.pos(d5 + d25 * randomScale, d6 + d23 * randomScale, d7 + d26 * randomScale).color(r, g, b, starBrightness).tex(u, v).endVertex();
                    }
                }
            }
        }

    }

    public void renderSky(MatrixStack matrixStackIn, float partialTicks) {
        float celestialAngle = 0.0F;
        if (!hasGeneratedStars) {
            generateStars();
        }
        ActiveRenderInfo info = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(info.getPitch()));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(info.getYaw() + 180.0F));
        matrixStackIn.push();
        matrixStackIn.push();

        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int i = 0; i < 6; ++i) {
            ResourceLocation texture = END_SKY_TEXTURES;

            matrixStackIn.push();
            if (i == 1) {
                //texture = SKYBOX_TEXTURE_NORTH;
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
                //matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
            }

            if (i == 2) {
                //texture = SKYBOX_TEXTURE_SOUTH;
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90.0F));
                //matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
            }

            if (i == 3) {
                //texture = SKYBOX_TEXTURE_UP;
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
                //matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
            }

            if (i == 4) {
                //texture = SKYBOX_TEXTURE_WEST;
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
                //matrixStackIn.rotate(Vector3f.YP.rotationDegrees(0.0F));
            }

            if (i == 5) {
                //texture = SKYBOX_TEXTURE_EAST;
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-90.0F));
            }
            Minecraft.getInstance().textureManager.bindTexture(texture);

            Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(matrix4f, -100.0F, -100.0F, -100.0F).tex(0.0F, 0.0F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(matrix4f, -100.0F, -100.0F, 100.0F).tex(0.0F, 128.0F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(matrix4f, 100.0F, -100.0F, 100.0F).tex(128.0F, 128.0F).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(matrix4f, 100.0F, -100.0F, -100.0F).tex(128.0F, 0.0F).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            matrixStackIn.pop();
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();


        ClientWorld world = Minecraft.getInstance().world;
        RenderSystem.disableTexture();
        Vector3d Vector3d = world.getSkyColor(Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
        float f = (float) Vector3d.x;
        float f1 = (float) Vector3d.y;
        float f2 = (float) Vector3d.z;
        FogRenderer.applyFog();
        RenderSystem.depthMask(false);

        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        float[] afloat = world.func_239132_a_().func_230492_a_(world.func_242415_f(partialTicks), partialTicks);
        if (afloat != null) {
            RenderSystem.disableTexture();
            RenderSystem.shadeModel(7425);
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            float f3 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F;
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f3));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
            float f4 = afloat[0];
            float f5 = afloat[1];
            float f6 = afloat[2];
            Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
            bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();
            int i = 16;

            for (int j = 0; j <= 16; ++j) {
                float f7 = (float) j * ((float) Math.PI * 2F) / 16.0F;
                float f8 = MathHelper.sin(f7);
                float f9 = MathHelper.cos(f7);
                bufferbuilder.pos(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
            }

            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            matrixStackIn.pop();
            RenderSystem.shadeModel(7424);
        }

        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        matrixStackIn.push();
        float f11 = 1.0F - world.getRainStrength(partialTicks);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, f11);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(celestialAngle * 360.0F));
        Matrix4f matrix4f1 = matrixStackIn.getLast().getMatrix();

        float f12 = 20.0F;
        Minecraft.getInstance().textureManager.bindTexture(SUN_TEXTURES);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(matrix4f1, -f12, 100.0F, -f12).tex(0.0F, 0.0F).endVertex();
        bufferbuilder.pos(matrix4f1, f12, 100.0F, -f12).tex(1.0F, 0.0F).endVertex();
        bufferbuilder.pos(matrix4f1, f12, 100.0F, f12).tex(1.0F, 1.0F).endVertex();
        bufferbuilder.pos(matrix4f1, -f12, 100.0F, f12).tex(0.0F, 1.0F).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        f12 = 10.0F;
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
        Minecraft.getInstance().textureManager.bindTexture(EARTH_TEXTURES);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(matrix4f1, -f12, 100.0F, -f12).tex(0.0F, 0.0F).endVertex();
        bufferbuilder.pos(matrix4f1, f12, 100.0F, -f12).tex(1.0F, 0.0F).endVertex();
        bufferbuilder.pos(matrix4f1, f12, 100.0F, f12).tex(1.0F, 1.0F).endVertex();
        bufferbuilder.pos(matrix4f1, -f12, 100.0F, f12).tex(0.0F, 1.0F).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        float cloudMovement = (partialTicks + Minecraft.getInstance().player.ticksExisted) * 0.001F;
        Minecraft.getInstance().textureManager.bindTexture(EARTH_CLOUD_TEXTURES);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(matrix4f1, -f12, 99.0F, -f12).tex(0.0F + cloudMovement, 0.0F + cloudMovement).endVertex();
        bufferbuilder.pos(matrix4f1, f12, 99.0F, -f12).tex(0.25F + cloudMovement, 0.0F + cloudMovement).endVertex();
        bufferbuilder.pos(matrix4f1, f12, 99.0F, f12).tex(0.25F + cloudMovement, 0.25F + cloudMovement).endVertex();
        bufferbuilder.pos(matrix4f1, -f12, 99.0F, f12).tex(0.0F + cloudMovement, 0.25F + cloudMovement).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);

        Minecraft.getInstance().textureManager.bindTexture(STAR_TEXTURES);
        float f10 = 0.5F;
        int starVBOTick1 = (int) ((Minecraft.getInstance().player.ticksExisted + partialTicks) * f10 % STAR_TWINKLE_FRAMES);
        int starVBOTick2 = MathHelper.clamp(starVBOTick1, 0, STAR_TWINKLE_FRAMES - 1);
        this.starVBO[starVBOTick2].bindBuffer();
        this.skyVertexFormat.setupBufferState(0L);
        this.starVBO[starVBOTick2].draw(matrixStackIn.getLast().getMatrix(), 7);
        RenderSystem.disableBlend();
        VertexBuffer.unbindBuffer();
        this.skyVertexFormat.clearBufferState();


        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableFog();
        matrixStackIn.pop();
        RenderSystem.disableTexture();
        RenderSystem.color3f(0.0F, 0.0F, 0.0F);

        if (world.func_239132_a_().func_239216_b_()) {
            RenderSystem.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
        } else {
            RenderSystem.color3f(f, f1, f2);
        }

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();

        matrixStackIn.pop();
        matrixStackIn.pop();
    }

    @Override
    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc) {
        renderSky(new MatrixStack(), Minecraft.getInstance().getRenderPartialTicks());
    }
}
