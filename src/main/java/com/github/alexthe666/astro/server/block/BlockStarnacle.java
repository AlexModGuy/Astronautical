package com.github.alexthe666.astro.server.block;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockStarnacle extends DirectionalBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 4);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape TOP_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16D, 3D, 16.0D);
    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0.0D, 13.0D, 0.0D, 16D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

    public BlockStarnacle() {
        super(Properties.create(Material.OCEAN_PLANT).sound(SoundType.STONE).doesNotBlockMovement().variableOpacity().notSolid().hardnessAndResistance(0.8F, 0.0F).tickRandomly());
        this.setRegistryName(Astronautical.MODID, "starnacle");
        this.setDefaultState(this.stateContainer.getBaseState().with(COUNT, Integer.valueOf(1)).with(FACING, Direction.UP).with(WATERLOGGED, Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(FACING);
        switch(direction) {
            case EAST:
            default:
                return EAST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            case NORTH:
                return NORTH_AABB;
            case UP:
                return TOP_AABB;
            case DOWN:
                return DOWN_AABB;
        }
    }


    public int tickRate(IWorldReader worldIn) {
        return 1;
    }

    @Deprecated
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(worldIn instanceof ServerWorld){
            tick(state, (ServerWorld) worldIn, pos, new Random());
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }


    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        BlockPos offset = pos.offset(state.get(FACING).getOpposite());
        if(!worldIn.getBlockState(offset).isSolidSide(worldIn, offset, state.get(FACING))){
            worldIn.destroyBlock(pos, true);
        }

    }

    @Deprecated
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        BlockPos offset = currentPos.offset(stateIn.get(FACING).getOpposite());
        if(!worldIn.getBlockState(offset).isSolidSide(worldIn, offset, stateIn.get(FACING))){
            worldIn.destroyBlock(currentPos, true);
        }
        return stateIn;
    }


    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.with(FACING, mirrorIn.mirror(state.get(FACING)));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getPos());
        if (blockstate.getBlock() == this) {
            return blockstate.with(COUNT, Integer.valueOf(Math.min(4, blockstate.get(COUNT) + 1)));
        } else {
            IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
            boolean flag = ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8;
            Direction direction = context.getFace();
            return super.getStateForPlacement(context).with(WATERLOGGED, Boolean.valueOf(flag)).with(FACING, direction).with(WATERLOGGED, Boolean.valueOf(ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8));
        }
    }

    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return useContext.getItem().getItem() == this.asItem() && state.get(COUNT) < 4 ? true : super.isReplaceable(state, useContext);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(COUNT, FACING, WATERLOGGED);
    }

    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

}
