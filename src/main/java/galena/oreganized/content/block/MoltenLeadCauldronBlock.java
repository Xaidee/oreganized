package galena.oreganized.content.block;

import galena.oreganized.OreganizedConfig;
import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OItems;
import galena.oreganized.index.OTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class MoltenLeadCauldronBlock extends AbstractCauldronBlock implements CauldronInteraction {

    public static final Map<Item, CauldronInteraction> INTERACTION_MAP = CauldronInteraction.newInteractionMap();

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public static final CauldronInteraction FILL_MOLTEN_LEAD = (state, world, pos, player, hand, stack) ->
            CauldronInteraction.emptyBucket(world, pos, player, hand, stack, OBlocks.MOLTEN_LEAD_CAULDRON.get().defaultBlockState().setValue(AGE, 3), SoundEvents.BUCKET_EMPTY_LAVA);

    public static final CauldronInteraction EMPTY_MOLTEN_LEAD = (state, world, pos, player, hand, stack) ->
            CauldronInteraction.fillBucket(state, world, pos, player, hand, stack, new ItemStack(OItems.MOLTEN_LEAD_BUCKET.get()), blockState -> state.getValue(AGE).equals(3), SoundEvents.BUCKET_FILL_LAVA);

    public static final CauldronInteraction FILL_LEAD_BLOCK = (state, world, pos, player, hand, stack) ->
            placeBlock(world, pos, player, hand, stack, OBlocks.MOLTEN_LEAD_CAULDRON.get().defaultBlockState().setValue(AGE, 0), SoundEvents.METAL_PLACE);

    public static final CauldronInteraction EMPTY_LEAD_BLOCK = (state, world, pos, player, hand, stack) ->
            dropResource(state, world, pos, player, hand, stack, new ItemStack(OBlocks.LEAD_BLOCK.get()), blockState -> state.getValue(AGE).equals(0), SoundEvents.ITEM_FRAME_REMOVE_ITEM);

    public MoltenLeadCauldronBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(moltenStageEmission()), INTERACTION_MAP);
        registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(Items.CAULDRON);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    protected double getContentHeight(BlockState state) {
        return 0.9375D;
    }

    public static ToIntFunction<BlockState> moltenStageEmission() {
        return (state) -> state.getValue(AGE) * 2;
    }

    public boolean isFull(BlockState state) {
        return true;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        if (!state.getValue(AGE).equals(3)) return Shapes.block();
        return super.getShape(state, world, pos, ctx);
    }

    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (this.isEntityInsideContent(state, blockPos, entity)) {
            entity.setSecondsOnFire(10);
        }
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos blockPos) {
        return state.getValue(AGE) + 1;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.tick(state, world, pos, random);
    }

        @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        int max_age = AGE.getPossibleValues().size() - 1;
        int age = state.getValue(AGE);
        if (age < max_age && random.nextInt(1) == 0) {
            BlockState below = world.getBlockState(pos.below());
            if ((below.is(OTags.Blocks.FIRE_SOURCE) || below.getFluidState().is(FluidTags.LAVA)) && OreganizedConfig.COMMON.cauldronLeadMelting.get()) {
                world.setBlockAndUpdate(pos, state.setValue(AGE, age + 1));
                return;
            }
            world.setBlockAndUpdate(pos, state.setValue(AGE, Mth.clamp(age - 1, 0, max_age)));
        }
    }

    @Override
    public @NotNull InteractionResult interact(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        ItemStack itemstack = player.getItemInHand(hand);
        CauldronInteraction cauldroninteraction = INTERACTION_MAP.get(itemstack.getItem());
        return cauldroninteraction.interact(state, world, pos, player, hand, itemstack);
    }

    static InteractionResult placeBlock(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState state, SoundEvent sound) {
        if (!world.isClientSide) {
            Item item = stack.getItem();
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            if (!player.getAbilities().instabuild) stack.shrink(1);
            world.setBlockAndUpdate(pos, state);
            world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
        }

        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    static InteractionResult dropResource(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack usedStack, ItemStack droppedStack, Predicate<BlockState> stateCondition, SoundEvent sound) {
        if (!stateCondition.test(state))
            return InteractionResult.PASS;
        if (!world.isClientSide) {
            Item item = usedStack.getItem();
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            popResource(world, pos, droppedStack);
            world.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
            world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        return InteractionResult.sidedSuccess(world.isClientSide);
    }
}
