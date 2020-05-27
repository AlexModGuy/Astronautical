package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.server.item.AstroItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityFallingStar extends ProjectileItemEntity {

    public EntityFallingStar(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityFallingStar(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(AstroEntityRegistry.FALLING_STAR, worldIn);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return AstroItemRegistry.FALLING_STAR_RENDER;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        this.remove();
        ItemEntity item = new ItemEntity(world, result.getHitVec().x, result.getHitVec().y, result.getHitVec().z, new ItemStack(AstroItemRegistry.FALLING_STAR));
        world.addEntity(item);
    }

    public void tick() {
        super.tick();
        float f = -MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F));
        this.setMotion(this.getMotion().add(f * 0.01F, 0, f2 * 0.01F));
    }
}
