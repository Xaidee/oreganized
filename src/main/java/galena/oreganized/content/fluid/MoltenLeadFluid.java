package galena.oreganized.content.fluid;

import galena.oreganized.index.OParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class MoltenLeadFluid  extends ForgeFlowingFluid {

    public static BooleanProperty MOVING = BooleanProperty.create("moving");


    public MoltenLeadFluid(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(LEVEL, 8).setValue(MOVING, false));
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL).add(MOVING);
    }

    @Override
    public int getAmount(FluidState state) {
        return 8;
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    protected void spread(LevelAccessor world, BlockPos blockPos, FluidState fluidState) {
        if (!fluidState.isEmpty()) {
            if (fluidState.getValue(MOVING)) {
                BlockPos belowPos = blockPos.below();
                BlockState belowState = world.getBlockState(belowPos);
                if (this.canSpreadTo(world, belowPos, belowState, Direction.DOWN, belowPos, belowState, world.getFluidState(belowPos), fluidState.getType())) {
                    this.spreadTo(world, belowPos, belowState, Direction.DOWN, fluidState);
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                }
            } else {
                world.setBlock(blockPos, fluidState.setValue(MOVING, true).createLegacyBlock(), 3);
            }
        }
    }

    @Override
    public boolean canBeReplacedWith(FluidState p_76233_, BlockGetter p_76234_, BlockPos p_76235_, Fluid p_76236_, Direction p_76237_) {
        return false;
    }

    @Override
    public void animateTick(Level world, BlockPos blockPos, FluidState fluidState, Random random) {
        BlockPos abovePos = blockPos.above();
        if (world.getBlockState(abovePos).isAir() && !world.getBlockState(abovePos).isSolidRender(world, abovePos)) {
            if (random.nextInt(300) == 0) {
                world.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }
    }

    @Override
    @Nullable
    public ParticleOptions getDripParticle() {
        return OParticleTypes.DRIPPING_LEAD.get();
    }
}
