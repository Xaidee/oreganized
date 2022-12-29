package galena.oreganized.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.Nullable;

public class CrystalGlassPaneBlock extends StainedGlassPaneBlock implements ICrystalGlass {

    public CrystalGlassPaneBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(color, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getPlayer() != null;
        return super.getStateForPlacement(context).setValue(TYPE, flag && context.getPlayer().isCrouching() ? ROTATED : NORMAL);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState adjState, LevelAccessor world, BlockPos pos, BlockPos adjPos) {
        Block centerBlock = state.getBlock();
        Block aboveBlock = world.getBlockState(pos.above()).getBlock();
        Block twoAboveBlock = world.getBlockState(pos.above(2)).getBlock();
        Block belowBlock = world.getBlockState(pos.below()).getBlock();
        Block twoBelowBlock = world.getBlockState(pos.below(2)).getBlock();

        if (direction == Direction.DOWN || direction == Direction.UP) {
            if (centerBlock == aboveBlock && centerBlock == belowBlock) {
                updatePattern(pos, world);
            } else if (centerBlock == twoAboveBlock && centerBlock == aboveBlock && centerBlock != belowBlock) {
                updatePattern(pos.above(), world);
            } else if (centerBlock == twoBelowBlock && centerBlock == belowBlock && centerBlock != aboveBlock) {
                updatePattern(pos.below(), world);
            }
            if (state.getValue(TYPE) == OUTER || state.getValue(TYPE) == INNER) {
                if (centerBlock != adjState.getBlock()) {
                    return state.setValue(TYPE, ROTATED);
                }
            }
        }
        return super.updateShape(state, direction, adjState, world, pos, adjPos);
    }
}
