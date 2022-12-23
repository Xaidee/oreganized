package galena.oreganized.content.block;

import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OEffects;
import galena.oreganized.index.OItems;
import galena.oreganized.index.OTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MoltenLeadBlock extends LiquidBlock {

    private static final BooleanProperty MOVING = BooleanProperty.create( "moving" );

    public static final VoxelShape STABLE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public MoltenLeadBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties.noCollission().strength(-1.0F, 3600000.0F).noDrops().lightLevel((state) -> 8));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder <Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MOVING);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState neighbour, LevelAccessor pLevel, BlockPos pPos, BlockPos neighbourPos) {
        if(pDirection == Direction.DOWN){
            pLevel.scheduleTick( pPos , this , 30 );
        }
        return super.updateShape( pState , pDirection , neighbour , pLevel , pPos , neighbourPos );
    }

    @Nullable
    @Override
    public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return BlockPathTypes.WALKABLE;
    }

    @Override
    public boolean canEntityDestroy( BlockState state , BlockGetter world , BlockPos pos , Entity entity ){
        return false;
    }

    @Override
    public void onPlace( BlockState pState , Level pLevel , BlockPos pPos , BlockState pOldState , boolean pIsMoving ){
        if (pIsMoving){
            if(!pOldState.getFluidState().is( FluidTags.WATER )){
                scheduleFallingTick( pLevel , pPos , 30 );
                pLevel.setBlock( pPos , pState.setValue( MOVING , true ) , 3 );
            }else{
                pLevel.levelEvent( 1501 , pPos , 0 );
                pLevel.setBlock( pPos , OBlocks.LEAD_BLOCK.get().defaultBlockState() , 3 );
            }
        } else {
            if(!pOldState.getFluidState().is( FluidTags.WATER )){
                pLevel.setBlock( pPos , pState.setValue( MOVING , false ) , 3 );
                pLevel.scheduleTick( pPos , this , 300 );
            }else{
                pLevel.levelEvent( 1501 , pPos , 0 );
                pLevel.setBlock( pPos , OBlocks.LEAD_BLOCK.get().defaultBlockState() , 3 );
            }
        }
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
    }

    @Override
    public void tick(BlockState pState , ServerLevel pLevel , BlockPos pPos , Random pRandom ){
        if (pLevel.getBlockState(pPos.below()).getBlock() == Blocks.AIR || pLevel.getBlockState(pPos.below()).is(BlockTags.REPLACEABLE_PLANTS)
                || pLevel.getBlockState(pPos.below()).getFluidState().is(FluidTags.WATER)
                || pLevel.getBlockState(pPos.below()).is(BlockTags.SMALL_FLOWERS)
                || pLevel.getBlockState(pPos.below()).is(BlockTags.TALL_FLOWERS)){
            pLevel.setBlock( pPos , Blocks.AIR.defaultBlockState() , 67 );
            pLevel.setBlock( pPos.below() , OBlocks.MOLTEN_LEAD.get().defaultBlockState() , 67 );
        }
    }

    private boolean scheduleFallingTick( LevelAccessor pLevel , BlockPos pPos , int pDelay ){
        if(pLevel.getBlockState( pPos.below() ).getBlock() == Blocks.AIR || pLevel.getBlockState(pPos.below()).is(BlockTags.REPLACEABLE_PLANTS)
                || pLevel.getBlockState( pPos.below() ).getFluidState().is(FluidTags.WATER)
                || pLevel.getBlockState( pPos.below() ).is(BlockTags.SMALL_FLOWERS)
                || pLevel.getBlockState(pPos.below()).is(BlockTags.TALL_FLOWERS)){
            pLevel.scheduleTick( pPos , this , pDelay );
            return true;
        }
        return false;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter world, BlockPos blockPos, CollisionContext ctx) {
        if (ctx instanceof EntityCollisionContext eCtx && eCtx.getEntity() != null)
            return ctx.isAbove(STABLE_SHAPE, blockPos, true) && blockState.getValue(LEVEL) == 0 && isEntityLighterThanLead(eCtx.getEntity()) ? STABLE_SHAPE : Shapes.empty();

        return super.getCollisionShape(blockState, world, blockPos, ctx);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockWorld, BlockPos pos, CollisionContext context) {
        if (context.isHoldingItem(OItems.MOLTEN_LEAD_BUCKET.get()))
            return blockWorld.getBlockState(pos.above()) != state ? STABLE_SHAPE : Shapes.block();

        return Shapes.empty();
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return state.is(Blocks.LAVA);
    }


    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (entity.getY() < pos.getY() + STABLE_SHAPE.max(Direction.Axis.Y)) {
            if (!(entity instanceof LivingEntity) || entity.getFeetBlockState().is(this)) {
                entity.makeStuckInBlock(state, new Vec3(0.9F, 1.0D, 0.9F));
            }

            if (entity instanceof LivingEntity living && living.isUsingItem() && living.getUseItemRemainingTicks() == 0 && living.getItemInHand(living.getUsedItemHand()).isEdible()) {
                living.addEffect(new MobEffectInstance(OEffects.STUNNING.get(), 40 * 20));
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
            }
            entity.setSecondsOnFire(10);
            if (!world.isClientSide) entity.setSharedFlagOnFire(true);
        }
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!((double)fallDistance < 4.0D) && entity instanceof LivingEntity living) {
            LivingEntity.Fallsounds fallSound = living.getFallSounds();
            SoundEvent sound = (double)fallDistance < 7.0D ? fallSound.small() : fallSound.big();
            entity.playSound(sound, 1.0F, 1.0F);
        }
    }

    public static boolean isEntityLighterThanLead(Entity entity) {
        if (entity.getType().is(OTags.Entities.LIGHTER_THAN_LEAD)) {
            return true;
        } else {
            return entity instanceof LivingEntity && ((LivingEntity) entity).getItemBySlot(EquipmentSlot.FEET).is(OTags.Items.LIGHTER_THAN_LEAD);
        }
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType pathFinder) {
        return true;
    }
}
