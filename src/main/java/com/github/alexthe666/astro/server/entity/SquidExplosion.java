package com.github.alexthe666.astro.server.entity;

import com.github.alexthe666.astro.AstronauticalConfig;
import com.github.alexthe666.astro.server.block.AstroBlockRegistry;
import com.github.alexthe666.astro.server.block.BlockSquidMucus;
import com.github.alexthe666.astro.server.misc.DamageSourceSquid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.*;

public class SquidExplosion extends Explosion {
    private final boolean causesFire;
    private final Explosion.Mode mode;
    private final Random random = new Random();
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity exploder;
    private final float size;
    private final List<BlockPos> affectedBlockPositions = Lists.newArrayList();
    private final Map<PlayerEntity, Vector3d> playerKnockbackMap = Maps.newHashMap();
    private final Vector3d position;
    private DamageSource damageSource;

    @OnlyIn(Dist.CLIENT)
    public SquidExplosion(World worldIn, @Nullable Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, false, Explosion.Mode.DESTROY, affectedPositions);
    }

    @OnlyIn(Dist.CLIENT)
    public SquidExplosion(World worldIn, @Nullable Entity exploderIn, double xIn, double yIn, double zIn, float sizeIn, boolean causesFireIn, Explosion.Mode modeIn, List<BlockPos> affectedBlockPositionsIn) {
        this(worldIn, exploderIn, xIn, yIn, zIn, sizeIn, causesFireIn, modeIn);
        this.affectedBlockPositions.addAll(affectedBlockPositionsIn);
    }

    public SquidExplosion(World worldIn, @Nullable Entity exploderIn, double xIn, double yIn, double zIn, float sizeIn, boolean causesFireIn, Explosion.Mode modeIn) {
        super(worldIn, exploderIn, xIn, yIn, zIn, sizeIn, causesFireIn, modeIn);
        this.world = worldIn;
        this.exploder = exploderIn;
        this.size = sizeIn;
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
        this.causesFire = causesFireIn;
        this.mode = modeIn;
        this.damageSource = DamageSourceSquid.causeSquidDamage(this);
        this.position = new Vector3d(this.x, this.y, this.z);
    }

    public static float getBlockDensity(Vector3d p_222259_0_, Entity p_222259_1_) {
        AxisAlignedBB axisalignedbb = p_222259_1_.getBoundingBox();
        double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                        double d5 = MathHelper.lerp(f, axisalignedbb.minX, axisalignedbb.maxX);
                        double d6 = MathHelper.lerp(f1, axisalignedbb.minY, axisalignedbb.maxY);
                        double d7 = MathHelper.lerp(f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                        Vector3d Vector3d = new Vector3d(d5 + d3, d6, d7 + d4);
                        if (p_222259_1_.world.rayTraceBlocks(new RayTraceContext(Vector3d, p_222259_0_, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, p_222259_1_)).getType() == RayTraceResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    private static void func_229976_a_(ObjectArrayList<Pair<ItemStack, BlockPos>> p_229976_0_, ItemStack p_229976_1_, BlockPos p_229976_2_) {
        int i = p_229976_0_.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = p_229976_0_.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.func_226532_a_(itemstack, p_229976_1_)) {
                ItemStack itemstack1 = ItemEntity.func_226533_a_(itemstack, p_229976_1_, 16);
                p_229976_0_.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (p_229976_1_.isEmpty()) {
                    return;
                }
            }
        }

        p_229976_0_.add(Pair.of(p_229976_1_, p_229976_2_));
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void doExplosionA() {
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BlockState blockstate = this.world.getBlockState(blockpos);
                            FluidState ifluidstate = this.world.getFluidState(blockpos);
                            if(blockstate.isAir() || ifluidstate.isTagged(FluidTags.WATER)){
                                set.add(blockpos);
                            }else{
                                if ((!blockstate.isAir(this.world, blockpos) || !ifluidstate.isEmpty())) {
                                    float f2 = Math.max(blockstate.getExplosionResistance(this.world, blockpos, this), 0);
                                    if (this.exploder != null) {
                                        f2 = this.exploder.getExplosionResistance(this, this.world, blockpos, blockstate, ifluidstate, f2);
                                    }
                                    f -= (f2 + 0.3F) * 0.3F;
                                }
                                if ((f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, blockstate, f))) && canBeDestroyedBySquid(blockstate)) {
                                    set.add(blockpos);
                                }
                            }


                            d4 += d0 * (double) 0.3F;
                            d6 += d1 * (double) 0.3F;
                            d8 += d2 * (double) 0.3F;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(set);
        float f3 = this.size * 2.0F;
        int k1 = MathHelper.floor(this.x - (double) f3 - 1.0D);
        int l1 = MathHelper.floor(this.x + (double) f3 + 1.0D);
        int i2 = MathHelper.floor(this.y - (double) f3 - 1.0D);
        int i1 = MathHelper.floor(this.y + (double) f3 + 1.0D);
        int j2 = MathHelper.floor(this.z - (double) f3 - 1.0D);
        int j1 = MathHelper.floor(this.z + (double) f3 + 1.0D);
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);
        Vector3d Vector3d = new Vector3d(this.x, this.y, this.z);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);
            double d12 = MathHelper.sqrt(entity.getDistanceSq(Vector3d)) / f3;
            if (d12 <= 1.0D) {
                double d5 = entity.getPosX() - this.x;
                double d7 = entity.getPosYEye() - this.y;
                double d9 = entity.getPosZ() - this.z;
                double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                if (d13 != 0.0D) {
                    d5 = d5 / d13;
                    d7 = d7 / d13;
                    d9 = d9 / d13;
                    double d14 = getBlockDensity(Vector3d, entity);
                    double d10 = (1.0D - d12) * d14;
                    entity.attackEntityFrom(this.getDamageSource(), (float) ((int) ((d10 * d10 + d10) / 2.0D * 14.0D * (double) f3 + 1.0D)));
                    double d11 = d10;
                    if (entity instanceof LivingEntity) {
                        d11 = ProtectionEnchantment.getBlastDamageReduction((LivingEntity) entity, d10);
                    }

                    entity.setMotion(entity.getMotion().add(d5 * d11, d7 * d11, d9 * d11));
                    if (entity instanceof PlayerEntity) {
                        PlayerEntity playerentity = (PlayerEntity) entity;
                        if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.isFlying)) {
                            this.playerKnockbackMap.put(playerentity, new Vector3d(d5 * d10, d7 * d10, d9 * d10));
                        }
                    }
                }
            }
        }

    }

    private boolean canBeDestroyedBySquid(BlockState state) {
        return (BlockTags.LEAVES.func_230235_a_(state.getBlock()) || BlockTags.LOGS.func_230235_a_(state.getBlock()) || state.getBlock() == Blocks.GRASS_BLOCK) && AstronauticalConfig.squidfallGriefing
                || state.isAir() || state.getFluidState().isTagged(FluidTags.WATER);
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean spawnParticles) {
        if (this.world.isRemote) {
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean flag = this.mode != Explosion.Mode.NONE;
        if (spawnParticles) {
            if (!(this.size < 2.0F) && flag) {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            }
        }

        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            Collections.shuffle(this.affectedBlockPositions, this.world.rand);
            for (BlockPos blockpos : this.affectedBlockPositions) {
                BlockState blockstate = this.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if(block != Blocks.GRASS && block != Blocks.DIRT) {
                    if (!blockstate.isAir(this.world, blockpos) && blockstate.getFluidState().isEmpty()) {
                        BlockPos blockpos1 = blockpos.toImmutable();
                        this.world.getProfiler().startSection("explosion_blocks");
                        if (blockstate.canDropFromExplosion(this.world, blockpos, this) && this.world instanceof ServerWorld) {
                            TileEntity tileentity = blockstate.hasTileEntity() ? this.world.getTileEntity(blockpos) : null;
                            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.world)).withRandom(this.world.rand).withParameter(LootParameters.POSITION, blockpos).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withNullableParameter(LootParameters.BLOCK_ENTITY, tileentity).withNullableParameter(LootParameters.THIS_ENTITY, this.exploder);
                            if (this.mode == Explosion.Mode.DESTROY) {
                                lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, this.size);
                            }

                            blockstate.getDrops(lootcontext$builder).forEach((p_229977_2_) -> {
                                func_229976_a_(objectarraylist, p_229977_2_, blockpos1);
                            });
                        }
                        blockstate.onBlockExploded(this.world, blockpos, this);

                        this.world.getProfiler().endSection();
                    }
                }
            }

            for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.spawnAsEntity(this.world, pair.getSecond(), pair.getFirst());
            }
        }

        for (BlockPos blockpos2 : this.affectedBlockPositions) {
            FluidState ifluidstate = this.world.getFluidState(blockpos2);
            boolean water  = ifluidstate.isTagged(FluidTags.WATER);
            if(water && random.nextInt(8) != 0){
                continue;
            }
            if (AstronauticalConfig.squidfallGoop && this.random.nextInt(19) == 0 && this.world.getBlockState(blockpos2).isAir() || water) {
                List<Direction> directions = new ArrayList<>();
                if (world.getBlockState(blockpos2.offset(Direction.NORTH)).isSolidSide(world, blockpos2.offset(Direction.SOUTH), Direction.SOUTH)) {
                    directions.add(Direction.SOUTH);
                }
                if (world.getBlockState(blockpos2.offset(Direction.SOUTH)).isSolidSide(world, blockpos2.offset(Direction.NORTH), Direction.NORTH)) {
                    directions.add(Direction.NORTH);
                }
                if (world.getBlockState(blockpos2.offset(Direction.EAST)).isSolidSide(world, blockpos2.offset(Direction.WEST), Direction.WEST)) {
                    directions.add(Direction.WEST);
                }
                if (world.getBlockState(blockpos2.offset(Direction.WEST)).isSolidSide(world, blockpos2.offset(Direction.EAST), Direction.EAST)) {
                    directions.add(Direction.EAST);
                }
                if (world.getBlockState(blockpos2.offset(Direction.UP)).isSolidSide(world, blockpos2.offset(Direction.DOWN), Direction.DOWN)) {
                    directions.add(Direction.DOWN);
                }
                if (world.getBlockState(blockpos2.offset(Direction.DOWN)).isSolidSide(world, blockpos2.offset(Direction.UP), Direction.UP)) {
                    directions.add(Direction.UP);
                }
                if (!directions.isEmpty()) {
                    BlockState mucusState = AstroBlockRegistry.SQUID_MUCUS.getDefaultState().with(BlockSquidMucus.FACING, directions.size() > 1 ? directions.get(random.nextInt(directions.size() - 1)) : directions.get(0));
                    System.out.println(ifluidstate);
                    this.world.setBlockState(blockpos2, mucusState.with(BlockSquidMucus.WATERLOGGED, ifluidstate.isTagged(FluidTags.WATER)), 3);
                }
            }
        }

    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public void setDamageSource(DamageSource damageSourceIn) {
        this.damageSource = damageSourceIn;
    }

    public Map<PlayerEntity, Vector3d> getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    @Nullable
    public LivingEntity getExplosivePlacedBy() {
        if (this.exploder == null) {
            return null;
        } else if (this.exploder instanceof TNTEntity) {
            return ((TNTEntity) this.exploder).getTntPlacedBy();
        } else if (this.exploder instanceof LivingEntity) {
            return (LivingEntity) this.exploder;
        } else {
            return this.exploder instanceof DamagingProjectileEntity ? (LivingEntity) ((DamagingProjectileEntity) this.exploder).func_234616_v_() : null;
        }
    }

    public void clearAffectedBlockPositions() {
        this.affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }

    public Vector3d getPosition() {
        return this.position;
    }
}