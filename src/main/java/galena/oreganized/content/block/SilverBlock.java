package galena.oreganized.content.block;

import galena.oreganized.content.ISilver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Random;

public class SilverBlock extends Block implements ISilver {
    public static final IntegerProperty LEVEL = BlockStateProperties.AGE_7;

    public static final int TexturedFrames = LEVEL.getPossibleValues().size();
    public SilverBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, 7));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
        definition.add(LEVEL);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        int dist = getUndeadDistance(world, pos, null, TexturedFrames);
        world.setBlockAndUpdate(pos, state.setValue(LEVEL, dist - 1));
        world.scheduleTick(pos, state.getBlock(), 1);
    }
}
