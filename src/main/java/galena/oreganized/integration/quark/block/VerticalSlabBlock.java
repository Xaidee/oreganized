package galena.oreganized.integration.quark.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vazkii.quark.content.building.block.VerticalSlabBlock.VerticalSlabType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VerticalSlabBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<VerticalSlabType> TYPE = EnumProperty.create("type", VerticalSlabType.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public VerticalSlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, VerticalSlabType.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(TYPE) != VerticalSlabType.DOUBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(TYPE).shape;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(pos);
        if (state.getBlock() == this)
            return state.setValue(TYPE, VerticalSlabType.DOUBLE).setValue(WATERLOGGED, false);

        return this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(pos).is(Fluids.WATER)).setValue(TYPE, VerticalSlabType.fromDirection(this.getDirectionForPlacement(context)));
    }

    private Direction getDirectionForPlacement(BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        if (face.getAxis() != Direction.Axis.Y) return face;
        Vec3 difference = context.getClickLocation().subtract(Vec3.atLowerCornerOf(context.getClickedPos())).subtract(0.5, 0, 0.5);
        return Direction.fromYRot(-Math.toDegrees(Math.atan2(difference.x, difference.z))).getOpposite();
    }

    @Override
    public boolean canBeReplaced(BlockState state, @Nonnull BlockPlaceContext context) {
        VerticalSlabType slabType = state.getValue(TYPE);
        return slabType != VerticalSlabType.DOUBLE && context.getItemInHand().getItem() == this.asItem() && context.replacingClickedOnBlock() && (context.getClickedFace() == slabType.direction && this.getDirectionForPlacement(context) == slabType.direction);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluid) {
        return state.getValue(TYPE) != VerticalSlabType.DOUBLE && SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, fluid);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return state.getValue(TYPE) != VerticalSlabType.DOUBLE && SimpleWaterloggedBlock.super.canPlaceLiquid(world, pos, state, fluid);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType path) {
        return path == PathComputationType.WATER && world.getFluidState(pos).is(FluidTags.WATER);
    }
}
