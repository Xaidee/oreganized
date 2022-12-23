package galena.oreganized.content.block;

import galena.oreganized.content.ISilver;
import galena.oreganized.content.entity.ExposerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ExposerBlock extends DirectionalBlock implements ISilver, EntityBlock {

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 7);
    public static final int TexturedFrames = 4;


    public ExposerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExposerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> entityType) {
        return ExposerBlockEntity::tick;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        int dist = getUndeadDistance(world, pos, null, TexturedFrames);
        world.setBlockAndUpdate(pos, state.setValue(LEVEL, LEVEL.getPossibleValues().size() - dist));
        world.scheduleTick(pos, state.getBlock(), 1);
        this.updateNeighborsInFront(world, pos, state);
    }

    protected void updateNeighborsInFront(Level worldIn, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite().getNormal());
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.updateNeighborsAtExceptFromFacing(blockpos, this, direction);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getSignal(world, pos, direction);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        if (state.getValue(FACING) == direction.getOpposite()) return 0;
        return state.getValue(LEVEL) * 2;
    }
}
